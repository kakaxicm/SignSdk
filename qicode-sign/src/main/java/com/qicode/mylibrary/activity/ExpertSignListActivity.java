package com.qicode.mylibrary.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.qicode.mylibrary.R;
import com.qicode.mylibrary.fragment.ExpertSignListFragment;
import com.qicode.mylibrary.util.SizeUtils;

/**
 * Created by chenming on 16/11/24.
 */

public class ExpertSignListActivity extends BaseActivity {
    @Override
    protected void initContent() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ll_content, new ExpertSignListFragment());
        fragmentTransaction.commit();

        TextView titleTv = (TextView) findViewById(R.id.tv_left_title);
        titleTv.setText(R.string.expert_sign);

        findViewById(R.id.iv_left).setVisibility(View.GONE);
        titleTv.setPadding((int) SizeUtils.dp2Px(this, 10), 0, 0, 0);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_expert_sign_list;
    }
}
