package io.imoji.sdk.ui.sample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.ui.ImojiEditorActivity;
import io.imoji.sdk.ui.utils.EditorBitmapCache;

public class MainActivity extends AppCompatActivity {

    private ListView sectionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] labels = getResources().getStringArray(R.array.example_labels);

        sectionsList = (ListView) findViewById(R.id.main_activity_list);
        sectionsList.setHorizontalScrollBarEnabled(false);
        sectionsList.setVerticalScrollBarEnabled(false);
        sectionsList.setDivider(null);
        sectionsList.setAdapter(new TitleArrayAdapter<>(this, labels));

        sectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case 0:
                    case 1:
                    case 2:
                        Intent intent = new Intent(MainActivity.this, WidgetActivity.class);
                        intent.putExtra(WidgetActivity.WIDGET_IDENTIFIER, (int) id);
                        startActivity(intent);
                        break;
                    case 3:
                        startImojiEditorActivity();
                        break;
                }
            }
        });
    }

    private class TitleArrayAdapter<String> extends ArrayAdapter<String> {

        private String[] title;
        private Context context;

        public TitleArrayAdapter(Context context, String[] values) {
            super(context, R.layout.main_activity_list_item, values);
            this.title = values;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(R.layout.main_activity_list_item, parent, false);
            View icon = itemView.findViewById(R.id.main_activity_list_item_icon);

            if (position == 4) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) icon.getLayoutParams();
                int side = (int) getResources().getDimension(R.dimen.main_activity_list_item_icon_expanded_side);
                params.width = side;
                params.height = side;
                params.setMargins(0, 0, 0, 0);
                icon.setLayoutParams(params);
                icon.setBackground(getDrawable(R.drawable.imoji_menu_settings));
            }

            TextView titleView = (TextView) itemView.findViewById(R.id.main_activity_list_item_title);
            titleView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.otf"));
            titleView.setText(title[position].toString());

            return itemView;
        }
    }


    private void startImojiEditorActivity() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample, options);
        EditorBitmapCache.getInstance().put(EditorBitmapCache.Keys.INPUT_BITMAP, bitmap);
        Intent intent = new Intent(this, ImojiEditorActivity.class);
        intent.putExtra(ImojiEditorActivity.RETURN_IMMEDIATELY_BUNDLE_ARG_KEY, false);
        intent.putExtra(ImojiEditorActivity.TAG_IMOJI_BUNDLE_ARG_KEY, true);
        startActivityForResult(intent, ImojiEditorActivity.START_EDITOR_REQUEST_CODE);
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

//    public class ImojiCreateReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            boolean status = intent.getBooleanExtra(ImojiIntents.Create.STATUS_BUNDLE_ARG_KEY, false);
//            if (status) { //success?
//                Imoji imoji = intent.getParcelableExtra(ImojiIntents.Create.IMOJI_MODEL_BUNDLE_ARG_KEY);
//                String token = intent.getStringExtra(ImojiIntents.Create.CREATE_TOKEN_BUNDLE_ARG_KEY);
//                Toast.makeText(MainActivity.this, "got imoji: " + imoji.getIdentifier(), Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(MainActivity.this, "imoji creation failed", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
