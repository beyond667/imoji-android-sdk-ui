package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.CategoriesResponse;
import io.imoji.sdk.response.ImojisResponse;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchBarLayout;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiQuarterScreenWidget extends LinearLayout implements ImojiSearchBarLayout.ImojiSearchListener {

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
        searchBarLayout.setImojiSearchListener(this);
        searchBarLayout.hideLeftButton();


        resultAdapter = new ImojiSearchResultAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setOrientation(HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(resultAdapter);


        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private final int horizontalDivider = (int) getResources()
                    .getDimension(R.dimen.imoji_quarter_search_widget_recycler_inbetween_padding);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = horizontalDivider;

                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.right = horizontalDivider;
                }
            }
        });

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

    private void searchTerm(String term) {
        ImojiSDK.getInstance()
                .createSession(getContext().getApplicationContext())
                .searchImojis(term)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        recyclerView.removeAllViews();
                        resultAdapter.clearSet();
                        for (Imoji imoji : imojisResponse.getImojis()) {
                            resultAdapter.add(new ImojiSearchResultAdapter.SearchResult(imoji.getStandardThumbnailUri(), null));
                        }
                    }
                });
        resultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTextSubmit(String term) {
        searchTerm(term);
    }
}
