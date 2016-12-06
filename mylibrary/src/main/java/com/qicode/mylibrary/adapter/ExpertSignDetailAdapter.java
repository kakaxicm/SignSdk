package com.qicode.mylibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.activity.VideoActivity;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.model.SignDetailResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 16/3/14.
 */
public class ExpertSignDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SignDetailResponse mData;
    private List<SignDetailResponse.ResultEntity.DetailsDesignImageEntity> mImageEntities = new ArrayList<>();
    private Context mContext;


    private final int Type_VIDEO = 0;//视频
    private final int Type_IMG = 1;//图片
    private final int Type_Expert = 2;//专家介绍

    public ExpertSignDetailAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Type_VIDEO:
                //视频
                return new ViedItemViewHolder(View.inflate(mContext, R.layout.expert_detail_item_video, null));
            case Type_IMG:
                //图片
                return new ImageItemViewHolder(View.inflate(mContext, R.layout.expert_detail_item_image, null));
            case Type_Expert:
                //专家介绍
                return new ExpertItemViewHolder(View.inflate(mContext, R.layout.expert_detail_item_expert, null));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Type_VIDEO:
                //视频
                String videoUrl = mData.getResult().getVideo_url();
                String proName = mData.getResult().getSign_name();
                ((ViedItemViewHolder) holder).setVideoUrlAndProductName(videoUrl, proName);
                break;
            case Type_IMG:
                //图片
                int imageIndex = position - 1;//获取图片在数据列表的真实位置
                if (imageIndex < mImageEntities.size()) {
                    SignDetailResponse.ResultEntity.DetailsDesignImageEntity entity = mImageEntities.get(imageIndex);
                    ((ImageItemViewHolder) holder).setImage(entity);
                }
                break;
            case Type_Expert:
                //专家介绍
                ((ExpertItemViewHolder) holder).loadExpertInfo();
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : 1 + mImageEntities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return Type_VIDEO;

        if (mImageEntities.size() > 0) {
            if (position - 1 < mImageEntities.size()) {
                return Type_IMG;
            } else {
                return Type_Expert;
            }
        } else {
            return Type_Expert;
        }
    }

    public void setData(SignDetailResponse data) {
        if (data != null) {
            mData = data;
            mImageEntities = mData.getResult().getDetails_design_image();
            notifyDataSetChanged();
        }
    }

    private class ExpertItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mDesignerNameView;
        private TextView mDesignerTermView;
        private TextView mDesignerDescView;
        private SimpleDraweeView mDesignerHeaderView;

        //专家数据
        public ExpertItemViewHolder(View itemView) {
            super(itemView);
            mDesignerNameView = (TextView) itemView.findViewById(R.id.tv_name);
            mDesignerTermView = (TextView) itemView.findViewById(R.id.tv_term);
            mDesignerDescView = (TextView) itemView.findViewById(R.id.tv_desc);
            mDesignerHeaderView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_preview);
        }

        public void loadExpertInfo() {
            mDesignerHeaderView.setImageURI(Uri.parse(mData.getResult().getPortrait_url()));
            mDesignerNameView.setText(mData.getResult().getDesigner_name());
            mDesignerTermView.setText(mData.getResult().getDesigner_identity());
            mDesignerDescView.setText(mData.getResult().getDesigner_introduce());
        }

    }

    //图片
    private class ImageItemViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView mSdv;

        public ImageItemViewHolder(View itemView) {
            super(itemView);
            mSdv = (SimpleDraweeView) itemView.findViewById(R.id.sdv_image);
        }

        public void setImage(SignDetailResponse.ResultEntity.DetailsDesignImageEntity entity) {
            mSdv.setImageURI(Uri.parse(entity.getUrl()));
            mSdv.setAspectRatio(1.0f * entity.getWidth() / entity.getHeight());
        }
    }

    //视频
    private class ViedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mPlayVideoView;//播放视频按钮
        private String mVideoUrl;
        private String mProductName;

        public ViedItemViewHolder(View itemView) {
            super(itemView);
            mPlayVideoView = itemView.findViewById(R.id.iv_video_start);
        }

        public void setVideoUrlAndProductName(String url, String productName) {
            mVideoUrl = url;
            mProductName = productName;
            if (TextUtils.isEmpty(mVideoUrl)) {
                itemView.setVisibility(View.GONE);
            } else {
                mPlayVideoView.setOnClickListener(this);
                itemView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            int id = v.getId();
            if (id == R.id.iv_video_start) {
                if (!TextUtils.isEmpty(mVideoUrl)) {
                    intent = new Intent(mContext, VideoActivity.class);
                    intent.putExtra(AppConstant.INTENT_VIDEO_URL, mVideoUrl);
                    intent.putExtra(AppConstant.INTENT_VIDEO_NAME, mProductName);
                    mContext.startActivity(intent);
                }
            }
        }
    }

}
