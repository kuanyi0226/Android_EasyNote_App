package com.example.easynote.other_activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.easynote.R;
import com.example.easynote.database.NotesDB;
import com.example.easynote.tools.TimeManage;
import com.example.easynote.tools.file.PermisionUtils;

import java.io.File;

public class AddContent extends AppCompatActivity implements View.OnClickListener {
    public final static String FILE_SAVE_PATH = "/Internal storage/DCIM/Camera/EasyNote/";

    private String whatValue;
    private Button saveButton, cancelButton;
    private EditText ettext;
    private ImageView c_img;
    private VideoView v_video;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;


    //图文所需
    private File imgFile;
    //视频所需
    private File videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 解决FileUriExposedException
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_content);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
        }
        else if(requestCode == 3){
            v_video.setVideoURI(Uri.fromFile(videoFile));
            v_video.start();
        }
    }

    private void init(){
        //view
        whatValue = getIntent().getStringExtra("what");
        Toast.makeText(this, whatValue, Toast.LENGTH_LONG).show();
        saveButton = (Button)findViewById(R.id.save_add);
        cancelButton = (Button)findViewById(R.id.cancel_add);
        ettext = (EditText)findViewById(R.id.ettext_add);
        c_img = (ImageView)findViewById(R.id.c_img_add);
        v_video = (VideoView)findViewById(R.id.v_video_add);
        //判断MainActivity传过来的值，确定用户需要添加的是什么内容
        switch (whatValue){
            case "text":
                c_img.setVisibility(View.GONE);
                v_video.setVisibility(View.GONE);
                break;
            case "img":
                c_img.setVisibility(View.VISIBLE);
                v_video.setVisibility(View.GONE);
                Intent oImg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    imgFile = new File(FILE_SAVE_PATH + TimeManage.getTime() + ".jpg"); //以時間名拍下的圖片

                    PermisionUtils.verifyStoragePermissions(this);

                    oImg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
                    startActivityForResult(oImg, 2);
                }
                break;
            case "video":
                c_img.setVisibility(View.GONE);
                v_video.setVisibility(View.VISIBLE);
                Intent oVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    videoFile = new File(FILE_SAVE_PATH + TimeManage.getTime() + ".mp4");

                    PermisionUtils.verifyStoragePermissions(this);

                    oVideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                    startActivityForResult(oVideo, 3);
                }
                break;
        }

        //date
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        //event
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_add:
                addDataToDB();
                finish();
                break;
            case R.id.cancel_add:
                finish();
                break;
        }
    }

    private void addDataToDB(){
        ContentValues values = new ContentValues();
        values.put(NotesDB.CONTENT, ettext.getText().toString());
        values.put(NotesDB.TIME, TimeManage.getTime());
        values.put(NotesDB.PATH, imgFile + "");
        values.put(NotesDB.VIDEO, videoFile + "");
        dbWriter.insert(NotesDB.TABLE_NAME, null, values);
    }
}
