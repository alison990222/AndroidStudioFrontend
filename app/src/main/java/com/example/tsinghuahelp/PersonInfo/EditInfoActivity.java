package com.example.tsinghuahelp.PersonInfo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.MainActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;
import com.example.tsinghuahelp.utils.MyDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class EditInfoActivity extends Activity implements View.OnClickListener {
    String icon_url;
    String name;
    String real_name;
    String school;
    String department;
    String grade;
    String signature;
    String person_info;
    int id;
    boolean verify;
    Bitmap bitmap;
    File send_temp;

    boolean change_icon=false;
    boolean change_name=false;
    boolean change_signature=false;
    boolean change_person_info=false;
    boolean change_verify=false;


    TextView edit_id;
    TextView edit_name;
    TextView edit_verify;
    TextView edit_signature;
    TextView edit_info;
    PopupWindow popupWindow;
    com.mikhaellopez.circularimageview.CircularImageView icon_pic;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Global.FAIL_CODE:
                    Toast.makeText(EditInfoActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case Global.FRESH_ICON_CODE:
                    Log.e("m_tag","收到信息更新图片");
                    icon_pic.setImageBitmap(bitmap);
                    break;
                case Global.SAVE_CODE:
                    Log.e("m_tag","决定是否退出");
                    if(!change_icon&&!change_name&&!change_person_info&&!change_signature&&!change_verify){
                        finish();
                    }
                    else{
                        showWarningInfo();
                    }
                    break;
                case Global.FINISH_CODE:
                    finish();
                    break;
                case Global.QUIT_CODE:
                    Intent intent = new Intent();
                    intent.setClass(EditInfoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                    startActivity(intent);
                    break;
            }
        }
    };

    private void showWarningInfo() {
        String message="您的更新尚未提交完毕！";
        final MyDialog dialog = new MyDialog(this);
        dialog.setMessage(message)
                .setTitle("提示")
                .setPositive("我知道了")
                .setNegtive("我要退出")
                .setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
                Message message=new Message();
                message.what=Global.FINISH_CODE;
                mHandler.sendMessage(message);
            }
        }).show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        Intent intent=getIntent() ;
        icon_url = intent.getStringExtra("icon_url");
        fresh_icon();
        name=intent.getStringExtra("name");
        real_name=intent.getStringExtra("real_name");
        school=intent.getStringExtra("school");
        department=intent.getStringExtra("department");
        grade=intent.getStringExtra("grade");
        signature=intent.getStringExtra("signature");
        verify=intent.getBooleanExtra("verify",false);
        id=intent.getIntExtra("id",0);
        person_info=intent.getStringExtra("person_info");

        edit_id=findViewById(R.id.id_edit);
        edit_name=findViewById(R.id.name_edit);
        edit_verify=findViewById(R.id.verify_edit);
        edit_signature=findViewById(R.id.signature_edit);
        edit_info=findViewById(R.id.info_edit);
        icon_pic=findViewById(R.id.icon_img);

        findViewById(R.id.icon_btn).setOnClickListener(this);
        findViewById(R.id.item_name).setOnClickListener(this);
        findViewById(R.id.item_info).setOnClickListener(this);
        findViewById(R.id.item_signature).setOnClickListener(this);
        findViewById(R.id.item_verify).setOnClickListener(this);
        findViewById(R.id.quit_btn).setOnClickListener(this);
        findViewById(R.id.backward_btn).setOnClickListener(this);
        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.password_btn).setOnClickListener(this);

        edit_id.setText(String.valueOf(id));
        edit_name.setText(name);
        edit_signature.setText(signature);
        edit_info.setText(person_info);
        if(!verify){edit_verify.setText("未验证");}
        else {edit_verify.setText(school+department+grade+real_name);}

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.icon_btn:
                show_popup_windows();
                break;
            case R.id.item_name:
                intent=new Intent(this,EditDetailPage.class);
                intent.putExtra("msg",0);
                intent.putExtra("str",name);
                startActivityForResult(intent,0);
                break;
            case R.id.item_verify:
                intent=new Intent(this,EditDetailPage.class);
                intent.putExtra("msg",1);
                intent.putExtra("realname",real_name);
                intent.putExtra("school",school);
                intent.putExtra("department",department);
                intent.putExtra("grade",grade);
                startActivityForResult(intent,1);
                break;
            case  R.id.item_signature:
                intent=new Intent(this,EditDetailPage.class);
                intent.putExtra("msg",2);
                intent.putExtra("str",signature);
                startActivityForResult(intent,2);
                break;
            case R.id.item_info:
                intent=new Intent(this,EditDetailPage.class);
                intent.putExtra("msg",3);
                intent.putExtra("str",person_info);
                startActivityForResult(intent,3);
                break;
            case R.id.backward_btn:
                finish();
                break;
            case R.id.quit_btn:
                logout();
                break;
            case R.id.password_btn:
                intent=new Intent(this,EditDetailPage.class);
                intent.putExtra("msg",4);
                startActivityForResult(intent,4);
                break;
            case R.id.save_btn:
                if(!change_icon&&!change_name&&!change_person_info&&!change_signature&&!change_verify){
                    finish();
                    break;
                }
                else {
                    HashMap<String, String> send_info = new HashMap<>();
                    send_info.put("username", name);
                    send_info.put("signature", signature);
                    send_info.put("personal_info", person_info);
                    send_info.put("realname", real_name);
                    send_info.put("school", school);
                    send_info.put("department", department);
                    send_info.put("grade", grade);

                    if (change_icon) {
                        update_icon();
                    }
                    if (change_name) {
                        update("update_username", send_info);
                    }
                    if (change_verify) {
                        update("verification", send_info);
                    }
                    if (change_signature) {
                        update("update_signature", send_info);
                    }
                    if (change_person_info) {
                        update("update_personal_info", send_info);
                    }
                    break;
                }
        }
    }

    public File getFile(Bitmap sendbitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sendbitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Objects.requireNonNull(EditInfoActivity.this).getExternalCacheDir(),"send_temp.jpg");
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", "转成file出错啦！！！！！！");
        }
        return file;
    }

    private void update_icon() {
        CommonInterface.sendOkHttpPostIconRequest("/api/user/update_icon", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                Message message = new Message();
                message.what = Global.FAIL_CODE;
                message.obj = e.toString();
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = JSONObject.parseObject(resStr);
                    String resp=jsonObject.getString("response");
                    if(!resp.equals("valid")){throw new Exception();}
                    change_icon=false;
                    Message message=new Message();
                    message.what=Global.FINISH_CODE;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=Global.FAIL_CODE;
                    message.obj="update_icon失败！";
                    mHandler.sendMessage(message);
                }
            }
        },send_temp);

    }

    File tempFile;
    Uri imageUri;
    public static final int TAKE_PHOTO = 4;
    public static final int CHOOSE_PHOTO = 5;


    private void show_popup_windows(){
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo =  (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView)  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow != null && popupWindow.isShowing()) {
                    tempFile = new File(Objects.requireNonNull(EditInfoActivity.this).getExternalCacheDir(), "tempImg.jpg");
                    try{
                        if(tempFile.exists()){
                            tempFile.delete();
                        }
                        tempFile.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if(Build.VERSION.SDK_INT<24){
                        imageUri= Uri.fromFile(tempFile);
                    }else{
                        imageUri= FileProvider.getUriForFile(EditInfoActivity.this,"com.example.cameraalbumtest.fileprovider",tempFile);
                    }
                    Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                    //去除选择框
                    popupWindow.dismiss();
                }
            }
        });
        //相册按钮监听
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
                //去除选择框
                popupWindow.dismiss();
            }
        });
        //取消按钮监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(EditInfoActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    name=data.getStringExtra("msg");
                    edit_name.setText(name);
                    Log.d("返回", name);
                    change_name=true;
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    real_name=data.getStringExtra("realname");
                    school=data.getStringExtra("school");
                    department=data.getStringExtra("department");
                    grade=data.getStringExtra("grade");
                    edit_verify.setText(school+department+grade+real_name);
                    change_verify=true;
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    signature=data.getStringExtra("msg");
                    edit_signature.setText(signature);
                    Log.d("返回", signature);
                    change_signature=true;
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    person_info=data.getStringExtra("msg");
                    edit_info.setText(person_info);
                    Log.d("返回", person_info);
                    change_person_info=true;
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(EditInfoActivity.this).getContentResolver().openInputStream(imageUri));
                        icon_pic.setImageBitmap(bitmap);
                        send_temp=getFile(bitmap);
                        change_icon=true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    change_icon=true;
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(EditInfoActivity.this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = Objects.requireNonNull(EditInfoActivity.this).getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            icon_pic.setImageBitmap(bitmap);
            send_temp=getFile(bitmap);
        } else {
            Toast.makeText(EditInfoActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void fresh_icon(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(icon_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("charset","UTF-8");
                    if (connection.getResponseCode()==200){
                        InputStream in = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(in);
                        Message message=new Message();
                        message.what=Global.FRESH_ICON_CODE;
                        mHandler.sendMessage(message);
                    }
                    else{
                        Message message=new Message();
                        message.what=Global.FAIL_CODE;
                        message.obj="获取头像失败！";
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.e("error", e.toString());
                    Message message = new Message();
                    message.what = Global.FAIL_CODE;
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private void update(String type,HashMap<String,String> send_info){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "/api/user/" + type;
                CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                        Message message = new Message();
                        message.what = Global.FAIL_CODE;
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            // 解析json，然后进行自己的内部逻辑处理
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            String resp = jsonObject.getString("response");
                            if (!resp.equals("valid")) {
                                throw new Exception();
                            }
                            if (type.equals("update_username")) {
                                change_name = false;
                            } else if (type.equals("verification")) {
                                change_verify = false;
                            } else if (type.equals("update_signature")) {
                                change_signature = false;
                            } else if (type.equals("update_personal_info")) {
                                change_person_info = false;
                            }
                            Message message = new Message();
                            message.what = Global.FINISH_CODE;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = Global.FAIL_CODE;
                            message.obj = url+"失败！";
                            mHandler.sendMessage(message);
                        }
                    }
                }, send_info);
            }
        }).start();
    }

    private void logout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "/api/user/logout";
                CommonInterface.sendOkHttpGetRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                        Message message = new Message();
                        message.what = Global.FAIL_CODE;
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            // 解析json，然后进行自己的内部逻辑处理
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            String resp = jsonObject.getString("response");
                            if (!resp.equals("valid")) {
                                throw new Exception();
                            } else {
                                Log.e("response", "返回了valid，成功登出了");
                                Message message = new Message();
                                message.what = Global.QUIT_CODE;
                                mHandler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = Global.FAIL_CODE;
                            message.obj="登出失败！";
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();
    }
}
