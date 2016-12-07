package com.qicode.mylibrary.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.exception.HttpException;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.adapter.StickerExpertSignGridAdapter;
import com.qicode.mylibrary.constant.NetConstant;
import com.qicode.mylibrary.model.SignListResponse;
import com.qicode.mylibrary.util.DialogUtils;
import com.qicode.mylibrary.util.NetUtils;

/**
 * Created by chenming on 16/11/24.
 */

public class ExpertSignListFragment extends Fragment {
    private View mRootView;
    private RecyclerView mRcv;
    private StickerExpertSignGridAdapter mAdapter;
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_expert_sign_list, null);
        mRcv = (RecyclerView) mRootView.findViewById(R.id.rcv);

        mPage = 1;
        mAdapter = new StickerExpertSignGridAdapter(getActivity());
        mRcv.setAdapter(mAdapter);
        mRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        NetUtils.getInstance().post(getActivity(), NetConstant.URL.SIGN_LIST_URL.getUrl(getActivity()),
                NetConstant.getSignListParams(getActivity(), mPage, NetConstant.DEFAULT_PAGE_COUNT), new SignListListener(getActivity()));
    }

    private class SignListListener extends NetUtils.Callback<SignListResponse> {
        public SignListListener(Activity activity) {
            super(activity, SignListResponse.class);
        }

        @Override
        public void onNetSuccess(SignListResponse response) {
            onReceiveData(response);
        }

        @Override
        public void onNetFailed(HttpException e) {
            DialogUtils.showShortPromptToast(getActivity(), e.getLocalizedMessage());
        }
    }

    private void onReceiveData(SignListResponse response) {
        mAdapter.setSignList(response.getResult().getUser_signs(), response.getResult().getExpert_signs());
    }
}
