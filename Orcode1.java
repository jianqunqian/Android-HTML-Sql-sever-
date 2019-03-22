package com.makeqianjianqun.artfere.trivial;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeqianjianqun.artfere.R;
import com.makeqianjianqun.artfere.zxing.activity.CaptureActivity;
import com.makeqianjianqun.artfere.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by 19212 on 2019/2/20.
 */

public class Orcode1 extends Activity{
    private ImageButton btnscan;
    private EditText edittext;
    //private ImageView imagetext;
    //private Button btnmakeorcode;
    private ImageView getorcodetext;
    //private TextView gettext;
    private Handler handler = new Handler();
    private EditText editcargo;
    private EditText edituser;
    private EditText editpassword;
    private EditText edittime;
    private EditText editaddress;
    private EditText editremark;
    private Button btnsubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oror);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        btnscan=(ImageButton)findViewById(R.id.btnscan);
        //edittext=(EditText)findViewById(R.id.edittext);
        //imagetext=(ImageView)findViewById(R.id.imagetext);
       // btnmakeorcode=(Button)findViewById(R.id.btnmakeorcode);
       // getorcodetext=(ImageView)findViewById(R.id.getorcodetext);
        editcargo=(EditText)findViewById(R.id.editcargo);
        edituser=(EditText)findViewById(R.id.edituser);
        editpassword=(EditText)findViewById(R.id.editpassword);
        edittime=(EditText)findViewById(R.id.edittime);
        editaddress=(EditText)findViewById(R.id.editaddress);
        editremark=(EditText)findViewById(R.id.editremark);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int mon=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);
        int sed=calendar.get(Calendar.SECOND);
        String h=check(hour);
        String m=check(min);
        String s=check(sed);
        edittime.setText(year+"-"+mon+"-"+day+" "+hour+":"+min+":"+sed);
        getaddress();
        final Handler han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x123){
                    Toast.makeText(Orcode1.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cargoID="cargoID="+editcargo.getText().toString();
                String userID="&userID="+edituser.getText().toString();
                String password="&password="+editpassword.getText().toString();
                String time="&time="+edittime.getText().toString();
                String address="&address="+editaddress.getText().toString();
                String remark="&remark="+editremark.getText().toString();
                String comm=cargoID+userID+password+time+address+remark;
                new Thread(new AccessNetwork("POST","http://wmsceshi.picp.io:51006/Handler1.ashx",comm,han)).start();
            }
        });

    }
    class AccessNetwork implements Runnable{
        private String op ;
        private String url;
        private String params;
        private Handler h;

        public AccessNetwork(String op, String url, String params,Handler h) {
            super();
            this.op = op;
            this.url = url;
            this.params = params;
            this.h = h;
        }
        @Override
        public void run() {
            Message m = new Message();
            m.what = 0x123;
            m.obj = PostUtil.sendPost(url,params);
            h.sendMessage(m);
        }
    }
    private String check(int i){
        String j=new String();
        if (i < 10) {
            j="0"+i;     //当秒分小于10时，在左边补0；
        }
        return j;
    }
    /*class PostThread extends Thread{
        String cargo,user,password,time,address;
        public PostThread(String cargo,String user,String password,String time,String address){
            this.cargo=cargo;
            this.user=user;
            this.password=password;
            this.time=time;
            this.address=address;
        }

        @Override
        public void run() {

        }
    }*/
    private void getaddress(){

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        //设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);



        if (providerName != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                return;
            }

            Location location = lm.getLastKnownLocation(providerName);

            //获取维度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();

            editaddress.setText("经度"+longitude+" 纬度"+latitude);

        } else {
            Toast.makeText(this, "请打开我的位置", Toast.LENGTH_SHORT).show();
        }

    }

    //发送数据

    /**
     * 打开二维码扫描
     */
    private void open() {

        if(Build.VERSION.SDK_INT>=23){
            //检查是否已经给了权限
            int checkpermission= ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA);
            if(checkpermission!= PackageManager.PERMISSION_GRANTED){//没有给权限
                Log.e("permission","动态申请");
                //参数分别是当前活动，权限字符串数组，requestcode
                ActivityCompat.requestPermissions(Orcode1.this,new String[]{Manifest.permission.CAMERA}, 1);
            }
            else{
                config();
                startActivityForResult(new Intent(Orcode1.this,
                        CaptureActivity.class), 0);
            }
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults数组与权限字符串数组对应，里面存放权限申请结果
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Orcode1.this, "已授权", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Orcode1.this, "拒绝授权", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Orcode1.this, "授权", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Orcode1.this, "拒绝授权", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 创建二维码并将图片保存在本地
     */
    private void create() {
        int width = DensityUtil.dip2px(this, 200);
        Bitmap bitmap = EncodingUtils.createQRCode(edittext.getText().toString(),
                width, width, BitmapFactory.decodeResource(getResources(),
                        R.drawable.headportrait));
        getorcodetext.setImageBitmap(bitmap);
        saveBitmap(bitmap);
    }

    /**
     * 将Bitmap保存在本地
     *
     * @param bitmap
     */
    public void saveBitmap(Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "zxing_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "zxing_image" + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + "/sdcard/namecard/")));
    }

    /**
     * 提高屏幕亮度
     */
    private void config() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            editcargo.setText(result);
        }
    }
}
