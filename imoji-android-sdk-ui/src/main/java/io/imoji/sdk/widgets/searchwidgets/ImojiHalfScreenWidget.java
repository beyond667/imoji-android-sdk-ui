package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.view.ViewGroup;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiHalfScreenWidget extends ImojiBaseSearchWidget {

    public final static int SPAN_COUNT = 2;

    public ImojiHalfScreenWidget(Context context) {
        super(context, SPAN_COUNT, HORIZONTAL, true);
        int rowHeight = (int)getResources().getDimension(R.dimen.imoji_search_result_row_height);
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,rowHeight*SPAN_COUNT));
    }
}
