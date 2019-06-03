package com.sensology.opencv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sensology.opencv.util.FileUtil;
import com.sensology.opencv.util.SystemFunUtil;

import java.util.ArrayList;
import java.util.List;

public class MergeActivity extends AppCompatActivity {
    private TextView mImg;
    private SystemFunUtil util;
    private List<String> mListImg = new ArrayList<>();
    private String savePath;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);

        mImg = findViewById(R.id.img);

        util = new SystemFunUtil(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    public void onImage(View view) {
        util.openCamera(SystemFunUtil.SYSTEM_SELECT_IMAGE,2);
    }

    public void onRun(View view) {
        if (mListImg.size()>1){
            savePath = FileUtil.getFolderName(mListImg.get(0));

            OpenUtil.execute(
                    mListImg.get(0),
                    mListImg.get(1),
                    savePath, new OpenUtil.OpenCVRunListener() {
                        @Override
                        public void onStart() {
                            progressDialog.show();
                        }

                        @Override
                        public void onEnd(double result) {
                            progressDialog.dismiss();
                            Toast.makeText(MergeActivity.this,"success",Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode== Activity.RESULT_OK){
            if (requestCode == 2){
                Uri uriVideo = data.getData();
                String path = util.getImageAbsolutePath(uriVideo);
                if (TextUtils.isEmpty(path)){
                    return;
                }
                mListImg.add(path);
                showTv(mImg,path);
            }
        }

    }

    private void showTv(TextView tv,String txt){
        StringBuilder sb = new StringBuilder();
        sb.append(tv.getText().toString());
        sb.append("\n");
        sb.append(txt);
        tv.setText(sb.toString());
    }
}
