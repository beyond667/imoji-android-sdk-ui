package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiUISDKOptions;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiResultView;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiHalfScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 2;

    public ImojiHalfScreenWidget(Context context, ImojiUISDKOptions options, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, HORIZONTAL, false, ImojiResultView.SMALL, options, imageLoader);

        searchBarLayout.setLeftButtonVisibility(GONE);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.base_widget_separator));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int verticalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding);
            private int horizontalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_horizontal_padding);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = horizontalPadd;
                outRect.bottom = verticalPadd;
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

        searchBarLayout.toggleTextFocus(false);
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
    public void onTextCleared() {

    }

    @Override
    public void onFocusChanged(final boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBarLayout.getWindowToken(), 0);
        }

        if(hasFocus){
            searchBarLayout.setLeftButtonVisibility(VISIBLE);
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switcher.setVisibility(hasFocus ? GONE : VISIBLE);
            }
        }, 100);
    }

    @Override
    protected View getNoStickerView(boolean isRecents) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.imoji_half_search_widget_no_result, switcher);


        TextView textView = (TextView) view.findViewById(R.id.replacement_view_text);
        if(isRecents){
            textView.setText(getContext().getString(R.string.imoji_search_widget_no_recent_hint));
        }
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.otf"));

        return view;
    }
}
