package com.wangxiaobao.gsj.base;

import android.graphics.Color;
import android.view.View;

import com.wangxiaobao.gsj.view.BottomMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by candy on 16-12-15.
 */
public class BottomMenuManager implements View.OnClickListener {


    List<BottomMenuItem> mBottomMenuItemList = new ArrayList<>();

    public int getSelectedId() {
        return mSelectedId;
    }

    private int mSelectedId;

    public void setSelectedId(int selectedId) {
        this.mSelectedId = selectedId;
        updateSelectStatus(mSelectedId);
    }

    public void addChild(BottomMenuItem bottomMenuItem) {
        mBottomMenuItemList.add(bottomMenuItem);
    }

    private void updateSelectStatus(int selectedID) {
        for (BottomMenuItem bottomMenuItem : mBottomMenuItemList) {
            if (bottomMenuItem.getId()==selectedID){
                bottomMenuItem.setSelected(true);
                bottomMenuItem.setBackgroundColor(Color.TRANSPARENT);

            }else {
                bottomMenuItem.setSelected(false);
                bottomMenuItem.setBackgroundColor(Color.WHITE);

            }
        }
    }

    @Override
    public void onClick(View v) {
        updateSelectStatus(v.getId());
    }
}
