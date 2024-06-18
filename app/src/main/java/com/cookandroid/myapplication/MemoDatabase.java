package com.cookandroid.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version = 1)
public abstract class MemoDatabase extends RoomDatabase {

    private static MemoDatabase instance;

    public abstract MemoDao memoDao();
}
