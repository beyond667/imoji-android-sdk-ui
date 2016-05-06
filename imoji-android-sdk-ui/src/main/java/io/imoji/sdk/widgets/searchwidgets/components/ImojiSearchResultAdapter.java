package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.net.Uri;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiResultView;

/**
 * Created by engind on 4/22/16.
 */
public class ImojiSearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int DIVIDER_VIEW_TYPE = 2343234;

    private List<SearchResult> results;
    private Context context;
    private ImojiSearchTapListener tapListener;
    private ImojiImageLoader imageLoader;

    private int placeholderRandomizer;
    private
    @ImojiResultView.ResultViewSize
    int resultViewSize;
    private int dividerPosition = -1;
    private int orientation;


    public ImojiSearchResultAdapter(Context context, ImojiImageLoader imageLoader, @ImojiResultView.ResultViewSize int resultViewSize,int orientation) {
        results = new ArrayList<>();
        this.context = context;
        this.imageLoader = imageLoader;

        int[] colorArray = context.getResources().getIntArray(R.array.search_widget_placeholder_colors);
        this.placeholderRandomizer = new Random().nextInt(colorArray.length);

        this.resultViewSize = resultViewSize;
        this.orientation = orientation;
    }

    public void repopulate(List<SearchResult> newResults, int dividerPosition) {
        int size = this.results.size();
        if (size > 0) {
            results.clear();
            this.notifyItemRangeRemoved(0, size);
        }
        this.dividerPosition = dividerPosition;
        results.addAll(newResults);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DIVIDER_VIEW_TYPE) {
            return new DividerHolder(new View(context));
        }
        return new ResultHolder(new ImojiResultView(context, resultViewSize));
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position != dividerPosition) {
            final SearchResult sr = results.get(position);
            final ImojiResultView resultView = (ImojiResultView) holder.itemView;
            resultView.setListener(tapListener, results.get(holder.getAdapterPosition()));
            resultView.resetView(placeholderRandomizer, position);


            imageLoader.loadImage(resultView.getImageView(), sr.getThumbnailUri(), new ImojiImageLoadCompleteCallback() {
                @Override
                public void updateImageView() {
                    if (sr.isCategory()) {
                        resultView.loadCategory(sr.getTitle());
                    } else {
                        resultView.loadSticker();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dividerPosition) {
            return DIVIDER_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }


    public class ResultHolder extends RecyclerView.ViewHolder{

        public ResultHolder(View itemView) {
            super(itemView);
        }
    }

    public class DividerHolder extends RecyclerView.ViewHolder {

        public DividerHolder(View itemView) {
            super(itemView);
            itemView.setBackgroundColor(ColorUtils.setAlphaComponent(
                    //TODO // FIXME: 5/3/16
                    context.getResources().getColor(R.color.search_widget_category_divider), 18));

            int width = (int) context.getResources().getDimension(R.dimen.imoji_search_recycler_divider_width);
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    orientation == LinearLayout.HORIZONTAL ? width : ViewGroup.LayoutParams.MATCH_PARENT,
                    orientation == LinearLayout.HORIZONTAL ? ViewGroup.LayoutParams.MATCH_PARENT : width);
            if(orientation == LinearLayout.HORIZONTAL){
                int marg = (int) context.getResources().getDimension(R.dimen.imoji_search_horizontal_recycler_divider_padding);
                layoutParams.setMargins(0,marg,0,marg);
            }else{
                int marg = (int) context.getResources().getDimension(R.dimen.imoji_search_vertical_recycler_divider_padding);
                layoutParams.setMargins(marg,0,marg,0);
            }
            itemView.setLayoutParams(layoutParams);
        }
    }

    public void setSearchTapListener(ImojiSearchTapListener tapListener) {
        this.tapListener = tapListener;
    }

    public int getDividerPosition() {
        return dividerPosition;
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
