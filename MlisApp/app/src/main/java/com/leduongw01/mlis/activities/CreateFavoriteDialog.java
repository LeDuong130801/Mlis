package com.leduongw01.mlis.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.MyComponent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFavoriteDialog extends Dialog {
    Context context;
    public CreateFavoriteDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_favorite);
        EditText editText = findViewById(R.id.etFavoriteName);
        editText.setText("");
        ImageButton button = findViewById(R.id.btDone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().trim().equals("")){
                    Favorite favorite = new Favorite(editText.getText().toString().trim(), BackgroundLoadDataService.mlisUser.get_id());
                    ApiService.apisService.createFavorite(favorite).enqueue(new Callback<Favorite>() {
                        @Override
                        public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                            if (response.isSuccessful()){
                                BackgroundLoadDataService.getAllFavorite().add(response.body());
                                dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Favorite> call, Throwable t) {
                            MyComponent.ToastShort(context, "Có lỗi xuất hiện");
                        }
                    });
                }
            }
        });
    }
}
