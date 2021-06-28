package com.example.get_pick

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()
    //intent를 통해 전달된 이미지 데이터를 담을 리스트 선언

    private var timer: Timer? = null

    private var currentPosition = 0

    private val photoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_photoframe)

        getPhotoUriFromIntent()

    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize",0)
        for(i in 0..size) {
            intent.getStringExtra("photo$i")?.let { //null이 아닐때만 실행
                photoList.add(Uri.parse(it))
                //Str형을 Uri형으로 다시 변환하여 리스트에 저장
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5*1000) { //5초에 한 번씩 발생
            runOnUiThread {
                val current = currentPosition
                val next = if(photoList.size <= currentPosition + 1) 0 else currentPosition + 1
                //현재 위치가 전체 리스트 크기보다 크거나 같으면 0, 리스트 크기보다 작다면 현재 위치값에 +1 하여 인덱스 선언

                backgroundPhotoImageView.setImageURI(photoList[current])
                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate() //애니메이션 함수 실행
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()
                //순차적으로 애니메이션을 통해 이미지를 팝업한다.

                currentPosition = next
            }
        }
    }

//상황별 타이머 작동 여부 설정
    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}