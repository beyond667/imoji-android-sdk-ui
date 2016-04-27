package io.imoji.sdk.ui.sample;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import io.imoji.sdk.widgets.searchwidgets.ImojiFullScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiHalfScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiQuarterScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiBaseSearchWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiWidgetListener;


public class WidgetActivity extends AppCompatActivity {

    public static final String WIDGET_IDENTIFIER = "widget_identifier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(R.color.widget_activity_action_bar_color)
        ));
        setContentView(R.layout.activity_widget);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.widget_main_view);

        int identifier = getIntent().getIntExtra(WIDGET_IDENTIFIER, 1);
        ImojiBaseSearchWidget widget = new ImojiQuarterScreenWidget(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        switch (identifier) {
            case 1:
                widget = new ImojiQuarterScreenWidget(this);
                setTitle(R.string.activity_title_quarter_screen);
                container.addView(widget, params);
                break;
            case 2:
                widget = new ImojiHalfScreenWidget(this);
                setTitle(R.string.activity_title_half_screen);
                container.addView(widget, params);
                break;
            case 3:
                widget = new ImojiFullScreenWidget(this);
                getSupportActionBar().hide();
                container.addView(widget);
                widget.setWidgetListener(new ImojiWidgetListener() {
                    @Override
                    public void onBackButtonTapped() {
                    }

                    @Override
                    public void onCloseButtonTapped() {
                        NavUtils.navigateUpFromSameTask(WidgetActivity.this);
                    }

                    @Override
                    public void onCategoryTapped() {

                    }

                    @Override
                    public void onStickerTapped(Uri uri) {

                    }

                    @Override
                    public void onTermSearched(String term) {

                    }
                });
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
