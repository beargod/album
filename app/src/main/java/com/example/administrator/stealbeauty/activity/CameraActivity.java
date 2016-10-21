package com.example.administrator.stealbeauty.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.administrator.stealbeauty.R;
import com.example.administrator.stealbeauty.service.CameraWindowService;

public class CameraActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_shutter:
                Intent intent = new Intent();
                intent.setClass(this, CameraWindowService.class);
                startService(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
