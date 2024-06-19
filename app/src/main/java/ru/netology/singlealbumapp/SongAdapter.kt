package ru.netology.singlealbumapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val playPauseListener: (Track, ImageButton) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var tracks: List<Track> = emptyList()

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = tracks.size

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.song_title)
        private val playPauseButton: ImageButton = itemView.findViewById(R.id.playPauseButton)

        fun bind(track: Track) {
            trackName.text = track.file
            playPauseButton.setOnClickListener {
                playPauseListener(track, playPauseButton)
            }
        }
    }
}




