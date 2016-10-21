package com.example.administrator.stealbeauty.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.stealbeauty.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView showCamera;
    List<Map<String,Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCamera = (ListView)findViewById(R.id.camera_list);
        data = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.camera_item,new String[]{"time","photos"},new int[]{R.id.time,R.id.photos});
        showCamera.setAdapter(adapter);
    }
    private void initData(){

    }
}
