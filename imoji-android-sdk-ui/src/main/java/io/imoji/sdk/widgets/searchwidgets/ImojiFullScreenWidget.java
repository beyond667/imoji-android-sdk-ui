package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiResultView;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiFullScreenWidget extends ImojiBaseSearchWidget {

    private final static int SPAN_COUNT = 3;

    public ImojiFullScreenWidget(Context context, RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, VERTICAL, true, ImojiResultView.LARGE, imageFormat, imageLoader);

        searchBarLayout.setupCloseButton();
        searchBarLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.imoji_search_bar_height_full_widget)));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                //TODO // FIXME: 5/3/16
                int resultSize = (int) getResources().getDimension(R.dimen.imoji_result_width_large);
                int padding = (recyclerView.getWidth() - resultSize * SPAN_COUNT) / (SPAN_COUNT * 2);
                if (parent.getChildLayoutPosition(view) != resultAdapter.getDividerPosition()) {
                    outRect.right = outRect.right - padding;
                    outRect.left = padding;
                    outRect.bottom = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding);
                } else {
                    outRect.top = (int) getContext().getResources().getDimension(R.dimen.imoji_search_vertical_recycler_divider_top_margin);
                    outRect.bottom = (int) getContext().getResources().getDimension(R.dimen.imoji_search_vertical_recycler_divider_bottom_margin);
                }

            }
        });

        searchBarLayout.toggleTextFocus(false);
    }

    @Override
    protected void onHistoryCreated() {
        super.onHistoryCreated();
        searchBarLayout.setupBackButton();
    }

    @Override
    protected void onHistoryDestroyed() {
        super.onHistoryDestroyed();
        searchBarLayout.setupCloseButton();
    }

    @Override
    protected View getNoStickerView() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.imoji_full_search_widget_no_result, switcher);

        TextView text = (TextView) view.findViewById(R.id.replacement_view_text);
        TextView searchTitle = (TextView) view.findViewById(R.id.replacement_search_label);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf");
        text.setTypeface(typeface);
        searchTitle.setTypeface(typeface);

        View v = findViewById(R.id.replacement_search_container);

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHandler.retrySearch(context);
            }
        });

        return view;
    }
}
