package com.cookandroid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyMemo extends Activity {

    EditText edcontect, edtitle;
    Button btnSave;
    Memo memo;

    int listindex;


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

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content"); // 기본값 설정 가능
        boolean isabled = intent.getBooleanExtra("isabled",true);
        listindex =intent.getIntExtra("number",-1);
        edcontect.setText(content);
        edtitle.setText(title);

        edcontect.setEnabled(isabled);
        edtitle.setEnabled(isabled);
        if(!isabled){
            edcontect.setTextColor(Color.GRAY);
            edtitle.setTextColor(Color.GRAY);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isabled){
                    finish();
                }else{
                    String content = edcontect.getText().toString();
                    String title = edtitle.getText().toString();
                    int size = content.length()>10 ? 10:content.length() ;
                    if(title.isEmpty())
                        title=content.substring(0,size);
                    //Log.d("Modify-sub", "Received values: " + title + ", " + content + ", " );
                    Intent intent = new Intent();
                    intent.putExtra("content", content);
                    intent.putExtra("title", title);
                    intent.putExtra("number", listindex);
                    setResult(RESULT_OK, intent);
                    finish(); // 액티비티 종료
                }

            }
        });

    }
    //수정

}
