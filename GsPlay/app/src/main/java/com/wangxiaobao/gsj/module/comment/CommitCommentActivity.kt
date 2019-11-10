package com.wangxiaobao.gsj.module.comment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.wangxiaobao.gsj.base.App
import com.wangxiaobao.gsj.base.BaseActivity
import com.wangxiaobao.gsj.base.SplashActivity
import com.wangxiaobao.gsj.common.AppConstants
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.enity.CommitCommentModel
import com.wangxiaobao.gsj.enity.MapMerchantModel
import com.wangxiaobao.gsj.http.RetrofitClient
import com.wangxiaobao.gsj.util.AudioManager
import com.wangxiaobao.gsj.view.dialog.CommitSuccessDialog
import com.wangxiaobao.gsj.view.dialog.RecordDialogFragment
import com.wangxiaobao.waiter.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_commit_comment.*
import kotlinx.android.synthetic.main.item_merchant_location.*
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by ijays on 2018/8/14.
 */
class CommitCommentActivity : BaseActivity(), View.OnClickListener, RecordDialogFragment.OnUploadAudioListener {

    /**
     * 提交评论
     */
    private var commitComment: CommitCommentModel? = null

    private var merchantId = ""

    /**
     * 输入的评价
     */
    private var inputContent = ""

    private var pureTextView: View? = null

    /**
     * 评分的星星数目
     */
    private var rateStarCount = 0
    /**
     * 语音识别的地址
     */
    private var voiceUrl: String? = null

    private var disposable: Disposable? = null
    /**
     * 是否是使用语音输入
     */
    private var isVoice = false

