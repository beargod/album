package com.example.administrator.stealbeauty.camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.os.Handler;

import com.example.administrator.stealbeauty.util.CamParaUtil;
import com.example.administrator.stealbeauty.util.FileUtil;
import com.example.administrator.stealbeauty.util.ImageUtil;

import java.io.IOException;
import java.util.List;


/**
 * Created by BigGod on 2016/10/17.
 */
public class CameraInterface {
    private Camera mCamera;
    private Camera.Parameters mParams;
    private SaveCallBack saveCallBack;
    private int maxPicSize;
    private int maxPreSize;
    private boolean isPreviewing = false;
    private static CameraInterface mCameraInterface;


    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }
    /**
     * 保存成功回调函数
     */
    public interface SaveCallBack{
        void saveOver(String info);
    }
    /**
     * 打开Camera
     *
     * @param
     */
    public void doOpenCamera() {
        if(mCamera==null) {
            mCamera = Camera.open();
            maxPicSize = getBestSize(mCamera.getParameters().getSupportedPictureSizes());
            maxPreSize = getBestSize(mCamera.getParameters().getSupportedPreviewSizes());
        }
    }
    public int getBestSize(List<Size> list){
        int best = 0;
        int size = 0;
        for(int i=0;i<list.size();i++){
            int newSize = list.get(i).width*list.get(i).height;
            if(newSize>size){
                size = newSize;
                best = i;
            }
        }
        return best;
    }
    /**
     * 开启预览
     *
     * @param holder
     *
     */
    public void doStartPreview(SurfaceHolder holder) {
        if (isPreviewing) {
            return;
        }
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            CamParaUtil.getInstance().printSupportPictureSize(mParams);
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            resetPictureSize();
            resetPreviewSize();
            //设置PreviewSize和PictureSize
            //Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
            //         mParams.getSupportedPictureSizes(),previewRate, 800);
            // Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
            // mParams.getSupportedPreviewSizes(), previewRate, 800);

            mCamera.setDisplayOrientation(90);
            CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-picture")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//开启预览
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            isPreviewing = true;
            mParams = mCamera.getParameters(); //重新get一次
        }
    }

    /**
     * 重新照片分辨率
     */
    public void resetPictureSize() {
        Size pis = mParams.getSupportedPictureSizes().get(maxPicSize);
        mParams.setPictureSize(pis.width, pis.height);
        mCamera.setParameters(mParams);
    }

    /**
     * 重新显示的大小
     */
    public void resetPreviewSize() {
        Size pre = mParams.getSupportedPreviewSizes().get(maxPreSize);
        mParams.setPreviewSize(pre.width, pre.height);
        mCamera.setParameters(mParams);
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera(SurfaceHolder holder,SurfaceHolder.Callback callback) {
        if (null != mCamera) {
            holder.removeCallback(callback);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void doTakePicture(SaveCallBack saveCallBack) {
        if (isPreviewing && (mCamera != null)) {
            this.saveCallBack = saveCallBack;
            long time = System.currentTimeMillis();
            mCamera.takePicture(null, null, mJpegPictureCallback);
            Log.i("showTime",""+(System.currentTimeMillis()-time));
        }
    }
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Handler handler = new Handler();
            final byte[] datas = data;
            mCamera.startPreview();
            if (null != data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap b= BitmapFactory.decodeByteArray(datas, 0, datas.length);//data是字节数据，将其解析成位图
                        isPreviewing = false;
                        if (null != b) {
                            //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                            //图片竟然不能旋转了，故这里要旋转下
                            final Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                            saveCallBack.saveOver(FileUtil.saveBitmap(rotaBitmap));
                        }
                        //再次进入预览
                        isPreviewing = true;
                    }
                });

            }
            //保存图片到sdcard
        }
    };
}
