package com.dontsu.contentresolver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val PERM_STORAGE = 99

    init {
        //Timber initialize
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()

    }

    private fun startProcess() {
        setContentView(R.layout.activity_main)

        recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MusicRecyclerAdapter(getMusicList())
        }
    }

    private fun checkPermission() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           val permissionState = ContextCompat.checkSelfPermission(this@MainActivity, permissions[0])
           if (permissionState != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, permissions, PERM_STORAGE)
           } else {
               startProcess()
           }
       }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERM_STORAGE) {
            var check = true
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    check = false
                    break
                }
            }
            if (!check) {
                Toast.makeText(this@MainActivity, "권한 요청을 모두 승인해야지만 앱을 실행할 수 있습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                startProcess()
            }
        }
    }

    private fun getMusicList() : MutableList<Music>{
        // 1. 음원 정보 주소
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        // 2. 음원 정보 컬럼들
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
        )
        // 3. 컨텐트 리졸버의 쿼리에 주소와 컬럼명을 입력하면 커서 형태로 반환
        val cursor = contentResolver.query(listUrl, proj, null, null, null)
        val musicList = mutableListOf<Music>()
        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3)
            val duration = cursor.getLong(4)

            val music = Music(id, title, artist, albumId, duration)
            musicList.add(music)
        }

        return musicList
    }



}
