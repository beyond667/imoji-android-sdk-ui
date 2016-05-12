package io.imoji.sdk.widgets.searchwidgets.components;

import android.app.Activity;

import io.imoji.sdk.objects.RenderingOptions;

/**
 * Created by engind on 5/10/16.
 */
public class ImojiUISDKOptions {

    private RenderingOptions.ImageFormat imageFormat = RenderingOptions.ImageFormat.WebP;
    private boolean includeRecentsAndCreate = true;
    private boolean displayStickerBorders = true;
    private Activity parentActivity;

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

    public boolean isDisplayStickerBorders() {
        return displayStickerBorders;
    }

    public void setDisplayStickerBorders(boolean displayStickerBorders) {
        this.displayStickerBorders = displayStickerBorders;
    }

    public Activity getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }
}
