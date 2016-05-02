package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiSearchResultAdapter extends RecyclerView.Adapter<ImojiSearchResultAdapter.SearchResultHolder> {

    private final static int GRADIENT_START_ALPHA = 0;
    private final static int GRADIENT_END_ALPHA = 16;

    private List<SearchResult> results;
    private Context context;
    private int placeholderRandomizer;
    private ImojiSearchTapListener tapListener;
    private ImojiImageLoader imageLoader;

    public ImojiSearchResultAdapter(Context context, ImojiImageLoader imageLoader) {
        results = new ArrayList<>();
        this.context = context;
        this.imageLoader = imageLoader;

        int[] colorArray = context.getResources().getIntArray(R.array.search_widget_placeholder_colors);
        this.placeholderRandomizer = new Random().nextInt(colorArray.length);
    }

    public void repopulate(List<SearchResult> newResults) {
        int size = this.results.size();
        if (size > 0) {
            results.clear();
            this.notifyItemRangeRemoved(0, size);
        }
        results.addAll(newResults);
        notifyDataSetChanged();
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.imoji_search_result, parent, false);

        return new SearchResultHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final SearchResultHolder holder, int position) {
        final SearchResult sr = results.get(position);
        resetView(holder.container, holder.placeholder, holder.textView, position);

        imageLoader.loadImage(holder.imageView, sr.getThumbnailUri(), new ImojiImageLoadCompleteCallback() {
            @Override
            public void updateImageView() {
                if (sr.isCategory()) {
                    loadCategory(holder.container, holder.imageView, holder.placeholder, holder.textView, sr.getTitle());
                } else {
                    loadSticker(holder.imageView, holder.placeholder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class SearchResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private FrameLayout container;
        private GifImageView imageView;
        private ImageView placeholder;
        private TextView textView;

        public SearchResultHolder(View itemView) {
            super(itemView);
            container = (FrameLayout) itemView.findViewById(R.id.imoji_search_result_container);
            imageView = (GifImageView) itemView.findViewById(R.id.imoji_search_result_image);
            placeholder = (ImageView) itemView.findViewById(R.id.imoji_search_result_placeholder);
            textView = (TextView) itemView.findViewById(R.id.imoji_search_result_category_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (tapListener != null) {
                tapListener.onTap(results.get(getAdapterPosition()));
            }
        }
    }

    public void setSearchTapListener(ImojiSearchTapListener tapListener) {
        this.tapListener = tapListener;
    }

    public interface ImojiSearchTapListener {
        void onTap(SearchResult searchResult);
    }

    public void resetView(FrameLayout container, final ImageView placeholder, TextView textView, int position) {
        textView.setVisibility(View.GONE);

        int width = (int) context.getResources().getDimension(R.dimen.imoji_search_result_width);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.setLayoutParams(params);

        placeholder.setImageDrawable(getPlaceholder(position));
        placeholder.setVisibility(View.INVISIBLE);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.search_widget_result_fade_in);
        fadeInAnimation.setInterpolator(PathInterpolatorCompat.create(0.3f, 0.14f, 0.36f, 1.36f));
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        placeholder.startAnimation(fadeInAnimation);
    }

    private Drawable getPlaceholder(int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.search_widget_placeholder_colors);
        int color = colorArray[(placeholderRandomizer + position) % colorArray.length];

        GradientDrawable placeholder = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ColorUtils.setAlphaComponent(Color.WHITE, GRADIENT_START_ALPHA),
                        ColorUtils.setAlphaComponent(Color.WHITE, GRADIENT_END_ALPHA)});
        placeholder.setColor(color);
        placeholder.setShape(GradientDrawable.OVAL);
        return placeholder;
    }

    private void loadCategory(FrameLayout container, ImageView imageView, ImageView placeholder, TextView textView, String title) {
        int width = (int) context.getResources().getDimension(R.dimen.imoji_search_result_small_sticker_width);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        int margin = (int) context.getResources().getDimension(R.dimen.imoji_search_result_small_sticker_top_margin);
        params.setMargins(0, margin, 0, margin);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        container.setLayoutParams(params);

        textView.setText(title);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"));
        textView.setVisibility(View.VISIBLE);

        loadSticker(imageView, placeholder);
    }

    private void loadSticker(final ImageView imageView, final ImageView placeholder) {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.search_widget_result_fade_out);
        fadeOutAnimation.setInterpolator(PathInterpolatorCompat.create(0.25f, 0.1f, 0.25f, 1));
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        placeholder.startAnimation(fadeOutAnimation);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.search_widget_result_fade_in);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
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
        fadeInAnimation.setInterpolator(PathInterpolatorCompat.create(0.3f, 0.14f, 0.36f, 1.36f));
        imageView.startAnimation(fadeInAnimation);
    }

    public interface ImojiImageLoader {

        void loadImage(ImageView target, Uri uri, ImojiImageLoadCompleteCallback callback);
    }

    public abstract class ImojiImageLoadCompleteCallback {

        public abstract void updateImageView();
    }


}
