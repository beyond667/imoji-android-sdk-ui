package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.CategoryFetchOptions;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.CategoriesResponse;
import io.imoji.sdk.response.ImojisResponse;

/**
 * Created by engind on 5/6/16.
 */
public abstract class ImojiSearchHandler {

    private Stack<Pair<String, String>> historyStack;


    public ImojiSearchHandler() {
        historyStack = new Stack<Pair<String, String>>() {

            @Override
            public Pair<String, String> push(Pair<String, String> object) {
                if (size() == 0) {
                    onFirstHistoryItemAdded();
                }
                Pair<String, String> pair = super.push(object);
                onHistoryChanged();
                return pair;
            }

            @Override
            public synchronized Pair<String, String> pop() {
                Pair<String, String> popped = super.pop();
                if (size() == 0) {
                    onLastHistoryItemRemoved();
                }
                onHistoryChanged();
                return popped;
            }
        };

    }

    public void searchTerm(Context context, final String term, final String title, final boolean addToHistory) {
        ImojiSDK.getInstance()
                .createSession(context.getApplicationContext())
                .searchImojis(term)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        List<SearchResult> newResults = new ArrayList<SearchResult>();
                        for (Imoji imoji : imojisResponse.getImojis()) {
                            newResults.add(new SearchResult(imoji));
                        }
                        if (!imojisResponse.getRelatedCategories().isEmpty()) {
                            newResults.add(new SearchResult((Imoji) null));
                        }
                        for (Category c : imojisResponse.getRelatedCategories()) {
                            newResults.add(new SearchResult(c));
                        }
                        onSearchCompleted(newResults,
                                !imojisResponse.getRelatedCategories().isEmpty() ? imojisResponse.getImojis().size() : -1);

                        if (addToHistory) {
                            historyStack.push(new Pair<>(term, title));
                        }
                    }
                });
    }

    public void searchTrending(Context context) {
        ImojiSDK.getInstance()
                .createSession(context.getApplicationContext())
                .getImojiCategories(new CategoryFetchOptions(Category.Classification.Trending))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
                    @Override
                    protected void onPostExecute(CategoriesResponse categoriesResponse) {
                        List<SearchResult> newResults = new ArrayList<SearchResult>();
                        for (Category category : categoriesResponse.getCategories()) {
                            newResults.add(new SearchResult(category));
                        }
                        onSearchCompleted(newResults, -1);
                    }
                });
    }

    public void searchPrevious(Context context) {
        try {
            historyStack.pop();
        } catch (EmptyStackException e) {
            searchTrending(context);
        }
        if (!historyStack.isEmpty()) {
            Pair<String, String> pair = historyStack.peek();
            searchTerm(context, pair.first, pair.second, false);
        } else {
            searchTrending(context);
        }
    }

    public Pair getFirstElement() {
        try {
            return historyStack.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public abstract void onSearchCompleted(List<SearchResult> newResults, int dividerPosition);

    public abstract void onFirstHistoryItemAdded();

    public abstract void onLastHistoryItemRemoved();

    public abstract void onHistoryChanged();
}
