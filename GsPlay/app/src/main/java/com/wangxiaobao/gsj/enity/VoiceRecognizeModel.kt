package com.wangxiaobao.gsj.enity

import java.io.Serializable

/**
 * Created by ijays on 2018/8/22.
 */
data class VoiceRecognizeModel(val voiceUrl: String,
                               val message: String) : Serializable