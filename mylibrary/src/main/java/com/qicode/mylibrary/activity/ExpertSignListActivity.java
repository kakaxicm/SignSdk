package com.qicode.mylibrary.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.qicode.mylibrary.R;
import com.qicode.mylibrary.fragment.ExpertSignListFragment;

/**
 * Created by chenming on 16/11/24.
 */

public class ExpertSignListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutViewId());
        initContent();
    }

    private void initContent() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ll_content, new ExpertSignListFragment());
        fragmentTransaction.commit();

        TextView titleTv = (TextView) findViewById(R.id.tv_left_title);
        titleTv.setText(R.string.expert_sign);
    }

    private int setLayoutViewId() {
        return R.layout.activity_expert_sign_list;
    }
}
