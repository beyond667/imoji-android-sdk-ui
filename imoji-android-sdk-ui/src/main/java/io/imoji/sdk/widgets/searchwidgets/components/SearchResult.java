package io.imoji.sdk.widgets.searchwidgets.components;

import android.net.Uri;

import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.objects.RenderingOptions;

/**
 * Created by engind on 4/29/16.
 */
public class SearchResult {

    private Imoji imoji;
    private Category category;

    public SearchResult(Imoji imoji) {
        this.imoji = imoji;
    }

    public SearchResult(Category category) {
        this.category = category;
    }

    public Imoji getImoji() {
        return imoji;
    }

    public Category getCategory() {
        return category;
    }

    public Uri getThumbnailUri(ImojiUISDKOptions options) {
        Imoji thumbailImoji = this.imoji;
        if (isCategory()) {
            thumbailImoji = category.getPreviewImoji();
        }
        if (thumbailImoji.hasAnimationCapability()) {
            return thumbailImoji.getStandardThumbnailUri(true);
        } else {
            RenderingOptions renderingOptions = new RenderingOptions(
                    options.isDisplayStickerBorders() ? RenderingOptions.BorderStyle.Sticker : RenderingOptions.BorderStyle.None,
                    options.getImageFormat(), RenderingOptions.Size.Thumbnail);
            return thumbailImoji.urlForRenderingOption(renderingOptions);
        }
    }

    public String getTitle() {
        String title = null;
        if (isCategory()) {
            title = category.getTitle();
        }
        return title;
    }

    public boolean isCategory() {
        return category != null && imoji == null;
    }
}
