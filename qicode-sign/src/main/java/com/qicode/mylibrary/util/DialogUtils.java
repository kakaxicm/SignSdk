package com.qicode.mylibrary.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.qicode.mylibrary.R;
import com.qicode.mylibrary.dialog.ProgressDialog;

public class DialogUtils {
    private static Toast mShortPromptToast;
    private static Toast mLongPromptToast;

    public static void showShortPromptToast(Context context, int resId) {
        if (mShortPromptToast == null) {
            mShortPromptToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        }
        mShortPromptToast.setText(resId);
        mShortPromptToast.show();
    }

    public static void showShortPromptToast(Context context, String res) {
        if (mShortPromptToast == null) {
            mShortPromptToast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        }
        mShortPromptToast.setText(res);
        mShortPromptToast.show();
    }

    public static void showLongPromptToast(Context context, String... res) {
        StringBuilder content = new StringBuilder();
        for (String string : res) {
            content.append(string);
        }
        if (mLongPromptToast == null) {
            mLongPromptToast = Toast.makeText(context, content.toString(), Toast.LENGTH_LONG);
        }
        mLongPromptToast.setText(content.toString());
        mLongPromptToast.show();
    }

    public static void showLongPromptToast(Context context, int resId) {
        if (mLongPromptToast == null) {
            mLongPromptToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        }
        mLongPromptToast.setText(resId);
        mLongPromptToast.show();
    }

    /**
     * 弹出显示进度的dialog
     */
    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出显示进度的dialog
     */
    public static ProgressDialog showProgressDialog(Context context, String title) {
        ProgressDialog dialog = new ProgressDialog(context, title);
        dialog.show();
        return dialog;
    }

    public static void showConfirmDialog(Context context, int strTitleResId, int strMsgResId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitleResId).setMessage(strMsgResId).setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    public static void showConfirmDialog(Context context, String strTitle, String strMsg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle).setMessage(strMsg).setPositiveButton(R.string.positive, listener).setNegativeButton(R.string.negative, null)
                .show();
    }

    public static void showConfirmDialog(Context context, String strTitle, String strMsg, DialogInterface.OnClickListener listener,
                                         DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle).setMessage(strMsg).setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, cancelListener).show();
    }


    public static void showConfirmDialog(Context context, int messageId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messageId).setPositiveButton(R.string.positive, listener).setNegativeButton(R.string.negative, listener).show();
    }

    public static void showWarnDialog(Context context, String strTitle, String strMessage, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle).setMessage(strMessage).setNegativeButton(R.string.positive, listener).show();
    }

}
