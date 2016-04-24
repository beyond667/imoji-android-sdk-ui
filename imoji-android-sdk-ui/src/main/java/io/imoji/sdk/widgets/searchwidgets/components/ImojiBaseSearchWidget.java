package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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

    private RecyclerView recyclerView;
    private ImojiSearchBarLayout searchBarLayout;
    private ImojiSearchResultAdapter resultAdapter;
    private ImojiWidgetListener widgetListener;

    public ImojiBaseSearchWidget(Context context) {
        super(context);
        init(context);
    }

    public ImojiBaseSearchWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(getContext(), R.layout.imoji_quarter_screen_widget, this);

        recyclerView = (RecyclerView) this.findViewById(R.id.quarter_widget_recycler);
        searchBarLayout = (ImojiSearchBarLayout) this.findViewById(R.id.quarter_widget_search);
        searchBarLayout.setImojiSearchListener(this);
        searchBarLayout.hideLeftButton();

        resultAdapter = new ImojiSearchResultAdapter(context);
        resultAdapter.setSearchTapListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 1, HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(resultAdapter);

        searchCategories(context);
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
        recyclerView.removeAllViews();
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
