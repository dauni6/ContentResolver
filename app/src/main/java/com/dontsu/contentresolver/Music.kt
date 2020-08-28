package com.dontsu.contentresolver

import android.net.Uri
import android.provider.MediaStore

/**
 * @param id : MediaStore가 음원을 구분하는 유니크 ID
 * @param title : 음원의 제목
 * @param artist : 음원의 아티스트
 * @param albumId : 앨범을 구분하는 ID
 * @param duration : 음원의 길이
 * */

data class Music (
    var id: String = "",
    var title: String?,
    var artist: String?,
    var albumId: String?,
    var duration: Long?
) {
    fun getMusicUri() : Uri {
       return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

    fun getAlbumUri() : Uri {
        return Uri.parse("content://media/external/audio/albumart/$albumId")
    }
}