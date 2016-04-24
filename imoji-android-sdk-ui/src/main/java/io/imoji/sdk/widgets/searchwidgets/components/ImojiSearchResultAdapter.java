package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiImageView;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiSearchResultAdapter extends RecyclerView.Adapter<ImojiSearchResultAdapter.SearchResultHolder> {

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
        results.clear();
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
        SearchResult result = results.get(position);
        holder.imageView.setPlaceholder(pickPlaceholderColor(position));
        holder.imageView.displayResult(result, new ImojiImageView.ResultDisplayedCallback() {
            @Override
            public void onResultDisplayed(SearchResult result) {
                if(result.isCategory()){
                    holder.textView.setText(result.getTitle());
                    holder.textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"));
                    holder.textView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private int pickPlaceholderColor(int position) {
        int[] colorArray = context.getResources().getIntArray(R.array.search_widget_placeholder_colors);
        return colorArray[(placeholderRandomizer + position) % colorArray.length];
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class SearchResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImojiImageView imageView;
        private TextView textView;

        public SearchResultHolder(View itemView) {
            super(itemView);
            imageView = (ImojiImageView) itemView.findViewById(R.id.imoji_search_result_image);
            textView = (TextView) itemView.findViewById(R.id.imoji_search_result_category_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(tapListener != null){
                tapListener.onTap(results.get(getAdapterPosition()));
            }
        }
    }

    public void setSearchTapListener(ImojiSearchTapListener tapListener){
        this.tapListener = tapListener;
    }

    public interface ImojiSearchTapListener{
        void onTap(SearchResult searchResult);
    }

    public static class SearchResult {

        private Uri uri;
        private String title;

        public SearchResult(Uri uri, String title) {
            this.uri = uri;
            this.title = title;
        }

        public SearchResult(Uri uri) {
            this.uri = uri;
        }

        public Uri getUri() {
            return uri;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCategory() {
            return title != null;
        }
    }
}
