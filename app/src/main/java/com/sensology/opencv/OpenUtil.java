package com.sensology.opencv;

import android.os.AsyncTask;

/**
 * Created by ${chenM} on 2018/12/28.
 */
public class OpenUtil {

    public static void execute(String img1,String img2,String path1,String path2,String path,final OpenCVRunListener listener){
        new AsyncTask<String,Double,Double>(){

            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            protected Double doInBackground(String... params) {
                return playVideo(params[0],params[1],params[2],params[3],params[4]);
            }

            @Override
            protected void onPostExecute(Double integer) {
                if (listener != null) {
                    listener.onEnd(integer);
                }
            }

        }.execute(img1,img2,path1,path2,path);
    }

    public static void execute(String img1,String img2,String path,final OpenCVRunListener listener){
        new AsyncTask<String,Double,Double>(){

            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            protected Double doInBackground(String... params) {
//                return mergeImage(params[0],params[1],params[2]);
                return 0.0;
            }

            @Override
            protected void onPostExecute(Double integer) {
                if (listener != null) {
                    listener.onEnd(integer);
                }
            }

        }.execute(img1,img2,path);
    }

    public interface OpenCVRunListener{
        void onStart();
        void onEnd(double result);
    }

    static {
        System.loadLibrary("OpenCV");
    }

//    private static native double playVideo(String img1,String img2,String path1,String path2,String path);

    private static native double playVideo(String img1,String img2,String path1,String path2,String path);

    private static native double mergeImage(String img1, String img2,String path);
}
