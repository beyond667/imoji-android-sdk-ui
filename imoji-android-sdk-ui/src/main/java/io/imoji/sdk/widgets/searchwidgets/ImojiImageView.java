package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/19/16.
 */
public class ImojiImageView extends ImageView {

    public ImojiImageView(Context context, @ColorRes int placeholderColor) {
        super(context);
        int side = (int) getResources().getDimension(R.dimen.imoji_image_view_placeholder_padding);
        setPadding(side, side, side, side);
        setImageDrawable(createPlaceholder(placeholderColor));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImojiSticker();
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

    private void displayImojiSticker() {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.imoji_fade_out);
        startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setPadding(0,0,0,0);
                setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.test)).getBitmap());
                Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.imoji_fade_in);
                startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
