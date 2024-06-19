package ru.netology.singlealbumapp

data class Album(
    val album: String,
    val artist: String,
    val year: Int,
    val genre: String,
    val tracks: List<Track>
)

data class Track(
    val id: Int,
    val file: String
)
