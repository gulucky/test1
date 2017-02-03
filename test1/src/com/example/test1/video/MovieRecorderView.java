package com.example.test1.video;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OutputFormat;
import android.media.MediaRecorder.VideoEncoder;
import android.media.MediaRecorder.VideoSource;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.test1.R;
/**
 * 视频录制控件
 * 
 * @author lip
 * 
 * @date 2015-3-16
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MovieRecorderView extends LinearLayout implements OnErrorListener {
 
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ProgressBar mProgressBar;
 
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private Timer mTimer;// 计时器
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口
 
    private int mWidth;// 视频分辨率宽度
    private int mHeight;// 视频分辨率高度
    private boolean isOpenCamera;// 是否一开始就打开摄像头
    private int mRecordMaxTime;// 一次拍摄最长时间
    private int mTimeCount;// 时间计数
    private File mVecordFile = null;// 文件
 
    public MovieRecorderView(Context context) {
        this(context, null);
    }
 
    public MovieRecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MovieRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWidth=320;
        mHeight=240;
        isOpenCamera=true;
        mRecordMaxTime=10;
 
        LayoutInflater.from(context).inflate(R.layout.video_recorder_view, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(mRecordMaxTime);// 设置进度条最大量
 
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
 
    /**
     * 
     * @author liuyinjun
     * 
     * @date 2015-2-5
     */
    private class CustomCallBack implements Callback {
 
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            try {
                initCamera();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
 
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        	try {
//				if (mCamera != null) {
//					Parameters params = mCamera.getParameters();
//					List<Camera.Size> mSupportedPreviewSizes = params.getSupportedPreviewSizes();
//					Camera.Size size = params.getPreferredPreviewSizeForVideo();
//					if (mSupportedPreviewSizes != null) {
//						size = getOptimalPreviewSize(mSupportedPreviewSizes, 
//						            Math.max(width, height), Math.min(width, height));
//					}
//				    params.setPreviewSize(size.width, size.height);
//				    List<String> focusModes = params.getSupportedFocusModes();
//				    if(focusModes.contains("continuous-video")){
//				    	params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//				    }
//				    mCamera.setParameters(params);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
        }
 
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }
 
    }
 
    /**
     * 初始化摄像头
     * 
     * @author lip
     * @date 2015-3-16
     * @throws IOException
     */
    private void initCamera() throws IOException {
        if (mCamera != null) {
            freeCameraResource();
        }
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null)
            return;
 
        setCameraParams();
        mCamera.setDisplayOrientation(90);
        mCamera.setPreviewDisplay(mSurfaceHolder);
        mCamera.startPreview();
//        mCamera.unlock();
    }
 
    /**
     * 设置摄像头为竖屏
     * 
     * @author lip
     * @date 2015-3-16
     */
    private void setCameraParams() {
        if (mCamera != null) {
        	mWidth = 640;
        	mHeight = 10;
        	Parameters params = mCamera.getParameters();
        	List<Camera.Size> mSupportedPreviewSizes = params.getSupportedPreviewSizes();
        	Camera.Size size = params.getPreferredPreviewSizeForVideo();
        	if (mSupportedPreviewSizes != null) {
        		size = getOptimalPreviewSize(mSupportedPreviewSizes, 
        		            Math.max(mWidth, mHeight), Math.min(mWidth, mHeight));
    		}
//            params.set("orientation", "portrait");
            params.setPreviewSize(480, 720);
            List<String> focusModes = params.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
            	params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(params);
        }
    }
    
    /**
     * 从Google官方的Camera示例程序中可以看出，选择预览尺寸的标准是
     * （1）摄像头支持的预览尺寸的宽高比与SurfaceView的宽高比的绝对差值小于0.1；
     * （2）在（1）获得的尺寸中，选取与SurfaceView的高的差值最小的。
     * 通过代码对这两个标准进行了实现，这里贴一下官方的代码：
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) {
          return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
          double ratio = (double) size.width / size.height;
          if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
            continue;
          if (Math.abs(size.height - targetHeight) < minDiff) {
            optimalSize = size;
            minDiff = Math.abs(size.height - targetHeight);
          }
        }
        if (optimalSize == null) {
          minDiff = Double.MAX_VALUE;
          for (Camera.Size size : sizes) {
            if (Math.abs(size.height - targetHeight) < minDiff) {
              optimalSize = size;
              minDiff = Math.abs(size.height - targetHeight);
            }
          }
        }
        return optimalSize;
      }
 
    /**
     * 释放摄像头资源
     * 
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }
 
    private void createRecordDir() {
       // File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "im/video/");
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator+"RecordVideo/");
        //File sampleDir = new File("/video/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            mVecordFile = File.createTempFile("recording", ".mp4", vecordDir);//mp4格式
            //LogUtils.i(mVecordFile.getAbsolutePath());
            Log.d("Path:",mVecordFile.getAbsolutePath());
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
 
    /**
     * 初始化
     * 
     * @author lip
     * @date 2015-3-16
     * @throws IOException
     */
    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null)
            mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(AudioSource.MIC);// 音频源
        mMediaRecorder.setOutputFormat(OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setAudioEncoder(AudioEncoder.AMR_NB);// 音频格式
        mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
        // mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// 设置帧频率，然后就清晰了
        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(VideoEncoder.MPEG_4_SP);// 视频录制格式
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 开始录制视频
     * 
     * @author liuyinjun
     * @date 2015-2-5
//     * @param fileName
//     *            视频储存位置
     * @param onRecordFinishListener
     *            达到指定时间之后回调接口
     */
    public void record(final OnRecordFinishListener onRecordFinishListener) {
        this.mOnRecordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// 如果未打开摄像头，则打开
                initCamera();
            initRecord();
            mTimeCount = 0;// 时间计数器重新赋值
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
 
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mTimeCount++;
                    mProgressBar.setProgress(mTimeCount);// 设置进度条
                    if (mTimeCount == mRecordMaxTime) {// 达到指定时间，停止拍摄
                        stop();
                        if (mOnRecordFinishListener != null)
                            mOnRecordFinishListener.onRecordFinish();
                    }
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 停止拍摄
     * 
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }
 
    /**
     * 停止录制
     * 
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stopRecord() {
        mProgressBar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
 
    /**
     * 释放资源
     * 
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }
 
    public int getTimeCount() {
        return mTimeCount;
    }
 
    /**
     * @return the mVecordFile
     */
    public File getmVecordFile() {
        return mVecordFile;
    }
 
    /**
     * 录制完成回调接口
     *
     * @author lip
     * 
     * @date 2015-3-16
     */
    public interface OnRecordFinishListener {
        public void onRecordFinish();
    }
 
    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}