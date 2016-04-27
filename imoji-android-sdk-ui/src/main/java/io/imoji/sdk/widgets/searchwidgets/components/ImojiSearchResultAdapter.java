package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.objects.RenderingOptions.ImageFormat;
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

    public ImojiSearchResultAdapter(Context context) {
        results = new ArrayList<>();
        this.context = context;

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
        final SearchResult result = results.get(position);
        resetView(holder.imageView, holder.textView);
        Picasso.with(context)
                .load(result.getThumbnailUri())
                .placeholder(getPlaceholder(position))
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (result.isCategory()) {
                            loadCategory(holder.imageView, holder.textView, result.getTitle());
                        } else {
                            loadSticker(holder.imageView);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class SearchResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;

        public SearchResultHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imoji_search_result_image);
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

    public static class SearchResult {

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

        public Uri getThumbnailUri(){
            Imoji thumbailImoji = this.imoji;
            if(isCategory()){
                thumbailImoji = category.getPreviewImoji();
            }
            return thumbailImoji.getStandardThumbnailUri();
        }

        public String getTitle() {
            String title = null;
            if(isCategory()){
                title = category.getTitle();
            }
            return title;
        }

        public boolean isCategory() {
            return category != null && imoji == null;
        }

        public Uri getUri(ImageFormat imageFormat){
            return getImoji().urlForRenderingOption(new RenderingOptions(
                    RenderingOptions.BorderStyle.Sticker,
                    imageFormat,
                    RenderingOptions.Size.Thumbnail));
        }
    }

    public void resetView(ImageView imageView, TextView textView) {
        textView.setVisibility(View.GONE);

        int width = (int) context.getResources().getDimension(R.dimen.imoji_search_result_width);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);

        int padding = (int) context.getResources()
                .getDimension(R.dimen.imoji_search_result_placeholder_padding);
        imageView.setPadding(padding, padding, padding, padding);
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

    private void loadCategory(ImageView imageView, TextView textView, String title) {
        imageView.setPadding(0, 0, 0, 0);
        int width = (int) context.getResources().getDimension(R.dimen.imoji_search_result_small_sticker_width);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        int topMargin = (int) context.getResources().getDimension(R.dimen.imoji_search_result_small_sticker_top_margin);
        params.setMargins(0, topMargin, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(params);

        textView.setText(title);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"));
        textView.setVisibility(View.VISIBLE);
    }

    private void loadSticker(ImageView imageView) {
        imageView.setPadding(0, 0, 0, 0);
    }
}
