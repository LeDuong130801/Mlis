package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AccountOptionAdapter;
import com.leduongw01.mlis.databinding.ActivityAccountBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.StringValue;

import java.util.ArrayList;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ArrayList<StringValue> accStrv = new ArrayList<>();
        accStrv.add(new StringValue("0", "Đổi mật khẩu", ""));
        accStrv.add(new StringValue("1", "Thông tin người dùng", "Xem thông tin của bạn"));
        binding.rcvAccount.setAdapter(new AccountOptionAdapter(AccountActivity.this, accStrv, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                switch (position){
                    case 0:{
                        Log.i("Change pass", "user want change their password");
                        break;
                    }
                    case 1:{
                        Log.i("Show info", "user want watch their info");
                        break;
                    }
                    default:{
                        Log.i("E", "Có lỗi ở accountOptionAdapter");
                    }
                }
            }
        }));
        binding.rcvAccount.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}