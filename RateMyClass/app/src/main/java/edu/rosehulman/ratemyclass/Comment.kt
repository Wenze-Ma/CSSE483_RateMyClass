package edu.rosehulman.ratemyclass

data class Comment (var user: User, var isAnonymous: Boolean, var professor: User, var content: String, var rating: Float)