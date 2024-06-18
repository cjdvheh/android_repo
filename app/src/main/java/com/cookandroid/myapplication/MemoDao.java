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

    @Query("DELETE FROM Memo WHERE id = :id")
    void deleteMemoById(int id);

    @Query("Select count(*) from Memo")
    int countMemo();

}
