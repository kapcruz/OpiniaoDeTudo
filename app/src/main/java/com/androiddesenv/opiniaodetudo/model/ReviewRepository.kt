package com.androiddesenv.opiniaodetudo.model.repository
import com.androiddesenv.opiniaodetudo.model.Review
import java.util.*


class ReviewRepository{
    private constructor()
    companion object {
        val instance : ReviewRepository = ReviewRepository()
    }
    private val data = mutableListOf<Review>()
    fun save(name: String, review: String) {
        data.add(Review(UUID.randomUUID().toString(), name, review));
    }
}