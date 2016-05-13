# Imoji UI SDK  

The Imoji UI SDK for Android provides common display elements to simplify integration of Imoji content into your application.

### Prerequisites

You'll need to grab developer keys prior to integration. Sign up for a free developer account at [https://developer.imoji.io](https://developer.imoji.io) to get your keys.

### Setup

Integrating the libraries can be done in multiple fashions:

* [Download the latest](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.imoji.sdk%22%20AND%20a%3A%22imoji-sdk-ui%22) build from Maven Central manually
* Add the following to your Gradle build file:
```
dependencies {
        compile ('io.imoji.sdk:imoji-sdk-ui:+@aar') {
            transitive=true
        }
}
```

### Authentication

Initiate the client id and api token for ImojiSDK whe your application launches:

```java
public class MyApplication extends Application {
 @Override
    public void onCreate() {
        super.onCreate();
        ImojiSDK.getInstance()
                .setCredentials(UUID.fromString("YOUR_CLIENT_ID_HERE"), "YOUR_CLIENT_SECRET_HERE");
    }
}
```

### Integrating Imoji Editor

* Add the ImojiEditorActivity to your `AndroidManifest.xml` file. You may apply any theme you like, but make sure the theme is xxx.NoActionBar. In other words, it does not use the ActionBar. More on theming in the next item.
    ```xml
    <activity
        android:windowSoftInputMode="adjustNothing"
        android:name="io.imoji.sdk.ui.ImojiEditorActivity"
        android:theme="@style/AppTheme.NoActionBar"></activity>
    ```

* Editor theming.
The editor requires that you have  appropriately set `colorPrimary`, `colorPrimaryDark`, and `colorAccent` attributes because the default coloring of the editor will depend on those. Therefore, in your styles.xml where you define your style, make sure that these are set:
    ```xml
       <style name="AppTheme.NoActionBar" parent="Theme.AppCompat.Light.NoActionBar">
            <!-- Customize your theme here. -->
            <item name="colorPrimary">@color/colorPrimary</item>
            <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
            <item name="colorAccent">@color/colorAccent</item>
            <item name="windowActionBar">false</item>
            <item name="windowNoTitle">true</item>
            <item name="imoji__editorToolbarTheme">@style/ThemeOverlay.AppCompat.Dark.ActionBar</item>
        </style>
    ```
    You can also customize the toolbars used in the editor by setting the `imoji__editorToolbarTheme` attribute to whatever style you wish. You can see this in the code snippet above.

Now that you have integrated the SDK, you can use the imoji creator in your app. To create an imoji, follow these steps:

* Create a `Bitmap` of the image you would like to convert into an imoji.

* Put the `Bitmap` into the EditorBitmapCache using the `EditorBitmapCache.Keys.INPUT_BITMAP` cache key.
 ```java
 EditorBitmapCache.getInstance().put(EditorBitmapCache.Keys.INPUT_BITMAP, bitmap);
 ```

* Launch the `ImojiEditorActivity` using the `START_EDITOR_REQUEST_CODE`
 ```java
 Intent intent = new Intent(this, ImojiEditorActivity.class);
 startActivityForResult(intent, ImojiEditorActivity.START_EDITOR_REQUEST_CODE);
 ```
* **Optional:** Skip tagging
    You can optionally skip tagging by setting the intent key `ImojiEditorActivity.TAG_IMOJI_BUNDLE_ARG_KEY` to `false` when starting ImojiEditorActivity.
    Note, however, that the imoji will NOT be searchable, so your users will not be able to find the imoji by searching tags.

* **Optional:** Return Outlined Image Immediately, while returning the Imoji object Asynchronously
    You can optionally receive the outlined imoji bitmap immediately by setting the intent key `ImojiEditorActivity.RETURN_IMMEDIATELY_BUNDLE_ARG_KEY` to `true` when starting ImojiEditorActivity.
    The imoji object will be returned to you in a LocalBroadcast. You will need to register a receiver like such:
    
    ```java
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       
       ...
       
       ImojiCreateReceiver mReceiver = new ImojiCreateReceiver(); 
       LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ImojiIntents.Create.IMOJI_CREATE_INTERNAL_INTENT_ACTION));
    
    }
    ```
    
    Here's a sample receiver:
    
    ```java
    public class ImojiCreateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(ImojiIntents.Create.STATUS_BUNDLE_ARG_KEY, false);
            if (status) { //success?
                Imoji imoji = intent.getParcelableExtra(ImojiIntents.Create.IMOJI_MODEL_BUNDLE_ARG_KEY);
                String token = intent.getStringExtra(ImojiIntents.Create.CREATE_TOKEN_BUNDLE_ARG_KEY);
                Toast.makeText(MainActivity.this, "got imoji: " + imoji.getIdentifier(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "imoji creation failed", Toast.LENGTH_LONG).show();
            }
        }
    }
    ```
 
