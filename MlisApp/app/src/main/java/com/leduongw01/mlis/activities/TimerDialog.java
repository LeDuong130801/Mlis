package com.leduongw01.mlis.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.services.ForegroundAudioService;

public class TimerDialog extends Dialog {

    Button ok, cancel;
    TextView timertext;
    SeekBar seekTimer;

    public TimerDialog(@NonNull Context context) {
        super(context);
    }

    public TimerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TimerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_timer);
        ok = findViewById(R.id.btOk);
        cancel = findViewById(R.id.btCancel);
        timertext = findViewById(R.id.timertext);
        seekTimer = findViewById(R.id.seekTimer);
        if (ForegroundAudioService.getTimer()!=-1){
            seekTimer.setProgress(ForegroundAudioService.getTimer());
            timertext.setText(ForegroundAudioService.getTimer()+" phút");
        }
        seekTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b)
                timertext.setText(i+" phút");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.setTimer(seekTimer.getProgress());
                cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
