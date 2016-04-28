package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.EmptyStackException;
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
    private View separator;
    private RenderingOptions.ImageFormat imageFormat = RenderingOptions.ImageFormat.WebP;

    protected Stack<Pair<String, String>> historyStack;

    public ImojiBaseSearchWidget(Context context, int spanCount, int orientation,
                                 boolean searchOnTop, RenderingOptions.ImageFormat imageFormat,ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context);
        inflate(getContext(), R.layout.imoji_base_widget, this);
        this.imageFormat = imageFormat;

        historyStack = new Stack<Pair<String, String>>() {

            @Override
            public Pair<String, String> push(Pair<String, String> object) {
                if (size() == 0) {
                    onHistoryCreated();
                }
                Pair<String, String> pair = super.push(object);
                updateText();
                return pair;
            }

            @Override
            public synchronized Pair<String, String> pop() {
                Pair<String, String> popped = super.pop();
                if (size() == 0) {
                    onHistoryDestroyed();
                }
                updateText();
                return popped;
            }
        };

        recyclerView = (RecyclerView) this.findViewById(R.id.widget_recycler);
        searchBarLayout = (ImojiSearchBarLayout) this.findViewById(R.id.widget_search);
        separator = this.findViewById(R.id.sticker_separator);
        searchBarLayout.setImojiSearchListener(this);

        if (searchOnTop) {
            LinearLayout container = (LinearLayout) this.findViewById(R.id.widget_container);
            container.removeAllViews();
            container.addView(searchBarLayout, 0);
            container.addView(separator, 1);
            container.addView(recyclerView, 2);
        }

        resultAdapter = new ImojiSearchResultAdapter(context,imageLoader);
        resultAdapter.setSearchTapListener(this);
        gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), spanCount, orientation, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(resultAdapter);

        searchTrending();
    }

    private void searchTerm(String term, String title, boolean addToHistory) {
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
                        for(Category c :imojisResponse.getRelatedCategories()){
                            newResults.add(new ImojiSearchResultAdapter.SearchResult(c));
                        }
                        repopulateAdapter(newResults);
                    }
                });
        if (addToHistory) {
            historyStack.push(new Pair<String, String>(term, title));
        }
    }

    private void searchTrending() {
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

    private void searchPrevious(){
        try{
            historyStack.pop();
        }catch (EmptyStackException e){
            searchTrending();
        }
        if(!historyStack.isEmpty()){
            Pair<String,String> pair = historyStack.peek();
            searchTerm(pair.first,pair.second,false);
        }else{
            searchTrending();
        }
    }


    private void repopulateAdapter(List<SearchResult> newResults) {
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
        resultAdapter.repopulate(newResults);
    }

    @Override
    public void onTextSubmit(String term) {
        searchTerm(term, null, true);
        if (this.widgetListener != null) {
            widgetListener.onTermSearched(term);
        }
    }

    @Override
    public void onBackButtonTapped() {
        searchPrevious();
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
    public void onFocusChanged(boolean hasFocus) {

    }

    public void setWidgetListener(ImojiWidgetListener widgetListener) {
        this.widgetListener = widgetListener;
    }

    @Override
    public void onTap(SearchResult searchResult) {
        if (searchResult.isCategory()) {
            searchTerm(searchResult.getCategory().getIdentifier(), searchResult.getCategory().getTitle(), true);
            if (this.widgetListener != null) {
                this.widgetListener.onCategoryTapped();
            }
        } else {
            if (this.widgetListener != null) {
                this.widgetListener.onStickerTapped(searchResult.getUri(imageFormat));
            }
        }
    }

    private void updateText() {
        try{
            Pair<String,String> pair = historyStack.peek();
            String text = pair.first;
            if (pair.second != null) {
                text = pair.second;
            }
            searchBarLayout.setText(text);
        }catch (EmptyStackException e){
            searchBarLayout.setText("");
        }
    }

    protected void setSeparatorVisibility(int visibility) {
        separator.setVisibility(visibility);
    }

    protected void onHistoryCreated() {

    }

    protected void onHistoryDestroyed() {

    }
}
