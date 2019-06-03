package com.sensology.opencv;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sensology.opencv.util.SystemFunUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mImg,mVideo;
    private SystemFunUtil util;
    private List<String> mListImg = new ArrayList<>();
    private List<String> mList = new ArrayList<>();
    private String savePath;
    private ProgressDialog progressDialog;
    private String rootPath = Environment.getExternalStorageDirectory().toString();
    private int index = 1;
    private String imgPath = "";
    private String LEFT = "left";
    private String RIGHT = "right";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImg = findViewById(R.id.img);
        mVideo = findViewById(R.id.video);

        util = new SystemFunUtil(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        imgPath = rootPath+"/DCIM/Camera/opencv/%s/%d.jpg";
        showTv(mImg,String.format("index:%d",index));
        savePath = rootPath+"/DCIM/Camera/opencv";
        showTv(mVideo,savePath);
    }

    public void onImage(View view) {
//        util.openCamera(SystemFunUtil.SYSTEM_SELECT_IMAGE,2);
        index++;
        showTv(mImg,String.format("index:%d",index));
    }

    public void onVideo(View view) {
        util.openCamera(SystemFunUtil.SYSTEM_IMAGE_PHONE,1);
    }

    public void onRun(View view) {
//        if (mList.size()>1){
//            savePath = FileUtil.getFolderName(mList.get(0));

            OpenUtil.execute(
//                    mListImg.get(0),
//                    mListImg.get(1),
                    String.format(imgPath,LEFT,index),
                    String.format(imgPath,RIGHT,index),

                    rootPath+"/DCIM/Camera/opencv/left/",
                    rootPath+"/DCIM/Camera/opencv/right/",

//                    mList.get(0),
//                    "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
//                    "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov",
//                    "rtsp://192.168.1.254/xxx.mov",
//                    rootPath+"/DCIM/Camera/opencv/cut/",
//                    mList.get(1),
                    savePath, new OpenUtil.OpenCVRunListener() {
                        @Override
                        public void onStart() {
                            progressDialog.show();
                        }

                        @Override
                        public void onEnd(double result) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                        }
                    });

//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode== Activity.RESULT_OK){
            if (requestCode == 1){
                Uri uriVideo = data.getData();
                String path = util.getImageAbsolutePath(uriVideo);
                if (TextUtils.isEmpty(path)){
                    return;
                }
                mList.add(path);
                showTv(mVideo,path);
            }
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
//        sb.append(tv.getText().toString());
//        sb.append("\n");
        sb.append(txt);
        tv.setText(sb.toString());
    }


    public void onMerge(View view) {
        Intent intent = new Intent(this,MergeActivity.class);
        startActivity(intent);
    }
}
