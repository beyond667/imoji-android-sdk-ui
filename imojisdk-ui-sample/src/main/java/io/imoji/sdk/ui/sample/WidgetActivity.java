package io.imoji.sdk.ui.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.response.ImojisResponse;
import io.imoji.sdk.widgets.searchwidgets.ImojiSearchResultLayout;


public class WidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.widget_main_view);

        final ImojiSearchResultLayout layout = new ImojiSearchResultLayout(this, R.color.search_widget_placeholder_1);
        int width = (int) getResources().getDimension(io.imoji.sdk.ui.R.dimen.imoji_search_result_width);
        int height = (int) getResources().getDimension(io.imoji.sdk.ui.R.dimen.imoji_search_result_height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(layout, params);

        ImojiSDK.getInstance()
                .createSession(getApplicationContext())
                .getFeaturedImojis()
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        layout.displayResult(imojisResponse.getImojis().get(0).getStandardThumbnailUri(false),null);
                    }
                });

//        ImojiSearchBarLayout layout = new ImojiSearchBarLayout(this);
//        int height = (int) getResources().getDimension(io.imoji.sdk.ui.R.dimen.imoji_search_bar_height);
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        container.addView(layout, params);
//
//        ImojiSearchBarLayout layout2 = new ImojiSearchBarLayout(this);
//        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, height);
//        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        container.addView(layout2, params2);

    }

}
