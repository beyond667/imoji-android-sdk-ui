package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiResultView;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiQuarterScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 1;

    public ImojiQuarterScreenWidget(Context context, RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, HORIZONTAL, true, ImojiResultView.SMALL, imageFormat, imageLoader);
        searchBarLayout.setLeftButtonVisibility(GONE);
        setSeparatorVisibility(VISIBLE);

        LinearLayout container = (LinearLayout) this.findViewById(R.id.widget_container);
        container.removeAllViews();
        container.addView(separator);
        container.addView(switcher);
        container.addView(searchBarLayout);


        int height = (int) getResources().getDimension(R.dimen.imoji_search_result_row_height);
        switcher.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int horizontalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_horizontal_padding);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = horizontalPadd;
                outRect.right = horizontalPadd;

                int position = parent.getChildLayoutPosition(view);
                if (position == resultAdapter.getDividerPosition()) {
                    outRect.right = 0;
                } else if (position == 0) {
                    outRect.left = horizontalPadd * 2;
                } else if (position >= state.getItemCount() - SPAN_COUNT) {
                    outRect.right = horizontalPadd * 2;
                }
            }
        });

        searchBarLayout.setRecentsLayout(R.layout.imoji_recents_bar_small);
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBarLayout.getFocusedChild(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onTextCleared() {
        Pair pair = searchHandler.getFirstElement();
        if (pair != null && pair.second != null) {
            searchHandler.searchTrending(context);
        }
    }

    @Override
    protected View getNoStickerView(boolean isRecents) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.imoji_quarter_search_widget_no_result, switcher);

        TextView textView = (TextView) view.findViewById(R.id.replacement_view_text);
        if(isRecents){
            textView.setText(getContext().getString(R.string.imoji_search_widget_no_recent_hint));
        }
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.otf"));

        return view;
    }
}
