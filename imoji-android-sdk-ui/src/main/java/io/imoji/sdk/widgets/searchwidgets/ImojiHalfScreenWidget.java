package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiHalfScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 2;

    public ImojiHalfScreenWidget(Context context) {
        super(context, SPAN_COUNT, HORIZONTAL, true);

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
                outRect.right = horizontalPadd;
                if(parent.getChildLayoutPosition(view) % 2 != 0){
                    outRect.top += verticalPadd;
                    outRect.bottom -= (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding)*2;
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
}
