package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;

import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;

/**
 * Created by engind on 4/24/16.
 */
public class ImojiQuarterScreenWidget extends ImojiBaseSearchWidget {

    public ImojiQuarterScreenWidget(Context context) {
        super(context, 1, HORIZONTAL, false);
        searchBarLayout.hideLeftButton();
    }
}
