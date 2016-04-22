package io.imoji.sdk.widgets.searchwidgets.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiImageView extends ImageView {

    private final static int GRADIENT_START_ALPHA = 0;
    private final static int GRADIENT_END_ALPHA = 100;

    public ImojiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void displayResult(final ImojiSearchResultAdapter.SearchResult searchResult, final ResultDisplayedCallback callback) {

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
                        setPadding(0, 0, 0, 0);
                        setImageBitmap(bitmap);
                        if(searchResult.isCategory()){
                            int width = (int) getResources().getDimension(R.dimen.imoji_search_result_small_sticker_width);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            setLayoutParams(params);
                        }
                        callback.onResultDisplayed(searchResult);
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
        setTag(target);

        Picasso.with(getContext()).load(searchResult.getUri()).into(target);
    }


    public void setPlaceholder(int placeholderColor) {
        GradientDrawable placeholder = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ColorUtils.setAlphaComponent(placeholderColor, GRADIENT_START_ALPHA),
                        ColorUtils.setAlphaComponent(placeholderColor, GRADIENT_END_ALPHA)});
        placeholder.setShape(GradientDrawable.OVAL);
        setImageDrawable(placeholder);
    }

    public interface ResultDisplayedCallback{

        void onResultDisplayed(ImojiSearchResultAdapter.SearchResult result);
    }
}
