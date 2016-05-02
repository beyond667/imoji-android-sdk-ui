package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiSearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultHolder(new ImojiResultView(context));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SearchResult sr = results.get(position);
        final ImojiResultView resultView = (ImojiResultView) holder.itemView;
        resultView.resetView(placeholderRandomizer,position);

        imageLoader.loadImage(resultView.getImageView(), sr.getThumbnailUri(), new ImojiImageLoadCompleteCallback() {
            @Override
            public void updateImageView() {
                if (sr.isCategory()) {
                    resultView.loadCategory(sr.getTitle());
                }else{
                    resultView.loadSticker();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ResultHolder(View itemView) {
            super(itemView);
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


    public interface ImojiImageLoader {

        void loadImage(ImageView target, Uri uri, ImojiImageLoadCompleteCallback callback);
    }

    public abstract class ImojiImageLoadCompleteCallback {

        public abstract void updateImageView();
    }
}
