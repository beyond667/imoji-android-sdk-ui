package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 5/2/16.
 */
public class ImojiResultView extends RelativeLayout {

    @IntDef({LARGE, SMALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResultViewSize {}

    public static final int LARGE = 0;
    public static final int SMALL = 1;

    private final static int GRADIENT_START_ALPHA = 0;
    private final static int GRADIENT_END_ALPHA = 16;
    private final RelativeLayout container;
    private final GifImageView imageView;
    private final ImageView placeholder;
    private final TextView textView;

    private Context context;
    private @ResultViewSize int viewSize;

    public ImojiResultView(Context context, @ResultViewSize int viewSize) {
        super(context);
        this.context = context;

        this.viewSize = viewSize;

        int resultWidth = getDimension(0);
        int resultHeight = getDimension(1);
        setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(resultWidth, resultHeight));

        placeholder = new ImageView(context);
        int placeholderSide = getDimension(4);
        RelativeLayout.LayoutParams placeholderParams = new LayoutParams(placeholderSide, placeholderSide);
        placeholderParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        placeholder.setLayoutParams(placeholderParams);
        addView(placeholder);

        container = new RelativeLayout(context);
        addView(container, new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        imageView = new GifImageView(context);
        RelativeLayout.LayoutParams imageParams = new LayoutParams(resultWidth, resultWidth);
        imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(imageParams);
        container.addView(imageView);


        textView = new TextView(context);
        int titleHeight = getDimension(2);
        RelativeLayout.LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(titleParams);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(3));
        //TODO // FIXME: 5/2/16
        textView.setTextColor(getResources().getColor(R.color.search_result_category_title));
        textView.setGravity(Gravity.CENTER);
        textView.setVisibility(GONE);
        container.addView(textView);
    }

    public void resetView(final int placeholderRandomizer, final int position) {
        placeholder.setImageDrawable(getPlaceholder(placeholderRandomizer, position));
        placeholder.startAnimation(getAppearAnimation());
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
        int width = getDimension(5);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        int margin = getDimension(6);
        params.setMargins(0, margin, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(params);

        textView.setText(title);
        textView.setVisibility(VISIBLE);

        startResultAnimation();
    }

    public void loadSticker(){
        int width = getDimension(0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);

        textView.setVisibility(GONE);
        startResultAnimation();
    }

    private void startResultAnimation() {

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
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        container.startAnimation(appearAnimation);
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

    private int getDimension(int position){
        Resources res = getResources();
        TypedArray dimensions = res.obtainTypedArray(viewSize == LARGE ?
                R.array.search_result_large_dimens : R.array.search_result_small_dimens);
        int dimension = (int) dimensions.getDimension(position,0f);
        dimensions.recycle();
        return  dimension;
    }
}
