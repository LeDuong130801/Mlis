package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivityAccountInfoBinding;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

public class AccountInfoActivity extends AppCompatActivity {
    ActivityAccountInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_info);
        if (BackgroundLoadDataService.getInstance().checkAuthen()){
            MlisUser mlisUser = BackgroundLoadDataService.mlisUser;
            binding.tvEmail.setText(String.format("Email: %s", mlisUser.getEmail()));
            binding.tvDateOfBirth.setText(String.format("Ngày sinh: %s", mlisUser.getDateOfBirth()));
            binding.tvGender.setText(String.format("Giới tính: %s", mlisUser.getGender()));
            binding.tvUsername.setText(String.format("Tên người dùng: %s", mlisUser.getUsername()));
            binding.tvStatus.setText(mlisUser.getStatus().equals("1")? "Trạng thái: Kích hoạt": "Trạng thái: Ngưng kích hoạt");
        }

    }
}