package com.example.eis2.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EdittextListner extends EditText {

    private KeyImeChange keyImeChangeListener;

    public EdittextListner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setKeyImeChangeListener(KeyImeChange listener) {
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public boolean onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyImeChangeListener != null) {
            return keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

}
