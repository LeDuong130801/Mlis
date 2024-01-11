package com.leduongw01.mlis.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.utils.MyComponent;
import com.leduongw01.mlis.utils.MyConfig;

public class IpSettingDialog extends Dialog {
    EditText etIp;
    ImageButton ibDone;
    Context context;
    public IpSettingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ipsetting);
        etIp = findViewById(R.id.etIpServer);
        ibDone = findViewById(R.id.ibtDone);
        etIp.setText(MyConfig.serverAddress);
        ibDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyComponent.setStringRef(context, "servername", etIp.getText().toString());
                MyConfig.serverAddress = etIp.getText().toString();
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
