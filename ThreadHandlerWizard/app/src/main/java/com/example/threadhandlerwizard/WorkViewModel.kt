package com.example.threadhandlerwizard

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WorkViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private const val MSG_PREPARING = 0
        private const val MSG_WORKING = 1
        private const val MSG_PROGRESS = 2
        private const val MSG_DONE = 3
        private const val MSG_CANCELED = 4
    }

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _progressText = MutableLiveData("Idle")
    val progressText: LiveData<String> = _progressText

    private val _resultText = MutableLiveData("Idle")
    val resultText: LiveData<String> = _resultText

    private var worker: Thread? = null
    private var player: MediaPlayer? = null

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_PREPARING -> {
                    _progress.value = 0
                    _progressText.value = "準備中…"
                    _resultText.value = "準備中…"
                }
                MSG_WORKING -> {
                    _progressText.value = "工作中…"
                    _resultText.value = "工作中…"
                    player?.start()          // 開始播放音樂
                }
                MSG_PROGRESS -> {
                    _progress.value = msg.arg1
                }
                MSG_DONE -> {
                    _progress.value = 100
                    _progressText.value = "背景工作結束！"
                    _resultText.value = "背景工作結束！"
                    stopMusic()              // 此時音樂已經播完了
                }
                MSG_CANCELED -> {
                    _progressText.value = "已取消 (Canceled)"
                    _resultText.value = "背景工作已中斷"
                    stopMusic()
                }
            }
        }
    }

    fun start() {
        if (worker?.isAlive == true) return

        // 1. 先在主執行緒準備好音樂，取得音樂長度(毫秒)
        stopMusic()
        player = MediaPlayer.create(getApplication(), R.raw.bgm)
        val duration = player?.duration ?: 10_000          // 取不到就用 10 秒
        val stepDelay = (duration / 100L).coerceAtLeast(10L) // 每 1% 要等多久

        // 2. 背景執行緒依照音樂長度跑進度
        worker = Thread {
            try {
                handler.sendEmptyMessage(MSG_PREPARING)
                Thread.sleep(2000)

                handler.sendEmptyMessage(MSG_WORKING)
                for (i in 1..100) {
                    Thread.sleep(stepDelay)
                    handler.sendMessage(handler.obtainMessage(MSG_PROGRESS, i, 0))
                }

                handler.sendEmptyMessage(MSG_DONE)
            } catch (e: InterruptedException) {
                handler.sendEmptyMessage(MSG_CANCELED)
            }
        }
        worker?.start()
    }

    fun cancel() {
        worker?.interrupt()
    }

    private fun stopMusic() {
        player?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        player = null
    }

    override fun onCleared() {
        worker?.interrupt()
        handler.removeCallbacksAndMessages(null)
        stopMusic()
        super.onCleared()
    }
}