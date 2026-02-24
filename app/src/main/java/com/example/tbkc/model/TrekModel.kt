package com.example.tbkc.model

data class TrekModel(
    val trekId: String = "",
    val userId: String = "",
    val title: String = "",
    val location: String = "",
    val days: String = "",
    val description: String = "",
    val itinerary: List<String> = emptyList(),
    val budget: String = "",
    val recommendations: String = "",
    val difficulty: String = "Easy",
    val image: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "trekId" to trekId,
            "userId" to userId,
            "title" to title,
            "location" to location,
            "days" to days,
            "description" to description,
            "itinerary" to itinerary,
            "budget" to budget,
            "recommendations" to recommendations,
            "difficulty" to difficulty,
            "image" to image
        )
    }
}