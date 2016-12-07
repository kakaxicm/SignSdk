package com.qicode.mylibrary.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.util.SizeUtils;
import com.qicode.mylibrary.widget.FlowLayout;

/**
 * Created by star on 16/4/4.
 */
public class EnglishNameListActivity extends BaseActivity{
    private FlowLayout mMaleNamesFl;
    private FlowLayout mFemaleNamesFl;
    private String[] MALE_NAMES = {
            "Adam", "Alan","Brian", "Edward",
            "Alex", "Steven","Jack", "Leo",
            "Andy", "Oscar","Richard",
            "Tom","Wesley", "Sam",
            "Robinson", "Robert", "Philip", "Larry", "Kevin"};
    private String[] FEMALE_NAMES = {
            "Malcolm", "Joan","Niki", "Betty",
            "Linda", "Whitney","Lily", "Helen",
            "Katharine", "Lee","Ann",
            "Diana","Fiona", "Shelly",
            "Mary", "Dolly","Nancy","Jane",
            "Barbara","Ross","Julie","Gloria","Carol"};
    private String mSelectedName;
    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_en_name_list;
    }

    @Override
    protected void initTitle() {
        TextView titleView = (TextView) findViewById(R.id.tv_left_title);
        titleView.setText("选择英文名");
        ImageView rightBtn = (ImageView) findViewById(R.id.iv_right);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.select_finish);
        setOnClickListener(findViewById(R.id.iv_left), rightBtn);
    }


    @Override
    protected void initContent() {
        mMaleNamesFl = (FlowLayout) findViewById(R.id.fl_male_names);
        mFemaleNamesFl = (FlowLayout) findViewById(R.id.fl_female_names);
    }

    @Override
    protected void attachData() {
        mMaleNamesFl.setTag(0);//section
        for (int i = 0; i < MALE_NAMES.length; i++) {
            String name = MALE_NAMES[i];

            TextView maleNameTv = createTextView(name);
            maleNameTv.setTag(i);

            mMaleNamesFl.addView(maleNameTv);
        }


        mFemaleNamesFl.setTag(1);//section
        for (int i = 0; i < FEMALE_NAMES.length; i++) {
            String name = FEMALE_NAMES[i];

            TextView feMaleNameTv = createTextView(name);
            feMaleNameTv.setTag(i);
            mFemaleNamesFl.addView(feMaleNameTv);
        }

        mMaleNamesFl.setTagSelectedListener(mTagSelectedListener);
        mFemaleNamesFl.setTagSelectedListener(mTagSelectedListener);
    }

    private FlowLayout.OnFlowTagOnSelectedListener mTagSelectedListener = new FlowLayout.OnFlowTagOnSelectedListener() {
        @Override
        public void onFlowTagSelected(int section, int pos) {
            if(section == 0) {
                mFemaleNamesFl.clearSelectedStates();
                mSelectedName = MALE_NAMES[pos];
            }else{
                mMaleNamesFl.clearSelectedStates();
                mSelectedName = FEMALE_NAMES[pos];
            }
        }
    };


    /**
     * 动态生成TextView
     */
    private TextView createTextView(String text) {
        TextView nameView = new TextView(this);
        int topGap = (int) SizeUtils.dp2Px(getResources(), 6);
        int leftGap = (int) SizeUtils.dp2Px(getResources(), 14);

        nameView.setPadding(leftGap, topGap, leftGap, topGap);
        nameView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        nameView.setTextSize(12);
        nameView.setText(text);
        return nameView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.iv_left){
            finish();
            return;
        }

        if(id == R.id.iv_right){
            Intent result = new Intent();
            result.putExtra(AppConstant.INTENT_SELECTED_EN_NAME, mSelectedName);
            setResult(RESULT_OK, result);
            finish();
            return;
        }
    }
}
