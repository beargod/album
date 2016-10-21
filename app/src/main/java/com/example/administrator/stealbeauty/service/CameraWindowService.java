package com.example.administrator.stealbeauty.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.administrator.stealbeauty.R;
import com.example.administrator.stealbeauty.activity.CameraActivity;
import com.example.administrator.stealbeauty.camera.CameraInterface;
import com.example.administrator.stealbeauty.camera.CameraSurfaceView;

/**
 * Created by BigGod on 2016/10/17.
 */
public class CameraWindowService extends Service implements CameraInterface.SaveCallBack{
    private WindowManager wManager;// 窗口管理者
    private WindowManager.LayoutParams mParams;// 窗口的属性
    private CameraSurfaceView myView;
    private final String TAG = "cameraservice";
    private View view;
    private View showView;
    private FrameLayout layout;
    private boolean flag = true;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "create");
        setManagerSize();
        createViews();
        super.onCreate();
    }

    /**
     * 设置悬浮框样式
     */
    public void setManagerSize() {
        // TODO Auto-generated method stub
        wManager = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;// 系统提示window
        mParams.format = PixelFormat.TRANSLUCENT;// 支持透明
        //mParams.format = PixelFormat.RGBA_8888;
        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        mParams.width = 800;//窗口的宽和高
        mParams.height = 800;
        mParams.gravity = Gravity.CENTER;
        //mParams.alpha = 0.1f;//窗口的透明度
    }

    /**
     * 创建悬浮框里的控件
     */
    public void createViews() {
        myView = new CameraSurfaceView(this);
        showView = LayoutInflater.from(this).inflate(R.layout.layout_hide, null);
        Button show = (Button) showView.findViewById(R.id.show);
        show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloat();
            }
        });
        view = LayoutInflater.from(this).inflate(R.layout.camera_window, null);
        if (view == null) {
            Log.i(TAG, "null view");
        } else {
            Log.i(TAG, "view" + view.getWidth());
        }
        Button shutBt = (Button) view.findViewById(R.id.btn_shutter);
        Button add = (Button) view.findViewById(R.id.add);
        Button delete = (Button) view.findViewById(R.id.delete);
        Button return_activity = (Button) view.findViewById(R.id.return_activity);
        final Button hide = (Button) view.findViewById(R.id.hide);
        shutBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraInterface.getInstance().doTakePicture(CameraWindowService.this);
            }
        });
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.addRate();
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.deleteRate();
            }
        });
        return_activity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraWindowService.this, CameraActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopSelf();
            }
        });
        hide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFloat();
            }
        });

    }

    /**
     * 创建悬浮框
     */
    public void createManagerWindow() {
        if (flag) {
            flag = false;
            layout = new FrameLayout(this);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            myView.setLayoutParams(lp);
            layout.addView(myView);
            layout.addView(view);
            wManager.addView(layout, mParams);
            //添加窗口
        }
    }

    /**
     * 移除悬浮框
     */
    public void removeManager() {
        if (myView.getParent() != null) {
            wManager.removeView(layout);
        }//移除窗口
    }

    private void showFloat() {
        myView = new CameraSurfaceView(this);
        layout.removeAllViews();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        myView.setLayoutParams(lp);
        layout.addView(myView);
        layout.addView(view);
        wManager.removeView(showView);
        wManager.addView(layout, mParams);
    }

    /**
     * 隐藏悬浮框
     */
    private void hideFloat() {
        wManager.removeView(layout);
        wManager.addView(showView, mParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        createManagerWindow();
        Log.i(TAG, "start");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        removeManager();
        Log.i(TAG, "destroy");
        super.onDestroy();
    }

    @Override
    public void saveOver(String info) {
        if(info!=null){
            Toast.makeText(CameraWindowService.this,info,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CameraWindowService.this,"保存失败",Toast.LENGTH_SHORT).show();
        }
    }
}
