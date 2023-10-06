package com.leduongw01.mlis.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivityLoginBinding;
import com.leduongw01.mlis.utils.MyComponent;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ktSuKien();
    }
    private void ktSuKien(){
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loginProcess().equals("0")){
                    //gd
                }
            }
        });
        binding.goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private String loginProcess(){
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
        String key = "0";
        if(username.isEmpty()){
            MyComponent.ToastShort(this, "Tên người dùng hoặc email không được bỏ trống");
            return key;
        }
        if(password.isEmpty()){
            MyComponent.ToastShort(this, "Mật khẩu không được bỏ trống");
            return key;
        }
        //something
        return key;
    }
}