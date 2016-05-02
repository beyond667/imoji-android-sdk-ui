package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 5/2/16.
 */
public class ImojiResultView extends RelativeLayout {

    private final static int GRADIENT_START_ALPHA = 0;
    private final static int GRADIENT_END_ALPHA = 16;
    private final FrameLayout container;
    private final GifImageView imageView;
    private final ImageView placeholder;
    private final TextView textView;

    private Context context;

    public ImojiResultView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.imoji_search_result, this, true);
        container = (FrameLayout) findViewById(R.id.imoji_search_result_container);
        imageView = (GifImageView) findViewById(R.id.imoji_search_result_image);
        placeholder = (ImageView) findViewById(R.id.imoji_search_result_placeholder);
        textView = (TextView) findViewById(R.id.imoji_search_result_category_title);
    }

    public void resetView(int placeholderRandomizer, int position) {
        textView.setVisibility(View.GONE);

        int width = (int) context.getResources().getDimension(R.dimen.imoji_result_width_small);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.setLayoutParams(params);

        placeholder.setImageDrawable(getPlaceholder(placeholderRandomizer,position));
        placeholder.setVisibility(View.INVISIBLE);

        Animation appearAnimation = getAppearAnimation();
        appearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                placeholder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        placeholder.startAnimation(appearAnimation);
    }

    private Drawable getPlaceholder(int placeholderRandomizer, int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.search_widget_placeholder_colors);
        int color = colorArray[(placeholderRandomizer + position) % colorArray.length];

        GradientDrawable placeholder = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ColorUtils.setAlphaComponent(Color.WHITE, GRADIENT_START_ALPHA),
                        ColorUtils.setAlphaComponent(Color.WHITE, GRADIENT_END_ALPHA)});
        placeholder.setColor(color);
        placeholder.setShape(GradientDrawable.OVAL);
        return placeholder;
    }

    public void loadCategory(String title) {
        int width = (int) context.getResources().getDimension(R.dimen.imoji_result_category_image_side_small);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        int margin = (int) context.getResources().getDimension(R.dimen.imoji_result_category_image_top_margin_small);
        params.setMargins(0, margin, 0, margin);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        container.setLayoutParams(params);

        textView.setText(title);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"));
        textView.setVisibility(View.VISIBLE);

        loadSticker();
    }

    public void loadSticker() {

        Animation disappearAnimation = getDisappearAnimation();
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                placeholder.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        placeholder.startAnimation(disappearAnimation);

        Animation appearAnimation = getAppearAnimation();
        appearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(appearAnimation);
    }


    public GifImageView getImageView() {
        return imageView;
    }

    private Animation getAppearAnimation() {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.search_widget_result_fade_in);
        fadeInAnimation.setInterpolator(PathInterpolatorCompat.create(0.3f, 0.14f, 0.36f, 1.36f));
        return fadeInAnimation;
    }

    private Animation getDisappearAnimation() {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.search_widget_result_fade_out);
        fadeOutAnimation.setInterpolator(PathInterpolatorCompat.create(0.25f, 0.1f, 0.25f, 1));
        return fadeOutAnimation;
    }
}
