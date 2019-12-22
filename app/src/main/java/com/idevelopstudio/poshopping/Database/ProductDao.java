package com.idevelopstudio.poshopping.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAllProducts();

    @Query("SELECT * FROM product WHERE category = :category")
    List<Product> getProductsByCategory(String category);

    @Query("SELECT * FROM product WHERE id = :id")
    Product getProductById(long id);

    @Query("UPDATE product SET stock = :stock WHERE id = :id")
    void updateProductStock(int stock, long id);

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);
}