    private val emojiFilter = object : InputFilter {
        var emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            val emojiMatcher = emoji.matcher(source)
            return if (emojiMatcher.find()) {
                ""
            } else null
        }
    }
    private val spaceFilter = InputFilter { source, start, end, dest, dstart, dend ->
        if (source == " ") {
            ""
        } else {
            null
        }
    }

    private val specicalCharacter = InputFilter { source, start, end, dest, dstart, dend ->
        val regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(regexStr)
        val matcher = pattern.matcher(source.toString())
        if (matcher.matches()) {
            ""
        } else {
            null
        }
    }

    companion object {
        fun startActivity(context: BaseActivity, mapMerchantModel: MapMerchantModel, requestCode: Int) {
            val intent = Intent(context, CommitCommentActivity::class.java)
            intent.putExtra(AppConstants.EXTRA_DATA, mapMerchantModel)
            context.startActivityForResult(intent, requestCode)
        }

    }

    override fun generateLayoutId() = R.layout.activity_commit_comment

    override fun onCreate(savedInstanceState: Bundle?) {
        hideStatusbar()
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        val mapMerchantModel = intent.getSerializableExtra(AppConstants.EXTRA_DATA)
                as MapMerchantModel?
        merchantId = mapMerchantModel?.merchantId ?: ""

        tv_shop_name.text = mapMerchantModel?.merchantName
        tv_location_name.text = mapMerchantModel?.tradeAreaName


        rateBar.setStar(0f)
        tv_rate_result.text = ""
        rateBar.setOnRatingChangeListener {
            Log.e("SONGJIE", "sdsd$it")
            when (it) {
                1f -> {
                    tv_rate_result.text = "非常不满意，各方面都很差"
                    rateStarCount = 1
                }
                2f -> {
                    tv_rate_result.text = "不满意，比较差"
                    rateStarCount = 2
                }
                3f -> {
                    tv_rate_result.text = "一般，还需改善"
                    rateStarCount = 3
                }
                4f -> {
                    tv_rate_result.text = "比较满意，可以优化"
                    rateStarCount = 4
                }
                5f -> {
                    tv_rate_result.text = "非常满意，棒棒哒!"
                    rateStarCount = 5
                }
            }
        }

        etName.filters = arrayOf(emojiFilter, specicalCharacter, spaceFilter, InputFilter.LengthFilter(5))

        ll_switchBottom.setOnClickListener(this)

        addPureTextView()

        checkIsOperated()
    }

    fun addPureTextView() {
        llContainer.removeAllViews()
        pureTextView = LayoutInflater.from(this).inflate(R.layout.item_pure_text_input, null)

        pureTextView?.findViewById<View>(R.id.ll_voice)?.setOnClickListener(this)
        ll_switch.setOnClickListener(this)
        tvCommitComment.setOnClickListener(this)
        llContainer.addView(pureTextView)
    }

    private fun checkIsOperated() {
        App.mApp.lastOperateTime = System.currentTimeMillis()
        disposable = Observable.interval(5, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val currentTime = System.currentTimeMillis()
                    val diff = currentTime - App.mApp.lastOperateTime
                    Log.e("SONGJIE", "====>dispose=====$diff")
                    if (diff > 5 * 60 * 1000) {
                        LogTool.saveLog("10分钟没有操作，回到欢迎页面")
                        startActivity(Intent(this@CommitCommentActivity, SplashActivity::class.java))
                        finish()
                    }
                }, { })
    }

    @SuppressLint("CheckResult")
    override fun onUploadAudio(filePath: String) {
        //上传音频
        showWaitDialog()
        RetrofitClient.getInstance().resolveAudio(filePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dismissWaitDialog()
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
//                        CommonUtil.showShortToast(it.message)
                        LogTool.saveLog("提交评价==>${it.message}")
                        return@subscribe
                    }

                    isVoice = true

                    val voiceRecognizeModel = it.data
                    voiceUrl = voiceRecognizeModel.voiceUrl
                    inputContent = voiceRecognizeModel.message

                    llContainer.removeAllViews()
                    val v = LayoutInflater.from(this).inflate(R.layout.item_voice_turn_commit, null)
                    v.findViewById<TextView>(R.id.tv_turn_text).text = voiceRecognizeModel?.message
                    val voiceIcon = v.findViewById<GifImageView>(R.id.iv_voice_icon)
                    v.findViewById<ImageView>(R.id.iv_close)?.setOnClickListener {
                        inputContent = ""
                        voiceUrl = ""
                        isVoice = false
                        AudioManager.terminatePlay()
                        addPureTextView()

                    }
                    v.findViewById<ImageView>(R.id.iv_voice_icon).setOnClickListener {
                        if (AudioManager.isPlaying()) {
                            return@setOnClickListener
                        }
                        voiceIcon.setImageResource(R.drawable.gif_voice_play_list)
                        LogTool.saveLog("播放音频")

                        val gifDrawable = voiceIcon.drawable as GifDrawable
                        gifDrawable.start()
                        AudioManager.playOnlineAudio(voiceRecognizeModel.voiceUrl)
                        AudioManager.setOnCompletionListener(object : AudioManager.OnCompletionListener {
                            override fun onCompletion() {
                                gifDrawable.stop()
                            }
                        })

                    }
                    llContainer.addView(v)

                }, {
                    dismissWaitDialog()
                    CommonUtil.showShortToast("音频解析失败")
                    LogTool.saveLog("解析音频文件失败==>${it.message}")
                })
    }


    private fun hideStatusbar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_voice -> {
                val dialog = RecordDialogFragment().setTitle("按住说话 最多60秒")
                dialog.setOnUploadAudioListener(this)
                dialog.show(supportFragmentManager, "dialog")
            }
            R.id.ll_switch, R.id.ll_switchBottom -> finish()
            R.id.tvCommitComment -> {
                val userName = etName.text.toString()
                if (TextUtils.isEmpty(userName)) {
                    CommonUtil.showShortToast("请输入用户姓名")
                    return
                }
                if (rateStarCount == 0) {
                    CommonUtil.showShortToast("请打个分吧~")
                    return
                }

                if (!isVoice) {
                    inputContent = pureTextView?.findViewById<TextView>(R.id.etComment)?.text.toString().trim()
                    if (TextUtils.isEmpty(inputContent)) {
                        CommonUtil.showShortToast("请输入评价内容")
                        return
                    }
                } else {
                    if (TextUtils.isEmpty(inputContent.trim())) {
                        CommonUtil.showShortToast("语音转化失败，请重新录入")
                        return
                    }
                }


                commitComment = CommitCommentModel(inputContent, merchantId, "", userName,
                        rateStarCount, voiceUrl)

                doCommitComment()
            }
        }

    }

    private fun doCommitComment() {
        showWaitDialog()
        RetrofitClient.getInstance().addComment(commitComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    dismissWaitDialog()
                    if (it.code.toString() != AppConstants.RESPONSE_SUCCESS) {
                        CommonUtil.showShortToast(it.message)
                        LogTool.saveLog("提交评价==>${it.message}")
                        return@subscribe
                    }

                    val dialog = CommitSuccessDialog()
                    dialog.show(supportFragmentManager, "dialog")
                    dialog.setOnDismissListener(object : CommitSuccessDialog.OnDismissListener {
                        override fun onDismiss() {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    })
                }, {
                    dismissWaitDialog()
                    LogTool.saveLog("提交评论失败===>${it.message}")
                })

    }

    override fun onDestroy() {
        super.onDestroy()
        if (AudioManager.isPlaying()) {
            AudioManager.terminatePlay()
        }
        AudioManager.removeListener()
        if (disposable != null) {
            disposable?.dispose()
        }
    }


}