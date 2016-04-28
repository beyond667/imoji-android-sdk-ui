package io.imoji.sdk.ui.sample;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.widgets.searchwidgets.ImojiFullScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiHalfScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiQuarterScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiWidgetListener;


public class WidgetActivity extends AppCompatActivity {

    public static final String WIDGET_IDENTIFIER = "widget_identifier";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(R.color.widget_activity_action_bar_color)
        ));
        setContentView(R.layout.activity_widget);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.widget_main_view);

        int identifier = getIntent().getIntExtra(WIDGET_IDENTIFIER, 1);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        switch (identifier) {
            case 1:
                ImojiQuarterScreenWidget widget = new ImojiQuarterScreenWidget(this, RenderingOptions.ImageFormat.WebP, new ImojiSearchResultAdapter.ImojiImageLoader() {

                    @Override
                    public void loadImage(ImageView target, Drawable placeholder, @AnimRes int animationId, Uri uri,
                                          final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                        Ion.with(target)
                                .placeholder(placeholder)
                                .animateIn(animationId)
                                .load(uri.toString())
                                .setCallback(new FutureCallback<ImageView>() {
                                    @Override
                                    public void onCompleted(Exception e, ImageView result) {
                                        callback.updateImageView();
                                    }
                                });
                    }
                });
                setTitle(R.string.activity_title_quarter_screen);
                container.addView(widget, params);
                break;
            case 2:
                ImojiHalfScreenWidget halfWidget = new ImojiHalfScreenWidget(this, RenderingOptions.ImageFormat.Png, new ImojiSearchResultAdapter.ImojiImageLoader() {
                    @Override
                    public void loadImage(ImageView target, Drawable placeholder, @AnimRes int animationId, Uri uri,
                                          final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                        Picasso.with(context)
                                .load(uri)
                                .placeholder(placeholder)
                                .into(target, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        callback.updateImageView();
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                    }
                });
                setTitle(R.string.activity_title_half_screen);
                container.addView(halfWidget, params);
                break;
            case 3:
                ImojiFullScreenWidget fullWidget = new ImojiFullScreenWidget(this, RenderingOptions.ImageFormat.WebP, new ImojiSearchResultAdapter.ImojiImageLoader() {
                    @Override
                    public void loadImage(ImageView target, Drawable placeholder, @AnimRes int animationId, Uri uri,
                                          final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                        Ion.with(target)
                                .placeholder(placeholder)
                                .animateIn(animationId)
                                .load(uri.toString())
                                .setCallback(new FutureCallback<ImageView>() {
                                    @Override
                                    public void onCompleted(Exception e, ImageView result) {
                                        callback.updateImageView();
                                    }
                                });
                    }
                });
                getSupportActionBar().hide();
                container.addView(fullWidget);
                fullWidget.setWidgetListener(new ImojiWidgetListener() {
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
