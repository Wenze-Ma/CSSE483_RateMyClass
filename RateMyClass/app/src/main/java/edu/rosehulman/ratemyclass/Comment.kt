package edu.rosehulman.ratemyclass

data class Comment (var user: User, var isAnonymous: Boolean, var professor: String, var content: String, var rating: Ratings)