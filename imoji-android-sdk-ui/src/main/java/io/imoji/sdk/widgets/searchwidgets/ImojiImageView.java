package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.ImageView;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/19/16.
 */
public class ImojiImageView extends ImageView {

    public ImojiImageView(Context context, @ColorRes int placeholderColor) {
        this(context);
        setImageDrawable(createPlaceholder(placeholderColor));
    }

    public ImojiImageView(Context context, Bitmap imojiSticker) {
        this(context);
        setImageBitmap(imojiSticker);
    }

    private ImojiImageView(Context context) {
        super(context);

        int padding = (int) getResources().getDimension(R.dimen.imoji_box_padding);
        setPadding(padding, padding, padding, padding);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.test)).getBitmap());
            }
        });
    }

    private Drawable createPlaceholder(@ColorRes int placeholderColor) {
        GradientDrawable placeholder = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ColorUtils.setAlphaComponent(getResources().getColor(placeholderColor), 0),
                        ColorUtils.setAlphaComponent(getResources().getColor(placeholderColor), 16)});
        placeholder.setShape(GradientDrawable.OVAL);
        return placeholder;
    }
}
