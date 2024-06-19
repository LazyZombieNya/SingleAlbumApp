package ru.netology.singlealbumapp

data class Album(
    val id: Int,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<Track>
)

data class Track(
    val id: Int,
    val duration: String,
    val file: String
)
