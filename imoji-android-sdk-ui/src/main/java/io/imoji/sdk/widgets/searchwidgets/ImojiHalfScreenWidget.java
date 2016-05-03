package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiResultView;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiHalfScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 2;

    public ImojiHalfScreenWidget(Context context, RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, HORIZONTAL, true, ImojiResultView.SMALL, imageFormat, imageLoader);

        searchBarLayout.setLeftButtonVisibility(GONE);
        setSeparatorVisibility(VISIBLE);

//        int row = (int) getContext().getResources().getDimension(R.dimen.imoji_search_result_row_height);
//        int padd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_result_row_top_margin);
//
//        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SPAN_COUNT * (row + padd)));
//
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//
//            private int verticalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding);
//            private int horizontalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_horizontal_padding);
//
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.left = horizontalPadd;
//                outRect.right = horizontalPadd;
//                if (parent.getChildLayoutPosition(view) % 2 != 0) {
//                    outRect.top += verticalPadd;
//                    outRect.bottom -= (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding) * 2;
//                }
//
//            }
//        });
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
    public void onFocusChanged(boolean hasFocus) {
        super.onFocusChanged(hasFocus);
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBarLayout.getWindowToken(), 0);
        }
        recyclerView.setVisibility(hasFocus ? GONE : VISIBLE);
    }
}
