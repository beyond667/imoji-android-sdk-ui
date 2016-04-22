package io.imoji.sdk.widgets.searchwidgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

        firstLeftIcon = this.findViewById(R.id.search_bar_first_left_icon);
        secondLeftIcon = this.findViewById(R.id.search_bar_second_left_icon);
        textBox = (ImojiEditText) this.findViewById(R.id.search_bar_text_box);
        rightIcon = this.findViewById(R.id.search_bar_right_icon);

        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start == 0 && s.length() > 0) {
                    rightIcon.setVisibility(VISIBLE);
                    onStartSearchText();
                } else if (before > 0 && s.length() == 0) {
                    rightIcon.setVisibility(GONE);
                    onDeleteSearchText();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                textBox.setText("");
            }
        });
        init();
    }

    public void init(){

    }

    public void onDeleteSearchText() {

    }

    public void onStartSearchText() {

    }

}
