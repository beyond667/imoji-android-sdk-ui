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

    public Uri getThumbnailUri() {
        Imoji thumbailImoji = this.imoji;
        if (isCategory()) {
            thumbailImoji = category.getPreviewImoji();
        }
        return thumbailImoji.getStandardThumbnailUri(true);
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

    public Uri getUri(RenderingOptions.ImageFormat imageFormat) {
        return getImoji().urlForRenderingOption(new RenderingOptions(
                RenderingOptions.BorderStyle.Sticker,
                imageFormat,
                RenderingOptions.Size.Thumbnail));
    }
}
