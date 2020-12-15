package com.jsx.ffcmd;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jsx.libffmpegcmd.FFmpegCmd;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String in = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ffcmd_test.avi";
    private String out = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ffcmd_test.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view){
        String[] strings = new String[]{"ffmpeg", "-i", in, "-c:v", "copy", "-c:a", "aac", out};
        ffTest(strings);
    }

    private void ffTest(String[] cmd){
        new Thread(()->{
            int result = FFmpegCmd.ffmpegRunPro(cmd, log -> {
                Log.d(TAG, "ffTest: "+log);
            });
            runOnUiThread(()->Toast.makeText(MainActivity.this, "转码成功", Toast.LENGTH_LONG).show());
        }).start();
    }
}