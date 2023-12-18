package com.leduongw01.mlis.activities;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.leduongw01.mlis.MainActivity;
import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivityLoginBinding;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                try {
                    loginProcess();
                } catch (IOException e) {
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
    private String loginProcess() throws IOException {
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
        ApiService.apisService.login(username, password).enqueue(new Callback<MlisUser>() {
            @Override
            public void onResponse(Call<MlisUser> call, Response<MlisUser> response) {
                if (response.isSuccessful()){
                    BackgroundLoadDataService.mlisUser = response.body();
                    MlisUser mlisUser = response.body();
                    if (mlisUser!=null && !Objects.requireNonNull(mlisUser).get_id().equals("-1")){
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, MODE_PRIVATE);
                        sharedPreferences.edit().putString("username", mlisUser.getUsername()).commit();
                        sharedPreferences.edit().putString("token", mlisUser.getToken()).commit();
                        MyComponent.ToastShort(LoginActivity.this, "Đăng nhập thành công");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        onBackPressed();
                        return;
                    }
                    MyComponent.ToastShort(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác");
                }
            }

            @Override
            public void onFailure(Call<MlisUser> call, Throwable t) {
                MyComponent.ToastShort(LoginActivity.this, "Mất kết nối với máy chủ");
            }
        });
        if(BackgroundLoadDataService.mlisUser != null)
        return BackgroundLoadDataService.mlisUser.get_id();
        return "-1";
    }
}