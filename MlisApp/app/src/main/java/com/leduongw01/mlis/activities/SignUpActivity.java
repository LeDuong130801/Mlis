package com.leduongw01.mlis.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivitySignUpBinding;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.utils.MyComponent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    final Calendar myCalendar= Calendar.getInstance();
    final String[] genderList = new String[]{"Nam", "Nữ", "Khác"};
    int genderChecked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        xoaDl();
        ktSuKien();
    }
    private void xoaDl(){
        binding.etEmail.setText("");
        binding.etPassword1.setText("");
        binding.etPassword2.setText("");
        binding.etUsername.setText("");
        String myFormat="MM/dd/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.etDateOfBirth.setText(dateFormat.format(new Date()));
        genderChecked = 0;
        binding.etGender.setText(R.string.nam);
    }
    private void ktSuKien(){
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };
        binding.etDateOfBirth.setOnClickListener(view -> new DatePickerDialog(SignUpActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.gioitinh);
        binding.etGender.setOnClickListener(view -> {b.setSingleChoiceItems(genderList, genderChecked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position){
                    case 0:{
                        binding.etGender.setText(R.string.nam);
                        genderChecked = 0;
                        break;
                    }
                    case 1:{
                        binding.etGender.setText(R.string.nu);
                        genderChecked = 1;
                        break;
                    }
                    case 2:{
                        binding.etGender.setText(R.string.khac);
                        genderChecked = 2;
                        break;
                    }
                    default:{
                        binding.etGender.setText(R.string.vuilongchongioitinh);
                        genderChecked = -1;
                    }
                }
                dialog.dismiss();
            }
        });
            b.show();
        });
        binding.goLogin.setOnClickListener(view -> {
            this.finishActivity(10000);
            this.finish();
        });
        binding.btSignUp.setOnClickListener(view -> {
            signUpProcess();
        });
    }
    private void signUpProcess(){
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword1.getText().toString();
        String password2 = binding.etPassword2.getText().toString();
        String gender = binding.etGender.getText().toString();
        String dateOfBirth = binding.etDateOfBirth.getText().toString();
        String key = "0";
        if(username.isEmpty() && email.isEmpty()){
            MyComponent.ToastShort(this, "Tên người dùng hoặc email không được bỏ trống");
            return;
        }
        if(password.isEmpty()){
            MyComponent.ToastShort(this, "Mật khẩu không được bỏ trống");
            return;
        }
        if (password2.isEmpty()){
            MyComponent.ToastShort(this, "Vui lòng nhập lại mật khẩu");
            return;
        }
        if(!password.equals(password2)){
            MyComponent.ToastShort(this, "Mật khẩu không khớp");
            return;
        }
        MlisUser mlisUser = new MlisUser("0", username, password, email, "", "1", "a", dateOfBirth, gender);
        final String[] kq = {"0"};
        ApiService.apisService.register(mlisUser).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    MyComponent.ToastShort(SignUpActivity.this, "Đăng ký thành công");
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void updateLabel(){
        String myFormat="MM/dd/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.etDateOfBirth.setText(dateFormat.format(myCalendar.getTime()));
    }
}