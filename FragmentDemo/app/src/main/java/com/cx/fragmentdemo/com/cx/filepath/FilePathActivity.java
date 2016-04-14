package com.cx.fragmentdemo.com.cx.filepath;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cx.fragmentdemo.R;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by chenxin on 16/4/14.
 */
public class FilePathActivity extends Activity {
    private TextView tv_info;
    private String basePath = "";
    private String filePath = "";

    private EditText et_time;
    private EditText et_distance;

    private LocationManager locationManager;

    private long min_time = 3 * 1000;
    private float min_distance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.file_path_act);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                try {
//                    for (int i = 0; i < 10; i++) {
//                        str += i + "   lng: " + 1.23 + "    lat: " + 3.45 + "  time: " + getCurrentDate() + "\r\n";
//                        writeToFile(filePath, str);
//                    }
//
//                    Toast.makeText(FilePathActivity.this, "complete", Toast.LENGTH_SHORT).show();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }

                try {
                    textStr += "time: " + getCurrentDate() + " start " + "\r\n";
                    writeToFile(filePath,textStr );
                }catch (Exception e) {
                    e.printStackTrace();
                }

                initLoc();

            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    textStr += "time: " + getCurrentDate() + " stop";
                    writeToFile(filePath,textStr );
                    String text = readToFile(filePath);
                    tv_info.setText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        tv_info = (TextView) findViewById(R.id.tv_info);
        et_time = (EditText) findViewById(R.id.et_time);
        et_distance = (EditText) findViewById(R.id.et_distance);

        getFilePath();

    }

    private void initLoc() {
        if(!TextUtils.isEmpty(et_time.getText().toString())) {
            min_time = Long.valueOf(et_time.getText().toString());

            try {
                textStr += "min_time: " + min_time + "\r\n";
                writeToFile(filePath,textStr );
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(!TextUtils.isEmpty(et_distance.getText().toString())) {
            min_distance = Long.valueOf(et_distance.getText().toString());

            try {
                textStr += "min_distance: " + min_distance + "\r\n";
                writeToFile(filePath,textStr );
            }catch (Exception e) {
                e.printStackTrace();
            }

        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.addGpsStatusListener(statusListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                min_time,
                min_distance,
                listener);
    }

    private String textStr;
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                textStr += "time: " + getCurrentDate() + "  lat: " + location.getLatitude() + "  lng: " + location.getLongitude() + "\r\n";
                writeToFile(filePath, textStr);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void getFilePath() {

        // /data/data/com.cx.fragmentdemo/files
        Log.d("cx", "getFilesDir().getPath(): " + getFilesDir().getPath());

        // /data/data/com.cx.fragmentdemo/cache
        Log.d("cx", "getCacheDir().getPath(): " + getCacheDir().getPath());

        ///storage/emulated/0/Android/data/com.cx.fragmentdemo/files
        Log.d("cx", "getExternalFilesDir(null).getPath(): " + getExternalFilesDir(null).getPath());

        // /storage/emulated/0
        Log.d("cx", "Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());

        // mounted--有外置存储卡
        Log.d("cx", "Environment.getExternalStorageState(): " + Environment.getExternalStorageState());

        // /storage/emulated/0/Android/data/com.cx.fragmentdemo/cache
        Log.d("cx", "getExternalCacheDir().getPath(): " + getExternalCacheDir().getPath());

        // /storage/emulated/0/Android/data/com.cx.fragmentdemo/cache
        Log.d("cx", "getExternalCacheDir(): " + getExternalCacheDir());


        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 有存储卡
            basePath = Environment.getExternalStorageDirectory().getPath();
        } else {
            basePath = getFilesDir().getAbsolutePath();
        }

        filePath = basePath + "/" + "cx-location";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void writeToFile(String fileName, String writeStr) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        byte[] bytes = writeStr.getBytes();
        out.write(bytes);
        out.close();
    }

    private String readToFile(String fileName) throws IOException {

        String res = "";
        FileInputStream in = new FileInputStream(fileName);
        int length = in.available();

        byte[] buffer = new byte[length];
        in.read(buffer);

        res = EncodingUtils.getString(buffer, "UTF-8");
        in.close();

        return res;
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Toast.makeText(FilePathActivity.this, "第一次定位", Toast.LENGTH_SHORT).show();

                    try {
                        textStr += "time: " + getCurrentDate() + " 第一次定位" + "\r\n";
                        writeToFile(filePath, textStr);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                    Toast.makeText(FilePathActivity.this, "卫星状态改变", Toast.LENGTH_SHORT).show();

                    //获取当前状态
                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }

//                    try {
//                        textStr += "time: " + getCurrentDate() + " 搜索到：" + count + " 颗卫星" + "\r\n";
//                        writeToFile(filePath, textStr);
//                    }catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;

                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Toast.makeText(FilePathActivity.this, "定位启动", Toast.LENGTH_SHORT).show();
                    try {
                        textStr += "time: " + getCurrentDate() + " 定位启动" + "\r\n";
                        writeToFile(filePath, textStr);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Toast.makeText(FilePathActivity.this, "定位结束", Toast.LENGTH_SHORT).show();

                    try {
                        textStr += "time: " + getCurrentDate() + " 定位结束" + "\r\n";
                        writeToFile(filePath, textStr);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        locationManager.removeGpsStatusListener(statusListener);
        locationManager.removeUpdates(listener);
        super.onDestroy();
    }
}

