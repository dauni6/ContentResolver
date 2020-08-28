package com.dontsu.contentresolver

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_recycler.view.*
import java.text.SimpleDateFormat

class MusicRecyclerAdapter(private var musicList: MutableList<Music>) : RecyclerView.Adapter<MusicRecyclerAdapter.ItemViewHolder>(){

    var mediaPlayer: MediaPlayer? = null

    override fun getItemCount(): Int = musicList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view_recycler, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val music = musicList[position]
        holder.setMusic(music)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var musicUri: Uri? = null

        init {
            itemView.setOnClickListener {
                if (mediaPlayer != null) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
                mediaPlayer = MediaPlayer.create(itemView.context, musicUri)
                mediaPlayer?.start()
            }

            itemView.stopBtn.setOnClickListener {
                mediaPlayer?.stop()
            }
        }

        fun setMusic(music: Music) {
            itemView.imageAlbum.setImageURI(music.getAlbumUri())
            itemView.textArtist.text = music.artist
            itemView.textTitle.text = music.title

            val sdf = SimpleDateFormat("mm:ss")
            val duration = sdf.format(music.duration)
            itemView.textDuration.text = duration

            // 클릭 시 플레이를 할 수 있게 음원의 Uri를 저장
            this.musicUri = music.getMusicUri()
        }

    }
}

