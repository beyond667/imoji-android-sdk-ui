package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiEditText;

/**
 * Created by engind on 4/21/16.
 */
public class ImojiSearchBarLayout extends RelativeLayout {

    private View firstLeftIcon;
    private View secondLeftIcon;
    private View rightIcon;
    private ImojiEditText textBox;


    public ImojiSearchBarLayout(Context context) {
        super(context);
        setVerticalGravity(Gravity.CENTER_VERTICAL);
        //so that textBox doesn't grab focus automatically
        setFocusableInTouchMode(true);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.imoji_search_bar, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        firstLeftIcon = this.findViewById(R.id.search_bar_first_left_icon);
        secondLeftIcon = this.findViewById(R.id.search_bar_second_left_icon);
        textBox = (ImojiEditText) this.findViewById(R.id.search_bar_text_box);
        rightIcon = this.findViewById(R.id.search_bar_right_icon);
    }
}
