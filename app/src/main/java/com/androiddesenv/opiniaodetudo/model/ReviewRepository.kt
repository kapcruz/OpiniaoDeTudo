package com.androiddesenv.opiniaodetudo.model.repository

import android.content.Context
import androidx.room.Delete
import com.androiddesenv.opiniaodetudo.infra.dao.ReviewDao
import com.androiddesenv.opiniaodetudo.infra.dao.ReviewDatabase
import com.androiddesenv.opiniaodetudo.model.Review
import java.util.*


class ReviewRepository{

    private val reviewDAO: ReviewDao

    constructor(context: Context){
        val reviewDatabase = ReviewDatabase.getInstance(context)
        reviewDAO = reviewDatabase.reviewDao()
    }

    fun save(name: String, review: String){
        reviewDAO.save(Review(UUID.randomUUID().toString(), name, review))
    }

    fun listAll(): List<Review> {
        return reviewDAO.listAll()
    }

    @Delete
    fun delete(item: Review) {
        reviewDAO.delete(item)
    }
}