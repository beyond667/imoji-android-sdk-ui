package io.imoji.sdk.widgets.searchwidgets.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.imoji.sdk.ui.R;
import io.imoji.sdk.widgets.searchwidgets.ui.ImojiEditText;

/**
 * Created by engind on 4/21/16.
 */
public class ImojiSearchBarLayout extends RelativeLayout {

    private View firstLeftIcon;
    private View rightIcon;
    private ImojiEditText textBox;

    private ImojiSearchBarListener imojiSearchBarListener;

    public ImojiSearchBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.imoji_search_bar, this);

        firstLeftIcon = this.findViewById(R.id.search_bar_first_left_icon);
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

        textBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE ||
                        (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (imojiSearchBarListener != null) {
                        imojiSearchBarListener.onTextSubmit(textBox.getText().toString());
                    }
                    textBox.clearFocus();
                }
                return true;
            }
        });

        rightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                textBox.setText("");
                textBox.requestFocus();
            }
        });
        setupBackButton();
    }

    public void setImojiSearchListener(ImojiSearchBarListener searchListener){
        this.imojiSearchBarListener = searchListener;
    }

    public void setupCloseButton(){
        firstLeftIcon.setBackgroundResource(R.drawable.imoji_close);
        firstLeftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imojiSearchBarListener != null){
                    imojiSearchBarListener.onCloseButtonTapped();
                }
            }
        });
    }

    public void setupBackButton(){
        firstLeftIcon.setBackgroundResource(R.drawable.imoji_back);
        firstLeftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imojiSearchBarListener != null){
                    imojiSearchBarListener.onBackButtonTapped();
                }
            }
        });
    }

    public void setLeftButtonVisibility (int visibility) {
        firstLeftIcon.setVisibility(visibility);
    }

//    public void setText(String text){
//        textBox.setText(text);
//        textBox.clearFocus();
//    }

    protected void onDeleteSearchText() {

    }

    protected void onStartSearchText() {

    }

    public interface ImojiSearchBarListener{

        void onTextSubmit(String term);

        void onBackButtonTapped();

        void onCloseButtonTapped();
    }

}
