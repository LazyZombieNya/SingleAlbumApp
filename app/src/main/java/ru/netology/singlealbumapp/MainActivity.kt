package ru.netology.singlealbumapp

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import com.google.gson.Gson
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var playPauseButton: ImageButton
    private lateinit var albumInfo: TextView
    private val client = OkHttpClient()
    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrack: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        playPauseButton = findViewById(R.id.play_pause_button)
        albumInfo = findViewById(R.id.album_info)

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SongAdapter()

        // Обработчик кнопки Play/Pause
        playPauseButton.setOnClickListener {
            // Логика воспроизведения/паузы
        }

        // Установка информации об альбоме
        albumInfo.text = "One More Light Live\nLinkin Park\n2017 - Альтернатива"
        fetchAlbumData()
    }
    private fun fetchAlbumData() {
        val request = Request.Builder()
            .url("https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    Log.d("AlbumData", it)  // Логирование ответа
                    val album = Gson().fromJson(it, Album::class.java)
                    runOnUiThread {
                        updateUI(album)
                    }
                }
            }
        })
    }


    private fun updateUI(album: Album) {
        albumInfo.text = "${album.album}\n${album.artist}\n${album.year} - ${album.genre}"
        Log.d("AlbumData", "Tracks size: ${album.tracks.size}")  // Логирование размера списка треков
        (recyclerView.adapter as SongAdapter).setTracks(album.tracks)
    }
}
