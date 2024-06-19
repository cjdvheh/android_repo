package com.cookandroid.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MemoDao {

    @Insert
    void setInsetMemo(Memo memo);

    @Update
    void setUpdateMemo(Memo memo);

    @Delete
    void setDeleteMemo(Memo memo);

    @Query("select * from Memo")
    List<Memo> getAllMemo();

    @Query("SELECT * FROM Memo ORDER BY date ASC")
    List<Memo> getAllMemoOrderByDateAsc();

    @Query("SELECT * FROM Memo ORDER BY date DESC")
    List<Memo> getAllMemoOrderByDateDesc();

    @Query("SELECT * FROM Memo ORDER BY title ASC")
    List<Memo> getAllMemoOrderByTitleAsc();

    @Query("SELECT * FROM Memo ORDER BY title DESC")
    List<Memo> getAllMemoOrderByTitleDesc();

    @Query("DELETE FROM Memo WHERE id = :id")
    void deleteMemoById(int id);

    @Query("Select count(*) from Memo")
    int countMemo();

    @Query("SELECT * FROM Memo WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    List<Memo> searchMemos(String query);


}
