package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivityChangePasswordBinding;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.MyComponent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        binding.newpass1.setText("");
        binding.newpass2.setText("");
        binding.oldpass.setText("");
        binding.btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.newpass1.getText().toString().trim().equals("")){
                    MyComponent.ToastShort(ChangePasswordActivity.this, "Vui lòng nhập mật khẩu mới");
                }
                if (binding.newpass1.getText().toString().equals(binding.newpass2.getText().toString())){
                    StringValue stringValue = new StringValue(
                            BackgroundLoadDataService.mlisUser.getUsername(),
                            binding.oldpass.getText().toString(),
                            binding.newpass1.getText().toString()
                    );
                    ApiService.apisService.changePass(stringValue).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()){
                                String body = response.body() == null ? "none" : response.body();
                                if (body.equals("none")){
                                    MyComponent.ToastShort(ChangePasswordActivity.this, "Có lỗi xảy ra, tài khoản bị khóa");
                                }
                                else if(body.equals("empty")){
                                    MyComponent.ToastShort(ChangePasswordActivity.this, "Vui lòng nhập mật khẩu mới");
                                }
                                else if(body.equals("notMatch")){
                                    MyComponent.ToastShort(ChangePasswordActivity.this, "Mật khẩu cũ không khớp");
                                }
                                else if(body.equals("notFound")){
                                    MyComponent.ToastShort(ChangePasswordActivity.this, "Có lỗi xảy ra, tài khoản không tồn tại");
                                }
                                else{
                                    MyComponent.setStringRef(ChangePasswordActivity.this, "token", body);
                                    MyComponent.ToastShort(ChangePasswordActivity.this, "Đổi mật khẩu thành công");
                                    Intent i = new Intent(ChangePasswordActivity.this, HomeScreen.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
                else{
                    MyComponent.ToastShort(ChangePasswordActivity.this, "Mật khẩu mới không khớp nhau");
                }
            }
        });
    }
}