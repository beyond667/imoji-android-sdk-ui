package io.imoji.sdk.widgets.searchwidgets.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import io.imoji.sdk.ui.R;

/**
 * Created by engind on 4/21/16.
 */
public class ImojiSearchBarLayout extends ViewSwitcher {

    private View backCancelIcon;
    private View clearIcon;
    private ImojiEditText textBox;
    private LinearLayout extraActionsLayout;
    protected int recentsLayout = R.layout.imoji_recents_bar_large;

    private ImojiSearchBarListener imojiSearchBarListener;
    private boolean shouldTriggerAutoSearch = true;
    private boolean extraButtonsEnabled = true;

    public ImojiSearchBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.imoji_search_bar, this);


        backCancelIcon = this.findViewById(R.id.search_bar_back_cancel_icon);
        textBox = (ImojiEditText) this.findViewById(R.id.search_bar_text_box);
        clearIcon = this.findViewById(R.id.search_bar_clear_icon);
        extraActionsLayout = (LinearLayout) this.findViewById(R.id.search_bar_extra_action_container);

        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start == 0 && s.length() > 0) {
                    clearIcon.setVisibility(VISIBLE);
                } else if (before > 0 && s.length() == 0) {
                    clearIcon.setVisibility(GONE);
                }

                if (extraButtonsEnabled && s.length() == 0 && !textBox.hasFocus()) {
                    extraActionsLayout.setVisibility(VISIBLE);
                } else if (extraActionsLayout.getVisibility() == VISIBLE) {
                    extraActionsLayout.setVisibility(GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imojiSearchBarListener.onTextChanged(s.toString(), shouldTriggerAutoSearch);
            }
        });

        textBox.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (imojiSearchBarListener != null) {
                    imojiSearchBarListener.onFocusChanged(hasFocus);
                }
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

        clearIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                textBox.setText("");
                textBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(textBox, InputMethodManager.SHOW_IMPLICIT);
                imojiSearchBarListener.onTextCleared();
            }
        });

        findViewById(R.id.search_bar_search_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textBox.requestFocus();
            }
        });

        findViewById(R.id.search_bar_recent_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecentsView();
                imojiSearchBarListener.onRecentsButtonTapped();
            }
        });

        setupBackButton();
        textBox.requestFocus();
    }

    public void setExtraButtonsEnabled(boolean extraButtonsEnabled){
        this.extraButtonsEnabled = extraButtonsEnabled;
        extraActionsLayout.setVisibility(extraButtonsEnabled ? VISIBLE : GONE);
    }

    public void toggleTextFocus(boolean shouldRequest) {
        if (shouldRequest) {
            textBox.requestFocus();
        } else {
            textBox.clearFocus();
        }
    }

    public void setImojiSearchListener(ImojiSearchBarListener searchListener) {
        this.imojiSearchBarListener = searchListener;
    }

    public void setupCloseButton() {
        backCancelIcon.setBackgroundResource(R.drawable.imoji_close);
        backCancelIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imojiSearchBarListener != null) {
                    imojiSearchBarListener.onCloseButtonTapped();
                }
            }
        });
    }

    public void setupBackButton() {
        backCancelIcon.setBackgroundResource(R.drawable.imoji_back);
        backCancelIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imojiSearchBarListener != null) {
                    imojiSearchBarListener.onBackButtonTapped();
                }
            }
        });
    }

    public void showRecentsView() {
        if (getChildAt(1) != null) {
            removeViewAt(1);
        }

        LayoutInflater.from(getContext()).inflate(recentsLayout, this);

        ((TextView) findViewById(R.id.recents_bar_text))
                .setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.otf"));

        View backIcon = findViewById(R.id.recents_bar_back_icon);
        if (backIcon != null) {
            backIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeViewAt(1);
                    setDisplayedChild(0);
                    imojiSearchBarListener.onBackButtonTapped();
                }
            });
        }

        View createIcon = findViewById(R.id.recents_bar_create_icon);
        if (createIcon != null) {
            createIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    imojiSearchBarListener.onCreateButtonTapped();
                }
            });
        }

        findViewById(R.id.recents_bar_search_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeViewAt(1);
                setDisplayedChild(0);
                toggleTextFocus(true);
            }
        });

        setDisplayedChild(1);
    }

    public void setLeftButtonVisibility(int visibility) {
        backCancelIcon.setVisibility(visibility);
    }

    public void setText(String text) {
        shouldTriggerAutoSearch = false;
        textBox.setText(text);
        shouldTriggerAutoSearch = true;
        textBox.clearFocus();
    }

    public void setRecentsLayout(@LayoutRes int recentsLayout) {
        this.recentsLayout = recentsLayout;
    }

    public interface ImojiSearchBarListener {

        void onTextSubmit(String term);

        void onTextCleared();

        void onBackButtonTapped();

        void onCloseButtonTapped();

        void onFocusChanged(boolean hasFocus);

        void onTextChanged(String term, boolean shouldTriggerAutoSearch);

        void onRecentsButtonTapped();

        void onCreateButtonTapped();
    }

}
