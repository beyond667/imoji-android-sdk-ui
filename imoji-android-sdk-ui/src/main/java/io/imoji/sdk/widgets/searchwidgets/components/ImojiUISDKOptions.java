package io.imoji.sdk.widgets.searchwidgets.components;

import io.imoji.sdk.objects.RenderingOptions;

/**
 * Created by engind on 5/10/16.
 */
public class ImojiUISDKOptions {

    private RenderingOptions.ImageFormat imageFormat = RenderingOptions.ImageFormat.WebP;
    private boolean includeRecentsAndCreate = true;
    private boolean displayBorderSticker = true;

    public RenderingOptions.ImageFormat getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(RenderingOptions.ImageFormat imageFormat) {
        this.imageFormat = imageFormat;
    }

    public boolean isIncludeRecentsAndCreate() {
        return includeRecentsAndCreate;
    }

    public void setIncludeRecentsAndCreate(boolean includeRecentsAndCreate) {
        this.includeRecentsAndCreate = includeRecentsAndCreate;
    }

    public boolean isDisplayBorderSticker() {
        return displayBorderSticker;
    }

    public void setDisplayStickerBorder(boolean displayStickerBorder) {
        this.displayBorderSticker = displayStickerBorder;
    }
}
