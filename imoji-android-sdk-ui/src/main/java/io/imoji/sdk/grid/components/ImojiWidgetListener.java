package io.imoji.sdk.grid.components;

import io.imoji.sdk.objects.Imoji;

/**
 * Created by engind on 4/27/16.
 */
public interface ImojiWidgetListener {

    void onCloseButtonTapped();

    void onStickerTapped(Imoji imoji);
}