* Override `onActivityResult` in your Activity. If the Activity result was `RESULT_OK`, you can obtained the outlined imoji bitmap from the `EditorBitmapCache` using the `EditorBitmapCache.Keys.OUTLINED_BITMAP` cache key.
   Note that if you set the `ImojiEditorActivity.RETURN_IMMEDIATELY_BUNDLE_ARG_KEY` to `true` then you will not receive the imoji object but will instead get a token to match with your receiver from step 5. 
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (ImojiEditorActivity.START_EDITOR_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
        Log.d(LOG_TAG, "imoji id: " + model.getIdentifier());
        mOutlinedImoji.setImageBitmap(EditorBitmapCache.getInstance().get(EditorBitmapCache.Keys.OUTLINED_BITMAP));
    }
}
```

# Imoji Search Widgets

Imoji Android SDK UI offers three search views you can use in your apps; quarter, half and full screen widgets. Search widgets handles searching for imojis and categories. You can create one with
```java
 ImojiQuarterScreenWidget widget = new ImojiQuarterScreenWidget(this, new ImojiUISDKOptions(), new ImojiSearchResultAdapter.ImojiImageLoader() {
        @Override
        public void loadImage(ImageView target, Uri uri, ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                        
        }
 });
```
You'll need to pass the context, a new ImojiUISDKOptions object and a new ImojiImageLoader object.

##ImojiUISDKOptions
ImojiUISDKOptions is an optional configuration object for your ImojiSDK integration. If you don't want to change default configurations you can just pass ```new ImojiUISDKOptions()``` as the second parameter of your widget constructor. You can change values inside the object with simple setters.

```java
options.setImageFormat(RenderingOptions.ImageFormat.Png);
//use options.getImageFormat() to get the value
```
Supported configurations

1.**Image Format:** Optional. Default Webp. Image format for assets displayed in the widget.

2.**Include Recents and Create:** Optional. Default True. Enables Recents and Create buttons in widget's searchbar.

3.**Display Sticker Borders:** Optional. Default True. Displays borders around assets displayed in the widget.

## ImojiImageLoader
ImojiImageLoader is a simple interface with a single ```loadImage``` method that lets you use your choice of image library to load all assets in the widget. First parameter ```target``` is the ImageView in which your assets should be loaded. It also supports Gifs. Second parameter ```uri``` is the resource identifier for the asset to be loaded. Final parameter ```callback``` is a callback that handles ui changes once image is loaded. As soon as you are done with loading the image, you should call ```callback.updateImageView();```

**It is recommended to use an image loading library with Gif support.**

###Glide Example
```java
 ImojiFullScreenWidget fullWidget = new ImojiFullScreenWidget(this, 
                new ImojiUISDKOptions(), 
                new ImojiSearchResultAdapter.ImojiImageLoader() {
        @Override
        public void loadImage(ImageView target, Uri uri,
                                final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                Glide.with(context)
                        .load(uri)
                        .listener(new RequestListener<Uri, GlideDrawable>() {
                                @Override
                                 public boolean onException(Exception e, 
                                                Uri model, Target<GlideDrawable> target, 
                                                boolean isFirstResource) {
                                        return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, 
                                                Uri model, Target<GlideDrawable> target, 
                                                boolean isFromMemoryCache, boolean isFirstResource) {
                                        callback.updateImageView();
                                        return false;
                                }})
                        .into(target);
                    }
                });
```

###Ion Example
```java
ImojiQuarterScreenWidget widget = new ImojiQuarterScreenWidget(this, 
                new ImojiUISDKOptions(), 
                new ImojiSearchResultAdapter.ImojiImageLoader() {

        @Override
        public void loadImage(ImageView target, Uri uri,
                                final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                Ion.with(target)
                        .load(uri.toString())
                        .setCallback(new FutureCallback<ImageView>() {
                                @Override
                                public void onCompleted(Exception e, ImageView result) {
                                        callback.updateImageView();
                                }
                        });
        }
});
```

###Picasso Example
```java
ImojiHalfScreenWidget halfWidget = new ImojiHalfScreenWidget(this, 
        RenderingOptions.ImageFormat.Png, 
        new ImojiSearchResultAdapter.ImojiImageLoader() {
                @Override
                public void loadImage(ImageView target, Uri uri,
                                        final ImojiSearchResultAdapter.ImojiImageLoadCompleteCallback callback) {
                Picasso.with(context)
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
```

##Imoji Widget Listener
Once you created a search widget you can set an Imoji Widget Listener on it to listen for events.
```java
 widget.setWidgetListener(new ImojiWidgetListener() {
                    @Override
                    public void onCloseButtonTapped() {
                        //Define custom behavior for Full Screen Widget Close Button
                    }

                    @Override
                    public void onStickerTapped(Imoji imoji) {
                        //Get tapped sticker's ImojiSDK Imoji object, from which you can create URL's for different
                        //sizes, image formats and styles
                    }
                });
```

