package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiFullScreenWidget extends ImojiBaseSearchWidget {

    private final static int SPAN_COUNT = 4;

    public ImojiFullScreenWidget(Context context,RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, SPAN_COUNT, VERTICAL, true, imageFormat,imageLoader);
        LinearLayout container = (LinearLayout) this.findViewById(R.id.widget_container);
        container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        searchBarLayout.setupCloseButton();

        searchBarLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int)getResources().getDimension(R.dimen.imoji_search_bar_height_full_widget)));


        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int resultSize = (int) getResources().getDimension(R.dimen.imoji_search_result_width);
                int padding = (recyclerView.getWidth() - resultSize * SPAN_COUNT) / (SPAN_COUNT * 2);
                outRect.right = outRect.right - padding;
                outRect.left = padding;
                outRect.bottom = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_vertical_padding);
            }
        });
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
}
