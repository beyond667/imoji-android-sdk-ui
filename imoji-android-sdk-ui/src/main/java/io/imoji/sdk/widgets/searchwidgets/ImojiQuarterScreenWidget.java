package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiQuarterScreenWidget extends ImojiBaseSearchWidget {

    public ImojiQuarterScreenWidget(Context context, RenderingOptions.ImageFormat imageFormat, ImojiSearchResultAdapter.ImojiImageLoader imageLoader) {
        super(context, 1, HORIZONTAL, false, imageFormat, imageLoader);
        searchBarLayout.setLeftButtonVisibility(GONE);
        setSeparatorVisibility(VISIBLE);


        int horizontalPadding = (int) getResources()
                .getDimension(R.dimen.imoji_search_recycler_horizontal_padding);
        recyclerView.setPadding(horizontalPadding, 0, horizontalPadding, 0);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int horizontalPadd = (int) getContext().getResources().getDimension(R.dimen.imoji_search_recycler_horizontal_padding);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = horizontalPadd;
                outRect.right = horizontalPadd;
            }
        });
        searchBarLayout.requestTextFocus();


    }
}
