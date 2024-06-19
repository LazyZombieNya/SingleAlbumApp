package ru.netology.singlealbumapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var tracks: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        Log.d("SongAdapter", "ItemCount: ${tracks.size}")
        return tracks.size
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.song_title)
        private val duration: TextView = itemView.findViewById(R.id.song_duration)

        fun bind(track: Track) {
            title.text = track.file // Убедитесь, что это правильно
            duration.text = "Unknown duration" // Добавьте значение по умолчанию для длительности
            Log.d("SongViewHolder", "Binding ${track.file}") // Логирование для проверки привязки
        }
    }
}

