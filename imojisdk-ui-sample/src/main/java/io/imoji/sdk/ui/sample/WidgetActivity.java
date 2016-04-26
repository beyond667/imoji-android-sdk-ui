package io.imoji.sdk.ui.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.imoji.sdk.widgets.searchwidgets.ImojiFullScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiHalfScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiQuarterScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;


public class WidgetActivity extends AppCompatActivity {

    public static final String WIDGET_IDENTIFIER = "widget_identifier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.widget_main_view);

        int identifier = getIntent().getIntExtra(WIDGET_IDENTIFIER,1);
        ImojiBaseSearchWidget widget = new ImojiQuarterScreenWidget(this);
        switch (identifier){
            case 1:
               widget = new ImojiQuarterScreenWidget(this);
                break;
            case 2:
                widget = new ImojiHalfScreenWidget(this);
                break;
            case 3:
                widget = new ImojiFullScreenWidget(this);
                break;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        container.addView(widget, params);

        widget.setWidgetListener(new ImojiBaseSearchWidget.ImojiWidgetListener() {
            @Override
            public void onBackButtonTapped() {
                Toast.makeText(getApplicationContext(), "BACK BUTTON TAPPED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCloseButtonTapped() {
                Toast.makeText(getApplicationContext(), "CLOSE BUTTON TAPPED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCategoryTapped() {
                Toast.makeText(getApplicationContext(), "CATEGORY TAPPED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStickerTapped(Uri uri) {
                Toast.makeText(getApplicationContext(), "STICKER TAPPED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTermSearched(String term) {
                Toast.makeText(getApplicationContext(), "TERM SEARCHED: " + term, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
