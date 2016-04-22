package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.response.CategoriesResponse;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchBarLayout;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiQuarterScreenWidget extends LinearLayout {

    private RecyclerView recyclerView;
    private ImojiSearchBarLayout searchBarLayout;
    private ImojiSearchResultAdapter resultAdapter;

    public ImojiQuarterScreenWidget(Context context) {
        super(context);
        init(context);
    }

    public ImojiQuarterScreenWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(getContext(), R.layout.imoji_quarter_screen_widget, this);

        recyclerView = (RecyclerView) this.findViewById(R.id.quarter_widget_recycler);
        searchBarLayout = (ImojiSearchBarLayout) this.findViewById(R.id.quarter_widget_search);

        resultAdapter = new ImojiSearchResultAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setOrientation(HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(resultAdapter);
        ImojiSDK.getInstance()
                .createSession(context.getApplicationContext())
                .getImojiCategories(Category.Classification.Trending)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
                    @Override
                    protected void onPostExecute(CategoriesResponse categoriesResponse) {
                        for (Category category : categoriesResponse.getCategories()) {
                            resultAdapter.add(new ImojiSearchResultAdapter.SearchResult(category.getPreviewImoji().getStandardThumbnailUri(),
                                    category.getTitle()));
                        }
                    }
                });
        resultAdapter.notifyDataSetChanged();
    }
}
