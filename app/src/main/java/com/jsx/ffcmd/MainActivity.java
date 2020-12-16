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

    private int resultNow, resultMax;//当前进度，总进度

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
//                Log.d(TAG, "ffTest: "+log);

                int startIndex = log.indexOf("duration:");
                int nowIndex = log.indexOf("time=");
                int endIndex = log.indexOf("bitrate");

                if (startIndex > 0 && endIndex > 0) {
                    resultMax = Integer.valueOf(log.substring(startIndex + 9, endIndex).trim().split("\\.")[0]);
                }

                if (nowIndex > 0 && endIndex > 0) {
                    String[] nows = log.substring(nowIndex + 5, endIndex).split(":");
                    resultNow = Integer.valueOf(nows[0]) * 60 * 60 + Integer.valueOf(nows[1]) * 60 + Integer.valueOf(nows[2].split("\\.")[0]);
//                    onResult.progress(resultNow, resultMax);
                    Log.d(TAG, "ffTest: progress:"+resultNow+" "+resultMax);
                }
            });
            runOnUiThread(()->Toast.makeText(MainActivity.this, "转码成功", Toast.LENGTH_LONG).show());
        }).start();
    }
}