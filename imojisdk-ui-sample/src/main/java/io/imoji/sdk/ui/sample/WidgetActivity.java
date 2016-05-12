package io.imoji.sdk.ui.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.ui.ImojiEditorActivity;
import io.imoji.sdk.ui.utils.EditorBitmapCache;
import io.imoji.sdk.widgets.searchwidgets.ImojiFullScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiHalfScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.ImojiQuarterScreenWidget;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiSearchResultAdapter;
import io.imoji.sdk.widgets.searchwidgets.components.ImojiUISDKOptions;
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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ImojiSearchResultAdapter.ImojiImageLoader imageLoader = new ImojiSearchResultAdapter.ImojiImageLoader() {
            @Override
            public void loadImage(ImageView target, Uri uri, final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                Ion.with(target)
                        .load(uri.toString())
                        .setCallback(new FutureCallback<ImageView>() {
                            @Override
                            public void onCompleted(Exception e, ImageView result) {
                                callback.updateImageView();
                            }
                        });
            }
        };


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ImojiUISDKOptions options = new ImojiUISDKOptions();
        options.setImageFormat(RenderingOptions.ImageFormat.Png);
        options.setDisplayStickerBorders(preferences.getBoolean(getString(R.string.pref_key_sticker_borders_enabled), true));
        options.setIncludeRecentsAndCreate(preferences.getBoolean(getString(R.string.pref_key_recents_create_enabled), true));
        options.setParentActivity(this);

        switch (identifier) {
            case 0:
                ImojiQuarterScreenWidget widget = new ImojiQuarterScreenWidget(this, options, imageLoader);
                setTitle(R.string.activity_title_quarter_screen);
                container.addView(widget, params);
                break;
            case 1:
                ImojiHalfScreenWidget halfWidget = new ImojiHalfScreenWidget(this, options, imageLoader);
                setTitle(R.string.activity_title_half_screen);
                container.addView(halfWidget, params);
                break;
            case 2:
                ImojiFullScreenWidget fullWidget = new ImojiFullScreenWidget(this, options, imageLoader);
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
                    public void onCategoryTapped(Category category) {

                    }

                    @Override
                    public void onStickerTapped(Imoji imoji) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImojiEditorActivity.START_EDITOR_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
            Imoji model = data.getParcelableExtra(ImojiEditorActivity.IMOJI_MODEL_BUNDLE_ARG_KEY);
            if (model != null) {
                Log.d(getClass().getName(), "imoji id: " + model.getIdentifier());
            } else {
                Log.d(getClass().getName(), "we got a token: " + data.getStringExtra(ImojiEditorActivity.CREATE_TOKEN_BUNDLE_ARG_KEY));
            }
            showCreatedImoji(EditorBitmapCache.getInstance().get(EditorBitmapCache.Keys.OUTLINED_BITMAP));
        }
    }

    private void showCreatedImoji(final Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_created_imoji);
        builder.setPositiveButton(R.string.dialog_positive_label_created_imoji, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_outlined_imoji, null);

        ImageView image = (ImageView) dialogLayout.findViewById(R.id.iv_outlined_imoji);
        image.setImageBitmap(bitmap);
        dialog.setView(dialogLayout);
        dialog.show();

    }
}
