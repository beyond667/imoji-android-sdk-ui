package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/20/16.
 */
public class ImojiSearchResultLayout extends RelativeLayout {

    private final static int GRADIENT_START_ALPHA = 0;
    private final static int GRADIENT_END_ALPHA = 16;

    private ImageView imageView;
    private TextView textView;

    public ImojiSearchResultLayout(Context context, @ColorRes int placeholderColor) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.imoji_search_result, this);

        imageView = (ImageView) this.findViewById(R.id.imoji_search_result_image);
        imageView.setImageDrawable(createPlaceholder(placeholderColor));

        textView = (TextView) this.findViewById(R.id.imoji_search_result_category_title);
    }

    private Drawable createPlaceholder(@ColorRes int placeholderColor) {
        int color = getResources().getColor(placeholderColor);
        GradientDrawable placeholder = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ColorUtils.setAlphaComponent(color, GRADIENT_START_ALPHA), ColorUtils.setAlphaComponent(color, GRADIENT_END_ALPHA)});
        placeholder.setShape(GradientDrawable.OVAL);
        return placeholder;
    }

    public void displayResult(Uri url, final String title) {

        final Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.imoji_fade_out);
                startAnimation(fadeIn);

                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (title != null) {
                            displayCategory(bitmap, title);
                        } else {
                            displaySticker(bitmap);
                        }
                        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.imoji_fade_in);
                        startAnimation(fadeOut);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        imageView.setTag(target);

        Picasso.with(getContext()).load(url).into(target);
    }

    private void displaySticker(Bitmap bitmap) {
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageBitmap(bitmap);
    }

    private void displayCategory(Bitmap bitmap, String title) {
        int width = (int) getResources().getDimension(R.dimen.imoji_search_result_small_sticker_width);
        LayoutParams params = new LayoutParams(width, width);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);

        textView.setText(title);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.otf"));
        textView.setVisibility(VISIBLE);
    }
}
