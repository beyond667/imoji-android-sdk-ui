package io.imoji.sdk.ui.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.ui.ImojiEditorActivity;
import io.imoji.sdk.ui.utils.EditorBitmapCache;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ListView labelsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        labelsListView = (ListView) findViewById(R.id.example_list);


        String[] labels = getResources().getStringArray(R.array.example_labels);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, labels);
        labelsListView.setAdapter(adapter);


        labelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                    case 0:
                        startImojiEditorActivity();
                        break;
                    case 1:
                    case 2:
                    case 3:
                        Intent intent = new Intent(MainActivity.this,WidgetActivity.class);
                        intent.putExtra("title",String.valueOf(id));
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImojiEditorActivity.START_EDITOR_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
            Imoji model = data.getParcelableExtra(ImojiEditorActivity.IMOJI_MODEL_BUNDLE_ARG_KEY);
            if (model != null) {
                Log.d(LOG_TAG, "imoji id: " + model.getIdentifier());
            } else {
                Log.d(LOG_TAG, "we got a token: " +data.getStringExtra(ImojiEditorActivity.CREATE_TOKEN_BUNDLE_ARG_KEY));
            }
            showCreatedImoji(EditorBitmapCache.getInstance().get(EditorBitmapCache.Keys.OUTLINED_BITMAP));
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

    private void showCreatedImoji(final Bitmap bitmap){
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
