package com.wangxiaobao.gsj.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.common.ScreenUtil;
import com.wangxiaobao.waiter.R;

/**
 * Created by candy on 16-5-26.
 */
public class BottomMenuItem extends RelativeLayout {

    private MediaPlayer mp;
    private TextView mText;
    private TextView mCount;
    private ImageView mIcon;
    private TextView mDot;


    private Context mContext;
    private String mTextContent;
    private int textColor;
    private int mImageSourceId;
    private boolean isPlaying = false;

    public BottomMenuItem(Context context) {
        this(context, null);
        mContext = context;
    }

    public BottomMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        mContext = context;
    }

    public BottomMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BottomMenuItem);
        mTextContent = typedArray.getString(R.styleable.BottomMenuItem_text);
        textColor = typedArray.getResourceId(R.styleable.BottomMenuItem_textColor, -1);
        mImageSourceId = typedArray.getResourceId(R.styleable.BottomMenuItem_src, -1);
        typedArray.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomMenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(mContext).inflate(R.layout.custom_bottom_menu_item, this);
        mText = (TextView) findViewById(R.id.text);
        mIcon = (ImageView) findViewById(R.id.menu_icon);
        mDot = (TextView) findViewById(R.id.dot);
        mText.setText(mTextContent);
        setTextColor(textColor);
        mIcon.setImageResource(mImageSourceId);
        mCount = (TextView) findViewById(R.id.count);
//        ScreenUtil.initScale(this);
    }


    public void setText(String s) {
        mText.setText(s);
    }

    private void playNofitionMusic() {
        if (!isPlaying) {
            isPlaying = true;
            mp = MediaPlayer.create(mContext, R.raw.notification);
            try {
                mp.start();
                LogTool.E("play notification success");
            } catch (IllegalArgumentException e) {
                LogTool.E("play notification fail");
                e.printStackTrace();
                mp.release();
                isPlaying = false;
            } catch (IllegalStateException e) {
                LogTool.E("play notification fail");
                e.printStackTrace();
                mp.release();
                isPlaying = false;
            }

            mp.setOnCompletionListener(mp -> {
                mp.release();
                isPlaying = false;
            });
        }

    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mText.setTextColor(selected ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black));
    }

    public void setTextColor(int textColor) {
        this.mText.setTextColor(getResources().getColor(textColor));
    }

    public void setDotVisibility(int visiablity) {
        mDot.setVisibility(visiablity);
    }

    public void setDotText(String text) {
        mDot.setText(text);
    }

    public void setCount(boolean playMusic, int count) {
        if (count == 0) {
            mCount.setVisibility(GONE);
        } else {
            mCount.setVisibility(VISIBLE);
            if (playMusic) {
                int currentCount = Integer.parseInt(mCount.getText().toString());
                if (count > currentCount) {
                    playNofitionMusic();
                }
            }


        }
        mCount.setText(count + "");

    }

    public void setMenuIcon(int menuIcon) {
        mIcon.setImageResource(menuIcon);
    }
}
