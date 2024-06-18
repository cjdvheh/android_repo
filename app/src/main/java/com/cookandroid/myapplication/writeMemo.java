package com.cookandroid.myapplication;

import static com.cookandroid.myapplication.MainActivity.REQUSET_WRITE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class writeMemo extends Activity {
    EditText edcontect, edtitle;
    Button btnSave;
    boolean isNewMemo;//true일떄 새 메모 false일떄 수정

    Memo memo;

   // MemoDao memoDao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wirte);
        setTitle("메모장");

        //memoDao = datebase.memoDao();

        Intent intent = getIntent();
        //기본값이 false

        //아이디
        edcontect = findViewById(R.id.edcontent);
        edtitle = findViewById(R.id.edtitle);
        btnSave = findViewById(R.id.btnSave);


        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.getDefault());
        String formattedDate = sdf.format(currentDate);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edcontect.getText().toString();
                String title = edtitle.getText().toString();
                String date = formattedDate;
                int size = content.length()>10 ? 10:content.length() ;
                if(title.isEmpty())
                    title=content.substring(0,size);
                //Log.d("write", "Received values: " + title + ", " + content + ", " + date);
                Intent intent = new Intent();
                intent.putExtra("content",content);
                intent.putExtra("title",title);
                intent.putExtra("date",date);
                setResult(REQUSET_WRITE, intent);
                finish(); // 액티비티 종료
            }
        });

    }
}
