package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;

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

        int horizontalPadding = (int)getResources()
                .getDimension(R.dimen.imoji_search_recycler_horizontal_padding);
        recyclerView.setPadding(horizontalPadding,0,horizontalPadding,0);

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(context));
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
