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
 * ��Ƶ¼�ƿؼ�
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
    private Timer mTimer;// ��ʱ��
    private OnRecordFinishListener mOnRecordFinishListener;// ¼����ɻص��ӿ�
 
    private int mWidth;// ��Ƶ�ֱ��ʿ��
    private int mHeight;// ��Ƶ�ֱ��ʸ߶�
    private boolean isOpenCamera;// �Ƿ�һ��ʼ�ʹ�����ͷ
    private int mRecordMaxTime;// һ�������ʱ��
    private int mTimeCount;// ʱ�����
    private File mVecordFile = null;// �ļ�
 
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
        mProgressBar.setMax(mRecordMaxTime);// ���ý����������
 
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
     * ��ʼ������ͷ
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
     * ��������ͷΪ����
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
     * ��Google�ٷ���Cameraʾ�������п��Կ�����ѡ��Ԥ���ߴ�ı�׼��
     * ��1������ͷ֧�ֵ�Ԥ���ߴ�Ŀ�߱���SurfaceView�Ŀ�߱ȵľ��Բ�ֵС��0.1��
     * ��2���ڣ�1����õĳߴ��У�ѡȡ��SurfaceView�ĸߵĲ�ֵ��С�ġ�
     * ͨ���������������׼������ʵ�֣�������һ�¹ٷ��Ĵ��룺
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
     * �ͷ�����ͷ��Դ
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
        // �����ļ�
        try {
            mVecordFile = File.createTempFile("recording", ".mp4", vecordDir);//mp4��ʽ
            //LogUtils.i(mVecordFile.getAbsolutePath());
            Log.d("Path:",mVecordFile.getAbsolutePath());
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
 
    /**
     * ��ʼ��
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
        mMediaRecorder.setVideoSource(VideoSource.CAMERA);// ��ƵԴ
        mMediaRecorder.setAudioSource(AudioSource.MIC);// ��ƵԴ
        mMediaRecorder.setOutputFormat(OutputFormat.MPEG_4);// ��Ƶ�����ʽ
        mMediaRecorder.setAudioEncoder(AudioEncoder.AMR_NB);// ��Ƶ��ʽ
        mMediaRecorder.setVideoSize(mWidth, mHeight);// ���÷ֱ��ʣ�
        // mMediaRecorder.setVideoFrameRate(16);// ����Ұ���ȥ���ˣ��о�ûʲô��
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// ����֡Ƶ�ʣ�Ȼ���������
        mMediaRecorder.setOrientationHint(90);// �����ת90�ȣ���������¼��
        mMediaRecorder.setVideoEncoder(VideoEncoder.MPEG_4_SP);// ��Ƶ¼�Ƹ�ʽ
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
     * ��ʼ¼����Ƶ
     * 
     * @author liuyinjun
     * @date 2015-2-5
//     * @param fileName
//     *            ��Ƶ����λ��
     * @param onRecordFinishListener
     *            �ﵽָ��ʱ��֮��ص��ӿ�
     */
    public void record(final OnRecordFinishListener onRecordFinishListener) {
        this.mOnRecordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// ���δ������ͷ�����
                initCamera();
            initRecord();
            mTimeCount = 0;// ʱ����������¸�ֵ
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
 
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mTimeCount++;
                    mProgressBar.setProgress(mTimeCount);// ���ý�����
                    if (mTimeCount == mRecordMaxTime) {// �ﵽָ��ʱ�䣬ֹͣ����
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
     * ֹͣ����
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
     * ֹͣ¼��
     * 
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stopRecord() {
        mProgressBar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            // ���ú󲻻��
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
     * �ͷ���Դ
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
     * ¼����ɻص��ӿ�
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