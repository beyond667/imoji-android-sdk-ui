package io.imoji.sdk.ui.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import io.imoji.sdk.widgets.searchwidgets.ImojiImageView;


public class WidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.widget_main_view);

        ImojiImageView imojiImageView = new ImojiImageView(this, R.color.search_widget_placeholder_1);
        int sideLength = (int) getResources().getDimension(io.imoji.sdk.ui.R.dimen.imoji_image_view_side);
        int verticalMargin = (int) getResources().getDimension(io.imoji.sdk.ui.R.dimen.imoji_image_view_vertical_margin);
        LayoutParams params =  new LayoutParams(sideLength,sideLength);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.setMargins(0,verticalMargin,0,verticalMargin);
        container.addView(imojiImageView, params);
    }

}
