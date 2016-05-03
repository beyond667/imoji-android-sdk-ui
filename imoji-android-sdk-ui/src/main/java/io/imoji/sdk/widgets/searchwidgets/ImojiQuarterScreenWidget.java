package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiResultView;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiQuarterScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 1;

    public ImojiQuarterScreenWidget(Context context, RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, HORIZONTAL, false, ImojiResultView.SMALL, imageFormat, imageLoader);
        searchBarLayout.setLeftButtonVisibility(GONE);
        setSeparatorVisibility(VISIBLE);
        int height = (int) getResources().getDimension(R.dimen.imoji_search_result_row_height);
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int horizontalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_horizontal_padding);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = horizontalPadd;
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
        searchBarLayout.requestTextFocus();
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        super.onFocusChanged(hasFocus);
        if (hasFocus) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBarLayout.getFocusedChild(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onTextCleared() {
        super.onTextCleared();
        if (historyStack.peek().second != null) {
            searchTrending();
        }
    }
}
