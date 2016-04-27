package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.CategoryFetchOptions;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.objects.RenderingOptions;
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
    private RenderingOptions.ImageFormat imageFormat = RenderingOptions.ImageFormat.WebP;

    protected Stack<String> historyStack;

    public ImojiBaseSearchWidget(Context context, int spanCount, int orientation, boolean searchOnTop, RenderingOptions.ImageFormat imageFormat) {
        super(context);
        inflate(getContext(), R.layout.imoji_base_widget, this);
        this.imageFormat = imageFormat;

        historyStack = new Stack<String>() {

            @Override
            public String push(String object) {
                if (size() == 0) {
                    onHistoryCreated();
                }
                return super.push(object);
            }

            @Override
            public synchronized String pop() {
                String popped = super.pop();
                if (size() == 0) {
                    onHistoryDestroyed();
                }
                return popped;
            }
        };

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

        searchCategories();
    }

    public ImojiBaseSearchWidget(Context context, int spanCount, int orientation, boolean searchOnTop) {
        this(context,spanCount,orientation,searchOnTop, RenderingOptions.ImageFormat.WebP);
    }


    private void searchTerm(String term, boolean addToHistory) {
        ImojiSDK.getInstance()
                .createSession(getContext().getApplicationContext())
                .searchImojis(term)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        List<SearchResult> newResults = new ArrayList<SearchResult>();
                        for (Imoji imoji : imojisResponse.getImojis()) {
                            newResults.add(new ImojiSearchResultAdapter.SearchResult(imoji));
                        }
                        repopulateAdapter(newResults);
                    }
                });
        if (addToHistory) {
            historyStack.push(term);
        }
    }

    private void searchCategories() {
        ImojiSDK.getInstance()
                .createSession(getContext().getApplicationContext())
                .getImojiCategories(new CategoryFetchOptions(Category.Classification.Trending))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
                    @Override
                    protected void onPostExecute(CategoriesResponse categoriesResponse) {
                        List<SearchResult> newResults = new ArrayList<SearchResult>();
                        for (Category category : categoriesResponse.getCategories()) {
                            newResults.add(new ImojiSearchResultAdapter.SearchResult(category));
                        }
                        repopulateAdapter(newResults);
                    }
                });
    }

    private void repopulateAdapter(List<SearchResult> newResults) {
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
        resultAdapter.repopulate(newResults);
    }

    @Override
    public void onTextSubmit(String term) {
        searchTerm(term, true);
        if (this.widgetListener != null) {
            widgetListener.onTermSearched(term);
        }
    }

    @Override
    public void onBackButtonTapped() {
        historyStack.pop();
        if (historyStack.size() == 0) {
            searchCategories();
        } else {
            searchTerm(historyStack.peek(), false);
        }
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
            searchTerm(searchResult.getCategory().getIdentifier(), true);
            if (this.widgetListener != null) {
                this.widgetListener.onCategoryTapped();
            }
        } else {
            if (this.widgetListener != null) {
                this.widgetListener.onStickerTapped(searchResult.getUri(imageFormat));
            }
        }
    }

    protected void onHistoryCreated() {

    }

    protected void onHistoryDestroyed(){

    }

    public interface ImojiWidgetListener {

        void onBackButtonTapped();

        void onCloseButtonTapped();

        void onCategoryTapped();

        void onStickerTapped(Uri uri);

        void onTermSearched(String term);
    }
}
