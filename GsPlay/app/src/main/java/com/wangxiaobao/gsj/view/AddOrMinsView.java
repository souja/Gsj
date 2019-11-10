package com.wangxiaobao.gsj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangxiaobao.waiter.R;


/**
 * Created by Administrator on 2016-04-08.
 */
public class AddOrMinsView extends LinearLayout implements View.OnClickListener {
    ImageView mAdd, mRemove;
    TextView mCountTv;
    int mCount;

    private Context mContext;


    public void setModifyClickListener(OnModifyClickListener modifyClickListener) {
        this.mModifyClickListener = modifyClickListener;
    }

    private OnModifyClickListener mModifyClickListener;

    public AddOrMinsView(Context context) {
        super(context);
    }

    public AddOrMinsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddOrMinsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView(getContext());
    }

    protected void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.custom_pls_mins, this, true);
        mAdd = (ImageView) view.findViewById(R.id.add);
        mRemove = (ImageView) view.findViewById(R.id.remove);
        mCountTv = (TextView) view.findViewById(R.id.count);

        mAdd.setOnClickListener(this);
        mRemove.setOnClickListener(this);
//        final Drawable originBitmapDrawable = mRemove.getDrawable();
//        mRemove.setBackgroundDrawable(tintDrawable(originBitmapDrawable, ColorStateList.valueOf(Color.GREEN)));
    }


//    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
//        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
//        DrawableCompat.setTintList(wrappedDrawable, colors);
//        return wrappedDrawable;
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add:
                if (mModifyClickListener != null) {
                    mModifyClickListener.onAdd();
                }

                break;

            case R.id.remove:
                if (mModifyClickListener != null) {
                    mModifyClickListener.onRemove();
                }
                break;
        }

    }


    public interface OnModifyClickListener {
        void onAdd();

        void onRemove();
    }


    public void add() {
        mCountTv.setText(mCount++ + "");
    }

    public void remove() {
        mCountTv.setText(mCount-- + "");
    }


    public void changeCount(int count) {
        mCount = count;
        mCountTv.setText(count + "");
    }


}
