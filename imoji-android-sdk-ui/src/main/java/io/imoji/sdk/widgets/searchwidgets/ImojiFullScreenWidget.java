package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiFullScreenWidget extends ImojiBaseSearchWidget {

    public ImojiFullScreenWidget(Context context) {
        super(context, 4, VERTICAL, true);
        LinearLayout container = (LinearLayout) this.findViewById(R.id.widget_container);
        container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
