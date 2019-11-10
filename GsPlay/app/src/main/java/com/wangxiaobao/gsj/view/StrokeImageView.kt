package com.wangxiaobao.gsj.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.wangxiaobao.gsj.util.AudioRecordManager
import com.wangxiaobao.gsj.util.ClickUtil
import com.wangxiaobao.gsj.util.GlideUtil
import com.wangxiaobao.waiter.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by ijays on 2018/8/16.
 */
class StrokeImageView(context: Context, attr: AttributeSet?, def: Int = 0) : ImageView(context, attr, def) {

    private var paint: Paint? = null

    private var rectF: RectF? = null

    private var currentAngle = 0f

    private var disposable: Disposable? = null

    private var startRecordTime: Long = 0
    /**
     * 是否开始录音
     */
    private var isRecording = false

    private var listener: OnRecordListener? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0)

    init {
        initPaint()
        setImageResource(R.drawable.icon_voice_button)
    }

    private fun initPaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.color = ContextCompat.getColor(context, R.color.storm_dust)
        paint?.style = Paint.Style.STROKE
        paint?.strokeWidth = 4f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val centerX = (w / 2).toFloat()
        val centerY = (h / 2).toFloat()

        val radius = Math.min(centerX, centerY) - 2

        rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(rectF, 270f, currentAngle, false, paint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val actionMusked = event?.actionMasked ?: 0
        when (actionMusked) {
            MotionEvent.ACTION_DOWN -> {
                if (ClickUtil.isFastClick()) {
                    Log.e("SONGJIE", "fast click")
                    return true
                }
                Log.e("SONGJIE", "ACTION_DOWN")
                if (!isRecording) {
                    GlideUtil.displayGif(this, R.drawable.gif_recording)
                    startRecordTime = System.currentTimeMillis()
                    AudioRecordManager.setupAudio()
                    isRecording = true


                    disposable = Flowable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                listener?.onRecordStart()
                                currentAngle = it.toFloat() / 60 * 360
                                invalidate()

                                if (it.toInt() == 60 && AudioRecordManager.isRecording()) {
                                    //最多录制60s
                                    AudioRecordManager.release()
                                    listener?.onRecordFinish(AudioRecordManager.getFilePath(), 60 * 1000)
                                }

                            }
                }
            }
            MotionEvent.ACTION_UP -> {
                Log.e("SONGJIE", "ACTION_UP")
                if (isRecording) {
                    AudioRecordManager.release()
                    setImageResource(R.drawable.icon_voice_button)
                    isRecording = false
                    listener?.onRecordFinish(AudioRecordManager.getFilePath(),
                            (System.currentTimeMillis() - startRecordTime).toInt())
                }

                disposable?.dispose()
                currentAngle = 0f
                invalidate()
            }
        }

        return true
    }


    fun setOnRecordListener(listener: OnRecordListener) {
        this.listener = listener
    }

    interface OnRecordListener {
        fun onRecordStart()

        fun onRecordFinish(filePath: String, timeDiff: Int)
    }


}