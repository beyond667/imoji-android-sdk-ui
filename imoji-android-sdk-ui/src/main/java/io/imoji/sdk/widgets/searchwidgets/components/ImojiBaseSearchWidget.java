package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.CategoriesResponse;
import io.imoji.sdk.response.ImojisResponse;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchBarLayout.ImojiSearchBarListener;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter.ImojiSearchTapListener;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter.SearchResult;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiBaseSearchWidget extends LinearLayout implements ImojiSearchBarListener, ImojiSearchTapListener {

    protected RecyclerView recyclerView;
    protected ImojiSearchBarLayout searchBarLayout;
    private ImojiSearchResultAdapter resultAdapter;
    private ImojiWidgetListener widgetListener;
    private GridLayoutManager gridLayoutManager;

    public ImojiBaseSearchWidget(Context context, int spanCount, int orientation, boolean searchOnTop) {
        super(context);
        inflate(getContext(), R.layout.imoji_base_widget, this);

        recyclerView = (RecyclerView) this.findViewById(R.id.widget_recycler);
        searchBarLayout = (ImojiSearchBarLayout) this.findViewById(R.id.widget_search);
        searchBarLayout.setImojiSearchListener(this);

        if (searchOnTop) {
            LinearLayout container = (LinearLayout) this.findViewById(R.id.widget_container);
            container.removeAllViews();
            container.addView(searchBarLayout, 0);
            container.addView(recyclerView, 1);
        }

        resultAdapter = new ImojiSearchResultAdapter(context);
        resultAdapter.setSearchTapListener(this);
        gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), spanCount, orientation, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(resultAdapter);

        searchCategories(context);
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalPadding;
        private final int horizontalPadding;

        public VerticalSpaceItemDecoration(Context context) {
            this.verticalPadding = (int) context.getResources()
                    .getDimension(R.dimen.imoji_search_recycler_vertical_padding);
            this.horizontalPadding = (int) context.getResources()
                    .getDimension(R.dimen.imoji_search_recycler_horizontal_padding);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalPadding;
            outRect.left = horizontalPadding;
            outRect.right = horizontalPadding;
        }
    }


    private void searchTerm(String term) {
        ImojiSDK.getInstance()
                .createSession(getContext().getApplicationContext())
                .searchImojis(term)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        List<SearchResult> newResults = new ArrayList<SearchResult>();
                        for (Imoji imoji : imojisResponse.getImojis()) {
                            newResults.add(new ImojiSearchResultAdapter.SearchResult(imoji.getStandardThumbnailUri(), null));
                        }
                        repopulateAdapter(newResults);
                    }
                });
    }

    private void searchCategories(Context context) {
        ImojiSDK.getInstance()
                .createSession(context.getApplicationContext())
                .getImojiCategories(Category.Classification.Trending)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
                    @Override
                    protected void onPostExecute(CategoriesResponse categoriesResponse) {
                        List<SearchResult> newResults = new ArrayList<SearchResult>();
                        for (Category category : categoriesResponse.getCategories()) {
                            newResults.add(new ImojiSearchResultAdapter.SearchResult(category.getPreviewImoji().getStandardThumbnailUri(),
                                    category.getTitle()));
                        }
                        repopulateAdapter(newResults);
                    }
                });
    }

    private void repopulateAdapter(List<SearchResult> newResults) {
        resultAdapter.repopulate(newResults);
    }

    @Override
    public void onTextSubmit(String term) {
        searchTerm(term);
        if (this.widgetListener != null) {
            widgetListener.onTermSearched(term);
        }
    }

    @Override
    public void onBackButtonTapped() {
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

    public void setWidgetListener(ImojiWidgetListener widgetListener) {
        this.widgetListener = widgetListener;
    }

    @Override
    public void onTap(SearchResult searchResult) {
        if (searchResult.isCategory()) {
            gridLayoutManager.scrollToPositionWithOffset(0, 0);
            searchTerm(searchResult.getTitle());
            if (this.widgetListener != null) {
                this.widgetListener.onCategoryTapped();
            }
        } else {
            if (this.widgetListener != null) {
                this.widgetListener.onStickerTapped();
            }
        }
    }

    public interface ImojiWidgetListener {

        void onBackButtonTapped();

        void onCloseButtonTapped();

        void onCategoryTapped();

        void onStickerTapped();

        void onTermSearched(String term);
    }
}
