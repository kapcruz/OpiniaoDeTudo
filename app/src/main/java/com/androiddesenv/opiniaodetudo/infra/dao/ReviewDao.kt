package com.androiddesenv.opiniaodetudo.infra.dao

import androidx.room.*
import com.androiddesenv.opiniaodetudo.model.Review

@Dao
interface ReviewDao {

    @Insert
        fun save(review:Review)

    @Query("SELECT * from ${ReviewTableInfo.TABLE_NAME}")
        fun listAll():List<Review>

    @Delete
        fun delete(item: Review)

    @Update
        fun update(review: Review)

}

