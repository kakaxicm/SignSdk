package com.qicode.mylibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

/**
 * Created by chenming on 16/10/21.
 */

public class ObservableMediaController extends MediaController {
    /**
     * 监听controller 是否可见
     */
    public interface ControllerUIStateListener {
        void onControllerShow(boolean isShow);
    }

    private ControllerUIStateListener mListener;

    public ObservableMediaController(Context context){
        super(context);
    }

    public ObservableMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(ControllerUIStateListener listener){
        mListener = listener;
    }

    @Override
    public void show(int time) {
        super.show(time);
        if(mListener != null){
            mListener.onControllerShow(true);
        }
    }

    @Override
    public void hide() {
        super.hide();
        mListener.onControllerShow(false);
    }
}
