package com.example.easynote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.easynote.adapter.MyAdapter;
import com.example.easynote.database.NotesDB;
import com.example.easynote.other_activity.AddContent;
import com.example.easynote.other_activity.SelectActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button textButton, imgButton, videoButton;
    private ListView listView;
    private Intent addWhat;

    private NotesDB notesDB;
    private Cursor cursor;
    private SQLiteDatabase daReader;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromDB();
    }

    private void init(){
        //view
        listView = (ListView)findViewById(R.id.listView_main);
        textButton = (Button) findViewById(R.id.text_button_main);
        imgButton = (Button) findViewById(R.id.img_button_main);
        videoButton = (Button) findViewById(R.id.video_button_main);

        //other
        notesDB = new NotesDB(this);
        daReader = notesDB.getReadableDatabase();


        //event
        textButton.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent goSelect = new Intent(MainActivity.this, SelectActivity.class);
                goSelect.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                goSelect.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                goSelect.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                goSelect.putExtra(NotesDB.PATH, cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                goSelect.putExtra(NotesDB.VIDEO, cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(goSelect);
            }
        });
    }

    /**
     * 按钮点击监听器
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String flag = ((Button)v).getText().toString().trim();
        addWhat = new Intent(this, AddContent.class);
        switch (flag){
            case "打字":
                addWhat.putExtra("what", "text");
                startActivity(addWhat);
                break;
            case "拍照":
                addWhat.putExtra("what", "img");
                startActivity(addWhat);
                break;
            case "錄影":
                addWhat.putExtra("what", "video");
                startActivity(addWhat);
                break;
        }
    }

    public void getDataFromDB(){
        cursor = daReader.query(NotesDB.TABLE_NAME, null, null,
                null, null, null, null);
        myAdapter = new MyAdapter(this, cursor);
        listView.setAdapter(myAdapter);
    }
}
