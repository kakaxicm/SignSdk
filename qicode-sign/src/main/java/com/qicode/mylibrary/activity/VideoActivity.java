package com.qicode.mylibrary.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.download.DownloadCallback;
import com.qicode.mylibrary.download.DownloadManager;
import com.qicode.mylibrary.download.DownloadNetConfig;
import com.qicode.mylibrary.download.DownloadTask;
import com.qicode.mylibrary.download.DownloadTaskInfo;
import com.qicode.mylibrary.util.MD5Utils;
import com.qicode.mylibrary.util.StringUtils;
import com.qicode.mylibrary.widget.ObservableMediaController;

import java.io.File;

/**
 * Created by star on 15/10/3.
 */
public class VideoActivity extends BaseActivity {
    private String mVideoName;
    private String mUrl;
    private android.widget.VideoView mVideoView;
    private ProgressBar mProgressBar;

    /****
     * 视频下载监听UI
     */
    private TextView mVideoNameTv;
    private TextView mDownloadView;
    private DownloadTaskInfo mNewDownloadTaskInfo;//新的下载任务
    private DownloadTaskInfo mLocalColdTaskInfo;//数据库里的下载任务
    //开启新的下载任务
    private View.OnClickListener mStartDownloadTaskOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mNewDownloadTaskInfo = new DownloadTaskInfo(mVideoName, mUrl);
            mNewDownloadTaskInfo.mLocalPath = DownloadNetConfig.DOWNLOAD_PATH + MD5Utils.getMD5(mNewDownloadTaskInfo.getmDownloadUrl()) + ".mp4";
            mNewDownloadTaskInfo.addCallback(new VideoDownloadCallback(mNewDownloadTaskInfo));
            DownloadManager.getInstance(mContext).addTask(mNewDownloadTaskInfo);
        }
    };

    //从数据库冷启动
    private View.OnClickListener mRestartTaskFromDbOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DownloadTaskInfo taskInfo = (DownloadTaskInfo) v.getTag();
            taskInfo.getCallbacklist().clear();
            taskInfo.addCallback(new VideoDownloadCallback(taskInfo));
            //从数据库中进行冷启动
            DownloadManager.getInstance(mContext).restartDownloadTaskFromDb(taskInfo);
        }
    };

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_video;
    }

    @Override
    protected void getIntentData() {
        mUrl = getIntent().getStringExtra(AppConstant.INTENT_VIDEO_URL);
        mVideoName = getIntent().getStringExtra(AppConstant.INTENT_VIDEO_NAME);
    }

    @Override
    protected void initContent() {
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mVideoView = (android.widget.VideoView) findViewById(R.id.surface_view);
        final ObservableMediaController mc = new ObservableMediaController(this);
        mc.setListener(new ObservableMediaController.ControllerUIStateListener() {
            @Override
            public void onControllerShow(boolean isShow) {
                findViewById(R.id.download_ui_container).setVisibility(isShow?View.VISIBLE:View.GONE);
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
                mc.show();
            }
        });
        mc.setAnchorView(mVideoView);
        mVideoView.setMediaController(mc);

        /**
         * 视频名称初始化
         */
        mVideoNameTv = (TextView) findViewById(R.id.tv_video_name);
        mVideoNameTv.setText(mVideoName);
        mVideoNameTv.setOnClickListener(this);
        /**
         * 下载监听测试
         */
        mDownloadView = (TextView) findViewById(R.id.tv_download);
        Uri uri = dumpDownloadTask();
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();

    }

    /**
     * 读取下载任务的调度状态，初始化下载的UI
     *
     * @return 下载文件的URI
     */
    private Uri dumpDownloadTask() {
        /**
         * 读取这个视频地址的任务状态
         * step1:获取这个url对应的任务ID
         *     1.1:如果有，则走step2
         *     1.2:数据库里面没有这条记录，则表示这个视频从未下载过
         * step2:从内存中读取，
         *     2.1如果有，则给它直接加上任务监听
         *     2.2 如果没有，则从数据库里读取
         */
        Uri uri = Uri.parse(mUrl);//默认走网络
        //STEP1 获取这个url对应的任务ID
        mLocalColdTaskInfo = DownloadManager.getInstance(mContext).getDownloadTaskByUrlFromDb(mContext, mUrl);//查找本地是否曾经下载过这个视频
        if (mLocalColdTaskInfo == null) {
            //STEP1.2 该视频从未被下载过
            uri = Uri.parse(mUrl);
            //UI处理 点击新建下载任务
            mDownloadView.setOnClickListener(mStartDownloadTaskOnClickListener);
        } else {
            //STEP2 从内存中读取任务
            long taskId = mLocalColdTaskInfo.mId;
            DownloadTask currentScheduleTask = DownloadManager.getInstance(mContext).getDownloadTaskByIdFromMemory(taskId);
            if (currentScheduleTask != null) {
                //STEP 2.1 内存中这个任务在调度中，则加上这个任务的调度监听
                currentScheduleTask.mInfo.getCallbacklist().clear();
                currentScheduleTask.mInfo.addCallback(new VideoDownloadCallback(currentScheduleTask.mInfo));//监听当前任务的状态
                //UI处理
                setDownloadUI(currentScheduleTask.mInfo.mState);
            } else {
                //STEP2.2 内存中没有调度该任务(进程被回收或者完全退出会造成这样的情况)
                // 该任务不在内存中，但在数据库中有记录,则取出记录，显示当前状态
                //如果文件下载完成，则直接将URI设为本地的文件地址
                if (mLocalColdTaskInfo.mState == DownloadTask.STATE_FINISH) {
                    File f = new File(mLocalColdTaskInfo.mLocalPath);
                    if (f.exists()) {
                        uri = Uri.fromFile(f);
                    }
                }
                //UI处理
                setDownloadUI(mLocalColdTaskInfo.mState);
            }

        }
        return uri;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.tv_video_name){
            finish();
        }
    }

    /**
     * 这里做简单处理，只处理成功的情况，不成功直接都转为重新下载
     */
    private void setDownloadUI(int state) {
        switch (state) {
            case DownloadTask.STATE_FINISH:
                mDownloadView.setText(R.string.download_state_finish);
                mDownloadView.setClickable(false);
                break;
            default:
                mDownloadView.setClickable(true);
                mDownloadView.setTag(mLocalColdTaskInfo);
                mDownloadView.setText(R.string.restart_download);
                mDownloadView.setOnClickListener(mRestartTaskFromDbOnclickListener);
                break;
        }
    }

    private class VideoDownloadCallback implements DownloadCallback {
        private DownloadTaskInfo mAttachedTaskInfo;//监听那个下载任务

        public VideoDownloadCallback(DownloadTaskInfo info) {
            if (info != null) {
                mAttachedTaskInfo = info;
            }
        }

        @Override
        public void onWait() {
            mDownloadView.setText(R.string.download_state_wait);
            mDownloadView.setClickable(false);
        }

        @Override
        public void onStart() {
            mDownloadView.setText(R.string.download_state_start);
            mDownloadView.setClickable(false);
        }

        @Override
        public void onUpdate(int progress) {
            mDownloadView.setText(StringUtils.getString(progress, "%"));
            mDownloadView.setClickable(false);
        }

        @Override
        public void onFinish() {
            mDownloadView.setClickable(false);
            mDownloadView.setText(R.string.download_state_finish);
            mDownloadView.setClickable(false);
        }

        @Override
        public void onFail(String error) {
            mDownloadView.setText(R.string.download_state_fail);
            mDownloadView.setClickable(true);
            mDownloadView.setTag(mAttachedTaskInfo);
            mDownloadView.setOnClickListener(mRestartTaskFromDbOnclickListener);
        }

        @Override
        public void onStop() {
            mDownloadView.setText(R.string.download_state_stop);
            mDownloadView.setClickable(true);
            mDownloadView.setTag(mAttachedTaskInfo);
            mDownloadView.setOnClickListener(mRestartTaskFromDbOnclickListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

}
