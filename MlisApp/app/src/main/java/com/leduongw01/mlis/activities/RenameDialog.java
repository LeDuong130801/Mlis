package com.leduongw01.mlis.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RenameDialog extends Dialog {
    String favoriteId;
    String currentName;
    EditText et;
    public RenameDialog(@NonNull Context context, String id, String currentName) {
        super(context);
        this.favoriteId = id;
        this.currentName = currentName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_changename);
        ktSuKien();
    }

    private void ktSuKien() {
        et = findViewById(R.id.etFavoriteName);
        et.setText(currentName);
        ImageButton imageButton = findViewById(R.id.ibtDone);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newname = et.getText().toString();
                if (!newname.trim().equals("")) {
                    ApiService.apisService.renameFavorite(favoriteId, newname).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    for (int i = 0; i < BackgroundLoadDataService.getAllFavorite().size(); i++) {
                        if (BackgroundLoadDataService.getAllFavorite().get(i).get_id().equals(favoriteId)) {
                            BackgroundLoadDataService.getAllFavorite().get(i).setName(newname);
                            break;
                        }
                    }
                    RenameDialog.this.dismiss();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
