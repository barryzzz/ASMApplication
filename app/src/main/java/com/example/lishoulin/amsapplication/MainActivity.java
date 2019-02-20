package com.example.lishoulin.amsapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView mTextView;

    @TimeRecord
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        TimeUtil.getInstance().recordEnter("xxx");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.testbtn);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "test onclick");

            }
        });

        findViewById(R.id.textView).setOnClickListener(this);
        findViewById(R.id.textView2).setOnClickListener(this);

//        TimeUtil.getInstance().recordExit("xxx");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @TimeRecord
    @Override
    public void onClick(View v) {
//        TimeUtil.getInstance().recordEnter("xxx");
        switch (v.getId()) {
            case R.id.textView:
                Log.e(TAG, "textView onclick");
                break;
            case R.id.textView2:
                Log.e(TAG, "textView2 onclick");
                break;
            default:
                break;
        }
//        TimeUtil.getInstance().recordEnter("xxx");

    }

}
