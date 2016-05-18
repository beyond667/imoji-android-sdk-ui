/*
 * Imoji Android SDK UI
 * Created by engind
 *
 * Copyright (C) 2016 Imoji
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KID, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package io.imoji.sdk.grid.components;

import io.imoji.sdk.objects.RenderingOptions;

/**
 * Created by engind on 5/10/16.
 * A simple configuration POJO for developers pass their preferences
 */
public class ImojiUISDKOptions {

    private RenderingOptions.ImageFormat imageFormat = RenderingOptions.ImageFormat.WebP;
    private boolean includeRecentsAndCreate = true;
    private boolean displayStickerBorders = true;

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
}
