package io.imoji.sdk.widgets.searchwidgets.components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import java.util.List;

import io.imoji.sdk.ui.ImojiEditorActivity;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.ui.utils.EditorBitmapCache;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter.ImojiSearchTapListener;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiResultView;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiSearchBarLayout;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiSearchBarLayout.ImojiSearchBarListener;

/**
 * Created by engind on 4/22/16.
 */
public abstract class ImojiBaseSearchWidget extends LinearLayout implements ImojiSearchBarListener, ImojiSearchTapListener {

    protected ViewSwitcher switcher;
    protected RecyclerView recyclerView;
    protected ImojiSearchBarLayout searchBarLayout;
    protected ImojiSearchResultAdapter resultAdapter;
    protected ImojiSearchHandler searchHandler;
    protected Context context;

    private ImojiWidgetListener widgetListener;
    private GridLayoutManager gridLayoutManager;
    private ImojiUISDKOptions options;


    public ImojiBaseSearchWidget(Context context, final int spanCount, int orientation, boolean autoSearchEnabled, @ImojiResultView.ResultViewSize int resultViewSize,
                                 ImojiUISDKOptions options, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context);
        inflate(getContext(), R.layout.imoji_base_widget, this);
        this.context = context;
        this.options = options;

        this.searchHandler = new ImojiSearchHandler(autoSearchEnabled) {

            @Override
            public void onSearchCompleted(List<SearchResult> newResults, int dividerPosition, boolean isRecents) {
                repopulateAdapter(newResults, dividerPosition, isRecents);
            }

            @Override
            public void beforeSearchStarted() {
                switcher.setDisplayedChild(0);
            }

            @Override
            public void onFirstHistoryItemAdded() {
                onHistoryCreated();
            }

            @Override
            public void onLastHistoryItemRemoved() {
                onHistoryDestroyed();
            }

            @Override
            public void onHistoryChanged() {
                updateText();
            }
        };

        switcher = (ViewSwitcher) this.findViewById(R.id.widget_switcher);
        recyclerView = (RecyclerView) this.findViewById(R.id.widget_recycler);
        searchBarLayout = (ImojiSearchBarLayout) this.findViewById(R.id.widget_search);
        searchBarLayout.setImojiSearchListener(this);
        searchBarLayout.setExtraButtonsEnabled(options.isIncludeRecentsAndCreate());

        resultAdapter = new ImojiSearchResultAdapter(context, imageLoader, resultViewSize, orientation,options);
        resultAdapter.setSearchTapListener(this);
        gridLayoutManager = new GridLayoutManager(context, spanCount, orientation, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (resultAdapter.getItemViewType(position)) {
                    case ImojiSearchResultAdapter.DIVIDER_VIEW_TYPE:
                        return spanCount;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(resultAdapter);

        searchHandler.searchTrending(context);

    }

    private void repopulateAdapter(List<SearchResult> newResults, int dividerPosition, boolean isRecents) {
        updateRecyclerView(newResults.size(),isRecents);
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
        resultAdapter.repopulate(newResults, dividerPosition);
    }


    private void updateRecyclerView(int newSize,boolean isRecents) {
        if (newSize == 0) {
            if (switcher.getChildAt(1) != null) {
                switcher.removeViewAt(1);
            }
            getNoStickerView(isRecents);
            switcher.setDisplayedChild(1);
        }
    }

    @Override
    public void onTextSubmit(String term) {
        searchHandler.searchTerm(context, term, null, true);
        if (this.widgetListener != null) {
            widgetListener.onTermSearched(term);
        }
    }

    @Override
    public void onBackButtonTapped() {
//        searchHandler.searchPrevious(context);
        searchHandler.clearHistory();
        searchHandler.searchTrending(context);
        if (this.widgetListener != null) {
            widgetListener.onBackButtonTapped();
        }
    }

    @Override
    public void onCloseButtonTapped() {
        if (this.widgetListener != null) {
            widgetListener.onCloseButtonTapped();
        }
    }

    @Override
    public void onTextChanged(String term, boolean shouldTriggerAutoSearch) {
        if (shouldTriggerAutoSearch) {
            searchHandler.autoSearch(context, term);
        }
    }

    public void setWidgetListener(ImojiWidgetListener widgetListener) {
        this.widgetListener = widgetListener;
    }

    @Override
    public void onRecentsButtonTapped() {
        searchHandler.searchRecents(context);
    }

    @Override
    public void onCreateButtonTapped() {
        startImojiEditorActivity(options.getParentActivity());
    }

    @Override
    public void onTap(SearchResult searchResult) {
        if (searchResult.isCategory()) {
            searchHandler.searchTerm(context, searchResult.getCategory().getIdentifier(),
                    searchResult.getCategory().getTitle(), true);
            if (this.widgetListener != null) {
                this.widgetListener.onCategoryTapped(searchResult.getCategory());
            }
        } else {
            if (this.widgetListener != null) {
                this.widgetListener.onStickerTapped(searchResult.getImoji());
                searchHandler.addToRecents(context,searchResult.getImoji());
            }
        }
    }

    private void updateText() {
        Pair<String, String> pair = searchHandler.getFirstElement();
        if (pair != null) {
            String text = pair.first;
            if (pair.second != null) {
                text = pair.second;
            }
            searchBarLayout.setText(text);
        } else {
            searchBarLayout.setText("");
        }
    }

    private void startImojiEditorActivity(Activity activity) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imoji_noresults_graphic_large, options);
        EditorBitmapCache.getInstance().put(EditorBitmapCache.Keys.INPUT_BITMAP, bitmap);
        Intent intent = new Intent(activity, io.imoji.sdk.ui.ImojiEditorActivity.class);
        intent.putExtra(ImojiEditorActivity.RETURN_IMMEDIATELY_BUNDLE_ARG_KEY, false);
        intent.putExtra(ImojiEditorActivity.TAG_IMOJI_BUNDLE_ARG_KEY, true);
        activity.startActivityForResult(intent, ImojiEditorActivity.START_EDITOR_REQUEST_CODE);
    }

    protected void onHistoryCreated() {

    }

    protected void onHistoryDestroyed() {

    }

    protected abstract View getNoStickerView(boolean isRecents);
}
