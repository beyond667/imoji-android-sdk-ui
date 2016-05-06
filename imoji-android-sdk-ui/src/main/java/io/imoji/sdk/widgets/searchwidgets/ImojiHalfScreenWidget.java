package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiResultView;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiHalfScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 2;

    public ImojiHalfScreenWidget(Context context, RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, HORIZONTAL, true, false, ImojiResultView.SMALL, imageFormat, imageLoader);

        searchBarLayout.setLeftButtonVisibility(GONE);
        setSeparatorVisibility(VISIBLE);

        int row = (int) getContext().getResources().getDimension(R.dimen.imoji_search_result_row_height);
        int padd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_result_row_top_margin);

        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SPAN_COUNT * (row + padd)));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int verticalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding);
            private int horizontalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_horizontal_padding);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = horizontalPadd;
                outRect.bottom = verticalPadd;
                outRect.right = horizontalPadd;

                int position = parent.getChildLayoutPosition(view);

                if(position == resultAdapter.getDividerPosition()){
                    outRect.right = 0;
                }else if (position == 0){
                    outRect.left = horizontalPadd*2;
                }else if (position >= state.getItemCount()-SPAN_COUNT){
                    outRect.right = horizontalPadd*2;
                }
            }
        });
    }

    @Override
    protected void onHistoryCreated() {
        super.onHistoryCreated();
        searchBarLayout.setLeftButtonVisibility(VISIBLE);
    }

    @Override
    protected void onHistoryDestroyed() {
        super.onHistoryDestroyed();
        searchBarLayout.setLeftButtonVisibility(GONE);
    }

    @Override
    public void onFocusChanged(final boolean hasFocus) {
        super.onFocusChanged(hasFocus);
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBarLayout.getWindowToken(), 0);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(hasFocus ? GONE : VISIBLE);
            }
        }, 100);
    }
}
