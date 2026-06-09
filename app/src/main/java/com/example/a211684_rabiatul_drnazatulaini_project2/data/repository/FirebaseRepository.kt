package com.example.a211684_rabiatul_drnazatulaini_project2.data.repository

import com.example.a211684_rabiatul_drnazatulaini_project2.model.Review
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveReview(review: Review) {
        db.collection("reviews")
            .add(review)
    }

    fun getReviewsByStation(
        stationName: String,
        onResult: (List<Review>) -> Unit
    ) {
        db.collection("reviews")
            .whereEqualTo("stationName", stationName)
            .get()
            .addOnSuccessListener { result -> //if firebase successfully returns data, run this code

                val reviews =
                    result.toObjects(Review::class.java)

                onResult(reviews)
            }
    }
}