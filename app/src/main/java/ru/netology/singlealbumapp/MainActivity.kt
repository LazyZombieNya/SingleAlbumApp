package ru.netology.singlealbumapp

import android.media.MediaPlayer
import android.os.Bundle
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
    private lateinit var mainPlayPauseButton: ImageButton
    private lateinit var albumInfo: TextView
    private lateinit var trackInfo: TextView
    private val client = OkHttpClient()
    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrack: Track? = null
    private var currentPlayPauseButton: ImageButton? = null
    private lateinit var tracks: List<Track>
    private var currentTrackIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        mainPlayPauseButton = findViewById(R.id.play_pause_button)
        trackInfo = findViewById(R.id.track_info)
        albumInfo = findViewById(R.id.album_info)

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = SongAdapter()

        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnCompletionListener {
            playNextTrack()
        }

        // Обработчик кнопки Play/Pause
        mainPlayPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pauseTrack(mainPlayPauseButton)
            } else {
                currentTrack?.let {
                    playTrack(it, mainPlayPauseButton)
                    mainPlayPauseButton.setImageResource(R.drawable.pause_icon)
                    trackInfo.text = it.file
                }
            }
        }

        fetchAlbumData()// Загрузка данных из сети
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
                    val album = Gson().fromJson(it, Album::class.java)
                    runOnUiThread {
                        updateUI(album)
                    }
                }
            }
        })
    }
    private fun playTrack(track: Track, button: ImageButton) {
        val url = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/${track.file}"
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
            currentTrack = track
            currentPlayPauseButton = button
            button.setImageResource(R.drawable.pause_icon)
            mainPlayPauseButton.contentDescription = "Pause"  // Обновление текста кнопки
            mainPlayPauseButton.setImageResource(R.drawable.pause_icon)

            trackInfo.text = track.file
        }
        // Устанавливаем текущий индекс трека
        currentTrackIndex = tracks.indexOf(track)
    }

    private fun pauseTrack(button: ImageButton?) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mainPlayPauseButton.contentDescription = "Play"
            button?.setImageResource(R.drawable.play_icon)
            mainPlayPauseButton.setImageResource(R.drawable.play_icon)
        }
    }

    private fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mainPlayPauseButton.contentDescription = "Play"
            mainPlayPauseButton.setImageResource(R.drawable.play_icon)
        }
    }
    private fun playNextTrack() {
        if (tracks.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % tracks.size
            val nextTrack = tracks[currentTrackIndex]
            playTrack(nextTrack, currentPlayPauseButton!!)
        }
    }
    private fun playPauseTrack(track: Track, button: ImageButton) {
        if (currentTrack == track && mediaPlayer.isPlaying) {
            pauseTrack(button)
        } else {
            currentTrack?.let { pauseTrack(currentPlayPauseButton) }
            playTrack(track, button)
        }
    }



    private fun updateUI(album: Album) {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val adapter = SongAdapter { track, button -> playPauseTrack(track, button) }
        recyclerView.adapter = adapter
        adapter.setTracks(album.tracks)

        // Обновляем список треков и устанавливаем текущий индекс на -1
        tracks = album.tracks
        currentTrackIndex = -1

        // Обновление информации об альбоме
        albumInfo.text = """
            Title: ${album.title}
            Subtitle: ${album.subtitle}
            Artist: ${album.artist}
            Published: ${album.published}
            Genre: ${album.genre}
        """.trimIndent()
    }

}
