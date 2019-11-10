package com.wangxiaobao.gsj.util

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import com.wangxiaobao.gsj.base.App
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.LogTool

/**
 * Created by ijays on 2018/8/21.
 */
object AudioManager {
    private var mediaPlayer: MediaPlayer? = null

    private var isPlaying = false

    private var audioFilePath = ""

    private var listener: OnCompletionListener? = null

    init {
        val am = App.getContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.isSpeakerphoneOn = true
        am.mode = AudioManager.MODE_NORMAL
    }

    fun playOnlineAudio(audioPath: String) {
        if (TextUtils.isEmpty(audioPath)) {
            LogTool.saveLog("需要播放的音频地址为空")
            listener?.onCompletion()
            return
        }
        if (isPlaying) {
            // 如果已经在播放了
            if (audioFilePath == audioPath) {
                return
            } else {
                //如果这是一条新的语音记录
                terminatePlay()
            }
        }
        mediaPlayer = MediaPlayer()
        audioFilePath = audioPath
        try {
            isPlaying = true
            mediaPlayer?.setDataSource(audioPath)

            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.setOnPreparedListener {
                it.start()

                mediaPlayer?.setVolume(1f, 1f)
            }
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
            }

            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            isPlaying = false
            listener?.onCompletion()
            LogTool.saveLog("播放音频失败==>${e.message}")
        }

        mediaPlayer?.setOnErrorListener { _, what, _ ->
            Log.e("SONGJIE", "onError===>$what")
            CommonUtil.showShortToast("播放语音失败")
            listener?.onCompletion()
            true
        }
        mediaPlayer?.setOnCompletionListener {
            isPlaying = false
            listener?.onCompletion()
        }
    }

    fun setOnCompletionListener(listener: OnCompletionListener) {
        this.listener = listener
    }

    fun terminatePlay() {
        if (isPlaying) {
            isPlaying = false
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

    }


    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun finishPlay() {
        listener?.onCompletion()
    }


    /**
     *移除监听器
     */
    fun removeListener() {
        this.listener = null
    }

    interface OnCompletionListener {
        fun onCompletion()
    }


}
