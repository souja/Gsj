package com.wangxiaobao.gsj.util

import android.os.Environment
import com.wangxiaobao.gsj.common.LogTool
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by ijays on 2018/8/22.
 */
object AudioRecordManager {
    private var audioFilePath: String = ""

    private var isRecording = false

    private var mp3Recorder: MP3Recorder? = null

    private var disposable: Disposable? = null


    fun setupAudio() {


        try {

            val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/wangxiaobao/records"

            val file = File(dirPath)
            if (!file.exists()) {
                file.mkdir()
            }
            val fileName = System.currentTimeMillis().toString() + ".mp3"

            audioFilePath = File(file, fileName).absolutePath
            LogTool.saveLog("===path==$audioFilePath")

            isRecording = true

            mp3Recorder = MP3Recorder(File(audioFilePath))
            mp3Recorder

            mp3Recorder?.start()

            deleteAudioFile(dirPath)
        } catch (e: Exception) {
            LogTool.saveLog("录音出错了==>${e.message}")
        }


    }

    private fun deleteAudioFile(dirPath: String) {
        disposable = Observable.just(dirPath)
                .map {
                    val dir = File(it)
                    if (dir.isDirectory) {
                        val files = dir.listFiles()
                        if (files.isNotEmpty()) {
                            val currentTime = System.currentTimeMillis()
                            val iterator = files.iterator()
                            while (iterator.hasNext()) {
                                val f = iterator.next()
                                var fName = f.name
                                fName = fName.substring(0, fName.length - 4)
                                if (currentTime - fName.toLong() > 5000) {
                                    f.delete()
                                }
                            }
                        }
                    }
                    1
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun release() {
        try {
            isRecording = false
            mp3Recorder?.stop()
            mp3Recorder = null
        } catch (e: Exception) {
            LogTool.saveLog("释放录音异常==>${e.message}")
        }
    }

    fun getFilePath(): String {
        return audioFilePath
    }

    fun isRecording(): Boolean {
        return isRecording
    }


}
