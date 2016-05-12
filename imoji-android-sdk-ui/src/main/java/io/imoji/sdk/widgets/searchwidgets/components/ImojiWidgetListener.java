package io.imoji.sdk.widgets.searchwidgets.components;

import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;

/**
 * Created by engind on 4/27/16.
 */
public interface ImojiWidgetListener {

    void onBackButtonTapped();

    void onCloseButtonTapped();

    void onCategoryTapped(Category category);

    void onStickerTapped(Imoji imoji);

    void onTermSearched(String term);
}