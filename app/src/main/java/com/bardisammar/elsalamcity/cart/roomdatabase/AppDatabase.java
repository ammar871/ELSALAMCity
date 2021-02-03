package com.bardisammar.elsalamcity.cart.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;


@Database(entities = {Pro_Of_Product.class}, version = 4, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mInstance;
    private static final String DATABASE_NAME = "wave-database";
    public abstract UserDao userDao();

    public synchronized static AppDatabase getDatabaseInstance(Context context) {

        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

}
