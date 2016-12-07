package com.qicode.mylibrary.adapter;

import android.app.Activity;
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
import com.qicode.mylibrary.activity.ExpertSignDetailActivity;
import com.qicode.mylibrary.activity.ImitateActivity;
import com.qicode.mylibrary.activity.SignPayActivity;
import com.qicode.mylibrary.activity.SignProductPreviewActivity;
import com.qicode.mylibrary.activity.VideoActivity;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.model.ExpertSignLineItem;
import com.qicode.mylibrary.model.SignListResponse;
import com.qicode.mylibrary.util.DialogUtils;
import com.qicode.mylibrary.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StickerExpertSignGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int Type_User_Sign = 0;
    private final int Type_Divider = 1;
    private final int Type_Expert_Sign = 2;//两列显示

    private final int Sign_Designing = 1;
    private final int Sign_Design_Finish = 2;//设计完成：判断是否有视频

    private final int Video_No = 1;
    private final int Video_Yes = 2;

    private Context mContext;
    private Activity mActivity;
    private List<SignListResponse.ResultEntity.UserSignsEntity> mUserSignList;
    private List<SignListResponse.ResultEntity.ExpertSignsEntity> mExpertSignList;
    private List<ExpertSignLineItem> mExpertSignLineItems;
    private String mName;

    public StickerExpertSignGridAdapter(Context context) {
        mContext = context;
        mActivity = (Activity) context;
        mUserSignList = new ArrayList<>();
        mExpertSignList = new ArrayList<>();
        mExpertSignLineItems = new ArrayList<>();
    }

    public void setSignList(List<SignListResponse.ResultEntity.UserSignsEntity> userSignList,
                            List<SignListResponse.ResultEntity.ExpertSignsEntity> expertSignList) {
        if (userSignList != null) {
            mUserSignList.clear();
            mUserSignList.addAll(userSignList);
        }
        if (expertSignList != null) {
            mExpertSignList.clear();
            mExpertSignList.addAll(expertSignList);
            buildExpertLineData(expertSignList);
        }
        notifyDataSetChanged();
    }

    /**
     * 构建双列的专家签名数据
     */
    private void buildExpertLineData(List<SignListResponse.ResultEntity.ExpertSignsEntity> expertSignList) {
        mExpertSignLineItems.clear();
        int metaItemCount = expertSignList.size();//元数据个数
        int count = (metaItemCount % 2 == 0) ? metaItemCount / 2 : metaItemCount / 2 + 1;//行数
        for (int line = 0; line < count; line++) {
            //left item
            int leftIndex = line * 2;
            int rightIndex = line * 2 + 1;
            ExpertSignLineItem lineItem = new ExpertSignLineItem();

            SignListResponse.ResultEntity.ExpertSignsEntity leftMetaItem = expertSignList.get(leftIndex);
            SignListResponse.ResultEntity.ExpertSignsEntity rightMetaItem = null;
            if (rightIndex < metaItemCount) {
                rightMetaItem = expertSignList.get(rightIndex);
            }
            lineItem.setLeftExpertSignItem(leftMetaItem);
            lineItem.setRightExpertSignItem(rightMetaItem);
            mExpertSignLineItems.add(lineItem);
        }
    }

    public void addExpertSignList(List<SignListResponse.ResultEntity.ExpertSignsEntity> expertSignList) {
        int total = getItemCount();
        mExpertSignList.addAll(expertSignList);
        buildExpertLineData(mExpertSignList);
        notifyItemRangeChanged(total - 1, expertSignList.size());
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int getItemCount() {
        int count = mUserSignList.size() + mExpertSignLineItems.size();
        if (mUserSignList.size() > 0 && mExpertSignLineItems.size() > 0) {
            count++;
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Type_User_Sign:
                return new UserSignViewHolder(View.inflate(mContext, R.layout.item_sign_user, null));
            case Type_Divider:
                return new DividerViewHolder(View.inflate(mContext, R.layout.item_sign_divider, null));
            case Type_Expert_Sign:
                return new ExpertSignLineViewHolder(View.inflate(mContext, R.layout.item_sign_expert_line, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        if(position > 0) {
        Object data = getItem(position);
        if (data instanceof SignListResponse.ResultEntity.UserSignsEntity && viewHolder instanceof UserSignViewHolder) {
            UserSignViewHolder holder = (UserSignViewHolder) viewHolder;
            holder.attachData((SignListResponse.ResultEntity.UserSignsEntity) data);
        }
        if (data instanceof ExpertSignLineItem && viewHolder instanceof ExpertSignLineViewHolder) {
            ExpertSignLineViewHolder holder = (ExpertSignLineViewHolder) viewHolder;
            holder.attachData((ExpertSignLineItem) data);
        }
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mUserSignList.size() > 0) {
            if (position < mUserSignList.size()) {
                return Type_User_Sign;
            } else if (position == mUserSignList.size()) {
                return Type_Divider;
            } else {
                return Type_Expert_Sign;
            }
        } else {
            return Type_Expert_Sign;
        }
    }

    public Object getItem(int position) {
        if (mUserSignList.size() > 0) {
            if (position < mUserSignList.size()) {
                return mUserSignList.get(position);//用户列表
            } else if (position == mUserSignList.size()) {
                return null;//分割线
            } else {
                return mExpertSignLineItems.get(position - mUserSignList.size() - 1);
            }
        } else {
            return mExpertSignLineItems.get(position);
        }
    }

    public class UserSignViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View contentView;
        SimpleDraweeView productView;
        TextView signCategoryTv;
        TextView ownerView;
        TextView imitateView;
        TextView watchView;
        TextView designView;
        TextView signNameView;
        TextView commentTv;

        View designingContainer;
        View finishContainer;

        View weixinPublicICon;

        public UserSignViewHolder(View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.ll_content);
            productView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_preview);
            signCategoryTv = (TextView) itemView.findViewById(R.id.tv_sign_category_name);
            ownerView = (TextView) itemView.findViewById(R.id.tv_product_owner);
            imitateView = (TextView) itemView.findViewById(R.id.tv_imitate);
            watchView = (TextView) itemView.findViewById(R.id.tv_watch);
            designView = (TextView) itemView.findViewById(R.id.tv_designing);
            signNameView = (TextView) itemView.findViewById(R.id.tv_sign_name);
            commentTv = (TextView) itemView.findViewById(R.id.tv_comment);

            finishContainer = itemView.findViewById(R.id.ll_finish_container);
            designingContainer = itemView.findViewById(R.id.ll_designing_container);
            weixinPublicICon = itemView.findViewById(R.id.img_weixin_public);

            productView.setOnClickListener(this);
            imitateView.setOnClickListener(this);
            watchView.setOnClickListener(this);
            weixinPublicICon.setOnClickListener(this);
            commentTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            SignListResponse.ResultEntity.UserSignsEntity data = (SignListResponse.ResultEntity.UserSignsEntity) v.getTag();
            if (v.getId() == R.id.sdv_preview) {
                if (data != null && data.getSign_status() == Sign_Design_Finish) {
                    String url = data.getImage_url();
                    intent = new Intent(mContext, SignProductPreviewActivity.class);
                    intent.putExtra(AppConstant.INTENT_ONLINE_IMAGE_URL, url);
                    mContext.startActivity(intent);
                } else if (data == null || data.getSign_status() == Sign_Designing) {
                    DialogUtils.showShortPromptToast(mContext, "您的签名还在设计中.");
                }
            } else if (v.getId() == R.id.tv_imitate) {
                //跳到临摹界面
                String signImgPath = data.getImage_url();
                intent = new Intent(mContext, ImitateActivity.class);
                intent.putExtra(AppConstant.INTENT_IS_ONLINE_SIGN, true);
                intent.putExtra(AppConstant.INTENT_IMAGE_URL, signImgPath);
                mContext.startActivity(intent);
            } else if (v.getId() == R.id.tv_watch) {//观看教程or我要教程
                String video = data.getVideo_url();
                if (!TextUtils.isEmpty(video)) {//观看教程
                    String name = data.getExpert_sign__sign_name();
                    intent = new Intent(mContext, VideoActivity.class);
                    intent.putExtra(AppConstant.INTENT_VIDEO_URL, video);
                    intent.putExtra(AppConstant.INTENT_VIDEO_NAME, name);
                    intent.putExtra(AppConstant.INTENT_CAN_DOWNLOAD_VIDEO, true);
                    mContext.startActivity(intent);
                } else {
                    //我要教程,跳转到支付页面
                    intent = new Intent(mContext, SignPayActivity.class);
                    intent.putExtra(AppConstant.INTENT_EXPERT_SIGN_ID, data.getExpert_sign());//专家签名的id
                    intent.putExtra(AppConstant.INTENT_NAME, data.getSign_user_name());//签名内容
                    intent.putExtra(AppConstant.INTENT_IS_APPEND_VIDEO, true);//追加视频标记
                    intent.putExtra(AppConstant.INTENT_USER_SIGN_ID, data.getUser_sign_id());
                    mContext.startActivity(intent);
                }
            }
//                case R.id.img_weixin_public:
            //copy微信公众号,跳转微信
//                    StringUtils.copy(mContext, "artsignpro");
//                    DialogUtils.showShortPromptToast(mContext, StringUtils.paste(mContext));
//                    intent = mContext.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//                    ActivityUtils.jump(mContext, intent);
//                    break;
//                case R.id.tv_comment:
//                    if(TextUtils.isEmpty(data.getComment())) {
//                        CommentDialog commentDialog = new CommentDialog(mContext, mActivity);
//                        commentDialog.setUserSignId(data.getUser_sign_id());
//                        commentDialog.setView((TextView)v);
//                        commentDialog.show();
//                    } else {
//                        CommonDetailDialog commonDetailDialog = new CommonDetailDialog(mContext);
//                        commonDetailDialog.setContent(data.getComment());
//                        commonDetailDialog.show();
//                    }
//                    break;
//            }
        }

        public void attachData(SignListResponse.ResultEntity.UserSignsEntity data) {

            //判断sign_status
            //1设计中：
            //是否追加视频
            //1.1 是追加视频, 则显示图片，临摹，和视频设计中按钮
            //1.2 不是追加视频,则表示初次下单，只显示设计中按钮
            //2设计完成:
            //判断产品类型sign_type
            //2.1 设计稿:显示图片，临摹，我要教程按钮
            //2.2 设计稿+视频:显示图片，临摹和观看教程按钮
            commentTv.setVisibility(View.GONE);
            commentTv.setText("未评论");
            if (!TextUtils.isEmpty(data.getComment())) {
                commentTv.setText("已评论");
            }

            switch (data.getSign_status()) {
                case Sign_Designing:
                    boolean isAddVideo = data.getIs_addition_video() == 2;//是否追加视频
                    weixinPublicICon.setVisibility(View.GONE);

                    if (!isAddVideo) {//没有追加视频，表示初次下单设计中
                        // 边框
                        contentView.setBackgroundResource(R.drawable.bg_item_sign_gray);
                        productView.setBackgroundResource(R.drawable.bg_item_sign_gray);
                        finishContainer.setVisibility(View.GONE);
                        designingContainer.setVisibility(View.VISIBLE);
                        // “设计中”按钮
                        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH:mm", Locale.CHINA);
                        String deadline = format.format(new Date(data.getDeadline_time_stamp() * 1000L));
                        designView.setText(mContext.getString(R.string.complete_before_deadline, deadline));
                        // “开始临摹”按钮
                        imitateView.setTag("");
                        // “观看教程"按钮
                        watchView.setTag("");
                        watchView.setOnClickListener(null);
                        watchView.setBackgroundResource(R.drawable.bg_item_sign_blue);
                        watchView.setTextColor(mContext.getResources().getColor(R.color.white));
                    } else {//有追加视频，则表示该订单是二次追加的，有设计稿图片
                        finishContainer.setVisibility(View.VISIBLE);
                        designingContainer.setVisibility(View.GONE);
                        //显示视频设计中...无点击事件
                        watchView.setText(R.string.video_designing);
                        watchView.setBackgroundResource(R.drawable.bg_button_designing);
                        watchView.setTextColor(mContext.getResources().getColor(R.color.white));
                        watchView.setTag("");
                        watchView.setOnClickListener(null);
                        imitateView.setTag(data);
                        productView.setTag(data);
                        commentTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case Sign_Design_Finish:
                    // 边框
                    contentView.setBackgroundResource(R.drawable.bg_item_sign_blue);
                    productView.setBackgroundResource(R.drawable.bg_item_sign_blue);
                    weixinPublicICon.setVisibility(View.GONE);
                    // “开始临摹”按钮
                    finishContainer.setVisibility(View.VISIBLE);
                    designingContainer.setVisibility(View.GONE);
                    imitateView.setTag(data);
                    productView.setTag(data);
                    //判断产品类型
                    boolean isContainerVideo = !TextUtils.isEmpty(data.getVideo_url());
                    if (isContainerVideo) {//设计稿加视频
                        watchView.setText(R.string.watch_video);
                        watchView.setBackgroundResource(R.drawable.bg_button_blue);
                        watchView.setTextColor(mContext.getResources().getColor(R.color.white));
                    } else {
                        watchView.setText(R.string.want_video);
                        watchView.setBackgroundResource(R.drawable.bg_item_sign_blue_corner);
                        watchView.setTextColor(mContext.getResources().getColor(R.color.blue3));
                    }
                    watchView.setTag(data);
                    commentTv.setTag(data);
                    watchView.setOnClickListener(this);
                    commentTv.setVisibility(View.VISIBLE);
                    break;
            }
            // 预览图
            productView.setAspectRatio(1.33f);
            productView.setImageURI(Uri.parse(data.getImage_url()));
            // 签名名字
            signNameView.setText(data.getSign_user_name());
            // 产品详情
            signCategoryTv.setText(data.getExpert_sign__sign_name());
        }
    }

    public class DividerViewHolder extends RecyclerView.ViewHolder {

        public DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ExpertSignLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View contentView;
        View containerLeft;
        View containerRight;
        SimpleDraweeView leftSdv;
        SimpleDraweeView rightSdv;
        TextView leftSignStyleTv;
        TextView rightSignStyleTv;


        TextView leftPriceTv;
        TextView rightPriceTv;

        public ExpertSignLineViewHolder(View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.ll_content);
            containerLeft = itemView.findViewById(R.id.container_left_item);
            containerRight = itemView.findViewById(R.id.container_right_item);

            leftSdv = (SimpleDraweeView) containerLeft.findViewById(R.id.sdv_preview);
            rightSdv = (SimpleDraweeView) containerRight.findViewById(R.id.sdv_preview);

            leftSignStyleTv = (TextView) containerLeft.findViewById(R.id.tv_expert_sign_name);
            rightSignStyleTv = (TextView) containerRight.findViewById(R.id.tv_expert_sign_name);
            leftPriceTv = (TextView) containerLeft.findViewById(R.id.tv_price);
            rightPriceTv = (TextView) containerRight.findViewById(R.id.tv_price);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            SignListResponse.ResultEntity.ExpertSignsEntity data = (SignListResponse.ResultEntity.ExpertSignsEntity) v.getTag();
            //TODO revert
            int id = v.getId();
            if(id == R.id.container_left_item || id == R.id.container_right_item){
                intent = new Intent(mContext, ExpertSignDetailActivity.class);
                intent.putExtra(AppConstant.INTENT_EXPERT_SIGN_ID, data.getExpert_sign_id());
                intent.putExtra(AppConstant.INTENT_NAME, mName);
                intent.putExtra(AppConstant.INTENT_DESIGN_PRICE, data.getDesign_price());
                intent.putExtra(AppConstant.INTENT_VIDEO_PRICE, data.getVideo_price());
                mContext.startActivity(intent);
            }
        }

        public void attachData(ExpertSignLineItem data) {
            // 详情页跳转id
            contentView.setTag(data);
            SignListResponse.ResultEntity.ExpertSignsEntity leftExpertSignItem = data.getLeftExpertSignItem();
            SignListResponse.ResultEntity.ExpertSignsEntity rightExpertSignItem = data.getRightExpertSignItem();

            // 左边预览图
            leftSdv.setImageURI(Uri.parse(leftExpertSignItem.getImage_url()));

            String signName = leftExpertSignItem.getSign_name();
            leftSignStyleTv.setText(signName);

            String result = StringUtils.getPrice(leftExpertSignItem.getDesign_price());
            leftPriceTv.setText(StringUtils.getString("￥", result));
            containerLeft.setTag(leftExpertSignItem);
            containerLeft.setOnClickListener(this);
            //右边有可能是空
            if (rightExpertSignItem != null) {
                rightSdv.setImageURI(Uri.parse(rightExpertSignItem.getImage_url()));

                signName = rightExpertSignItem.getSign_name();
                rightSignStyleTv.setText(signName);

                result = StringUtils.getPrice(rightExpertSignItem.getDesign_price());
                rightPriceTv.setText(StringUtils.getString("￥", result));
                containerRight.setVisibility(View.VISIBLE);
                containerRight.setTag(rightExpertSignItem);
                containerRight.setOnClickListener(this);
            } else {
                containerRight.setVisibility(View.INVISIBLE);
            }
        }
    }
}
