package com.bardisammar.elsalamcity.cart.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bardisammar.elsalamcity.pojo.Pro_Of_Product;


import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("SELECT * FROM products")
    List<Pro_Of_Product> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(Pro_Of_Product mUser);

    @Insert
    void insertAllUser(Pro_Of_Product... mUsersList);

    @Delete
    void delete(Pro_Of_Product mUser);

    @Update
    void updateUser(Pro_Of_Product mUser);

    @Query("SELECT * FROM products WHERE uid = :uId")
    Pro_Of_Product getUserById(int uId);

    @Query("SELECT * FROM products WHERE uid IN (:userIds)")
    List<Pro_Of_Product> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM products WHERE name LIKE :name LIMIT 1")
    boolean findByName(String name);

    @Query("DELETE FROM products")
    void nukeTable();

    @Query("SELECT uid FROM products")
    List<Integer> getCount();
}
