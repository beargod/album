package com.example.administrator.stealbeauty.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.example.administrator.stealbeauty.util.DisplayUtil;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "yanzi";
    Context mContext;
    SurfaceHolder mSurfaceHolder;
    private float hwRate;
    private int rate = 1;
    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Point p = DisplayUtil.getScreenMetrics(mContext);
        hwRate = ((float)p.y)/((float)p.x);
        Log.i(TAG,"hwrate"+hwRate);
        // TODO Auto-generated constructor stub
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }
    public CameraSurfaceView(Context context) {
        super(context);
        mContext = context;
        Point p = DisplayUtil.getScreenMetrics(mContext);
        hwRate = ((float)p.y)/((float)p.x);
        Log.i(TAG,"hwrate"+hwRate);
        // TODO Auto-generated constructor stub
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        setSurfaceViewSize();
        CameraInterface.getInstance().doOpenCamera();
        CameraInterface.getInstance().doStartPreview(holder);
        Log.i(TAG, "surfaceCreated...");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Log.i(TAG, "surfaceChanged...");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.i(TAG, "surfaceDestroyed...");
        CameraInterface.getInstance().doStopCamera(holder,this);
    }
    public void addRate(){
        rate++;
        setSurfaceViewSize();
    }
    public void deleteRate(){
        rate--;
        setSurfaceViewSize();
    }
    private void setSurfaceViewSize(){
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = (100*rate);
        params.height =(int)(100*rate*hwRate);
        //默认全屏的比例预览
        this.setLayoutParams(params);

    }
}
