package com.cookandroid.myapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 101;

    public static final int REQUSET_WRITE = 1;

    public static final String SORT_BY_DATE_ASC = "date_asc";
    public static final String SORT_BY_DATE_DESC = "date_desc";
    public static final String SORT_BY_TITLE_ASC = "title_asc";
    public static final String SORT_BY_TITLE_DESC = "title_desc";
    ListView listView;
    ListItemAdapter adapter;

    TextView tvCount;

    Button btnwrite, btnorder;

    ArrayList<Memo> listItems;

    SearchView searchView;
    public MemoDao memoDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setTitle("메모장 연습");
        FindId();
        MemoDatabase datebase = Room.databaseBuilder(getApplicationContext(),
                        MemoDatabase.class, "db").
                fallbackToDestructiveMigration(). //스키마 버전 변경 가능
                        allowMainThreadQueries(). //Main th에서 i/o가능
                        build();

        memoDao = datebase.memoDao();
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        listItems = new ArrayList<>();
        String currentSortOrder = loadSortOrder();

        adapter = new ListItemAdapter(listItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        //ListItemAdapter main = new ListItemAdapter(listItems);
        //adapter = main;
        registerForContextMenu(listView);
        adapter.notifyDataSetChanged();
        setCount();

        switch (currentSortOrder) {
            case SORT_BY_TITLE_ASC:
                btnorder.setText("제목 순 오름차순");
                listItems.addAll(memoDao.getAllMemoOrderByTitleAsc());
                break;
            case SORT_BY_TITLE_DESC:
                btnorder.setText("제목 순 내림차순");
                listItems.addAll(memoDao.getAllMemoOrderByTitleDesc());
                break;
            case SORT_BY_DATE_ASC:
                btnorder.setText("만든 날짜 순 오름차순");
                listItems.addAll(memoDao.getAllMemoOrderByDateAsc());
                break;
            case SORT_BY_DATE_DESC:
                btnorder.setText("만든 날짜 순 내림차순");
                listItems.addAll(memoDao.getAllMemoOrderByDateDesc());
                break;
            default:
                btnorder.setText("정렬"); // 기본값 설정
                listItems.addAll(memoDao.getAllMemo()); // 기본 정렬 (날짜 오름차순)
                break;
        }
        //adapter.addItem(new Memo("제목1", "2024년 6월 18일 14시 24분", "내용1"));

        //기존 글에서
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title = listItems.get(i).getTitle();
                String content = listItems.get(i).getContent();
                boolean isabled = listItems.get(i).getIsabled();
                Intent intent = new Intent(getApplicationContext(), ModifyMemo.class);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("number", i);
                intent.putExtra("isabled", isabled);
                startActivityForResult(intent, REQUEST_CODE);
                //Log.d("MainActivity", "Received values: " + title + ", " + content + ", " + date);
            }
        });

        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), btnorder);
                popupMenu.getMenuInflater().inflate(R.menu.order_menu, popupMenu.getMenu());

                // 현재 정렬 상태를 불러와서 메뉴 버튼에 표시
                String currentSortOrder = loadSortOrder();
                switch (currentSortOrder) {
                    case SORT_BY_TITLE_ASC:
                        btnorder.setText("제목 순 오름차순");
                        break;
                    case SORT_BY_TITLE_DESC:
                        btnorder.setText("제목 순 내림차순");
                        break;
                    case SORT_BY_DATE_ASC:
                        btnorder.setText("만든 날짜 순 오름차순");
                        break;
                    case SORT_BY_DATE_DESC:
                        btnorder.setText("만든 날짜 순 내림차순");
                        break;
                    default:
                        btnorder.setText("정렬"); // 기본값 설정
                        break;
                }

                // 팝업 메뉴 아이템 클릭 리스너 설정
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String newSortOrder = "";

                        if (item.getItemId() == R.id.titleAsec) {
                            btnorder.setText("제목 순 오름차순");
                            listItems = new ArrayList<>(memoDao.getAllMemoOrderByTitleAsc());
                            newSortOrder = SORT_BY_TITLE_ASC;
                        } else if (item.getItemId() == R.id.titleDesc) {
                            btnorder.setText("제목 순 내림차순");
                            listItems = new ArrayList<>(memoDao.getAllMemoOrderByTitleDesc());
                            newSortOrder = SORT_BY_TITLE_DESC;
                        } else if (item.getItemId() == R.id.dateAsec) {
                            btnorder.setText("만든 날짜 순 오름차순");
                            listItems = new ArrayList<>(memoDao.getAllMemoOrderByDateAsc());
                            newSortOrder = SORT_BY_DATE_ASC;
                        } else if (item.getItemId() == R.id.dateDesc) {
                            btnorder.setText("만든 날짜 순 내림차순");
                            listItems = new ArrayList<>(memoDao.getAllMemoOrderByDateDesc());
                            newSortOrder = SORT_BY_DATE_DESC;
                        } else {
                            return false;
                        }

                        // 정렬된 리스트를 어댑터에 설정하고 리스트뷰 업데이트
                        adapter.setListItems(listItems);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        // 새로운 정렬 상태 저장
                        saveSortOrder(newSortOrder);

                        return true;
                    }
                });


                // 팝업 메뉴 표시
                popupMenu.show();
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //입력받은 문자열 처리
                Log.d("search", "search?: ");
                ArrayList<Memo> searchmemos = new ArrayList<>(memoDao.searchMemos(s));
                ListItemAdapter search = new ListItemAdapter(searchmemos);
                adapter = search;
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) { // 입력란의 문자열이 공백인 경우
                    // 모든 메모들을 가져와서 보여줌
                    ArrayList<Memo> allMemos = new ArrayList<>(memoDao.getAllMemo());
                    adapter.setListItems(allMemos);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    // 입력란의 문자열로 검색을 수행하여 결과를 가져옴
                    ArrayList<Memo> searchResults = new ArrayList<>(memoDao.searchMemos(s));
                    // adapter에 검색 결과를 설정하고, 리스트뷰에 반영
                    adapter.setListItems(searchResults);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                return false; // 이벤트 처리가 완료되었음을 반환
            }
        });


        //누르면 새 메모장 앱으로 ㄱㄱ
        btnwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), writeMemo.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void setCount() {
        int count = memoDao.countMemo();
        //tvCount.setText("메모 : " + count + "개" );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//거진 메인 함수 삽입/수정/삭제
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("modify", "Received values: " + requestCode+resultCode);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == REQUSET_WRITE) { // 메모 생성
                // 결과 데이터 받기
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                String date = data.getStringExtra("date");
                if (content.isEmpty() && title.isEmpty()) {
                    Toast.makeText(this, "메모가 비어 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Memo item = new Memo(date, title, content);
                    memoDao.setInsetMemo(item);
                    adapter.addItem(item);

                    setCount();
                }

            } else if (resultCode == RESULT_OK) { // 메모 수정
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                int index = data.getIntExtra("number", -1);

                if (index == -1 || index >= listItems.size()) {
                    Toast.makeText(this, "수정하는 데 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("modify-main", "Received values: " + title + ", " + content + ", " + index);
                    Memo item = listItems.get(index);
                    //Log.d("delete", "Received values: " + title + ", " + content + ", " + index);
                    item.setTitle(title);
                    item.setContent(content);
                    adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    memoDao.setUpdateMemo(item);
                    setCount();
                    if (item.isEmpty()) {
                        listItems.remove(index);
                        Toast.makeText(this, "내용이 모두 없어 제거되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                }
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        registerForContextMenu(listView);
        // 클릭된 메뉴의 리스트 뷰의 번호 가져오기
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        // 삭제 차단
        boolean isabled = listItems.get(position).getIsabled();
        menu.findItem(R.id.delete).setEnabled(isabled);

        // 글씨 토글
        if (isabled) {
            menu.findItem(R.id.lock).setTitle("잠금");
        } else {
            menu.findItem(R.id.lock).setTitle("잠금해제");
        }

        // 즐겨찾기 토글
        boolean isfind = listItems.get(position).getIsfind();
        if (!isfind) {
            menu.findItem(R.id.find).setTitle("즐겨찾기");
        } else {
            menu.findItem(R.id.find).setTitle("즐겨 찾기 해제");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        Memo memo = listItems.get(index);

        if (item.getItemId() == R.id.delete) {
            Memo memoToDelete = listItems.get(index); // 리스트에서 제거할 메모 가져오기
            listItems.remove(memoToDelete); // 리스트에서 제거
            memoDao.setDeleteMemo(memoToDelete); // DB에서 삭제할 메모 전달
            setCount();
            adapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.lock) {
            boolean islock = memo.getIsabled();
            memo.setIsabled(!islock);
            memoDao.setUpdateMemo(memo); // 잠금 여부 업데이트
            adapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.find) {
            boolean isfind = memo.getIsfind();
            memo.setIsfind(!isfind);
            memoDao.setUpdateMemo(memo); // 즐겨찾기 여부 업데이트
            adapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.copy) {
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.getDefault());
            String formattedDate = sdf.format(currentDate);
            Memo copiedMemo = new Memo(memo.getContent(), memo.getTitle(), formattedDate);
            listItems.add(copiedMemo); // 복사된 메모 리스트에 추가
            memoDao.setInsetMemo(copiedMemo); // DB에 복사된 메모 추가
            setCount();
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
    private void saveSortOrder(String sortOrder) {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sortOrder", sortOrder);
        editor.apply();
    }
    private String loadSortOrder() {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String sortOrder = sharedPref.getString("sortOrder", null); // SharedPreferences에서 값 가져오기

        if (sortOrder == null) {
            // 기본값 설정
            sortOrder = SORT_BY_TITLE_ASC; // 제목 오름차순으로 설정
        }

        return sortOrder;
    }

    private void FindId(){
        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        btnorder = findViewById(R.id.btnorder);
        btnwrite = findViewById(R.id.btnwrite);
    }
}