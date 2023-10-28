package com.leduongw01.mlis.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;

import com.leduongw01.mlis.R;

public class LoadingDialog{
    private Activity activity;
    private AlertDialog dialog;
    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }
    void dismiss(){
        dialog.dismiss();
    }
}