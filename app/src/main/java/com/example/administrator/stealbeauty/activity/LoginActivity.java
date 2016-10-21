package com.example.administrator.stealbeauty.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.stealbeauty.R;
import com.example.administrator.stealbeauty.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setEventHandlers(new EventHandlers());
    }
    private void login(){
        Intent intent = new Intent(LoginActivity.this,MineActivity.class);
        startActivity(intent);
    }
    public class EventHandlers{
        public void loginOnClick(View view){
            String account = binding.account.getText().toString();
            String password = binding.password.getText().toString();
            if(account.isEmpty()){
                Toast.makeText(view.getContext(),"请输入账号",Toast.LENGTH_SHORT).show();
            }
            else if(password.isEmpty()){
                Toast.makeText(view.getContext(),"请输入密码",Toast.LENGTH_SHORT).show();
            }else if(!account.equals("root")||!password.equals("root")){
                Toast.makeText(view.getContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
            }else{
                login();
            }
        }
        public void findOnClick(View view){
            Intent intent = new Intent(LoginActivity.this,FindPasswordActivity.class);
            startActivity(intent);
        }
        public void registerOnClick(View view){
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    }
}
