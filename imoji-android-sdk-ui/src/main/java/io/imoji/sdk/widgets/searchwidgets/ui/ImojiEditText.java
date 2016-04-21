package io.imoji.sdk.widgets.searchwidgets.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/21/16.
 */
public class ImojiEditText extends EditText {

    public ImojiEditText(Context context) {
        super(context);
        init();
    }

    public ImojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.otf"));
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setHintTextColor(focused ? getResources().getColor(R.color.search_widget_search_bar_focused_hint_color) :
                getResources().getColor(R.color.search_widget_search_bar_unfocused_text_color));
        setTextColor(focused ? getResources().getColor(R.color.search_widget_search_bar_focused_text_color) :
                getResources().getColor(R.color.search_widget_search_bar_unfocused_text_color));
    }
}
