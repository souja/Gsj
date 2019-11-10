package com.wangxiaobao.gsj.http;

import com.wangxiaobao.gsj.base.DialogEvent;

/**
 * Created by candy on 17-10-30.
 */
public class RetrofitObserverOption {
    public static final int FRAGMENT_VIEW_MODE = 1;
    public static final int ACTIVITY_VIEW_MODE = 2;
    private int viewMode = FRAGMENT_VIEW_MODE;
    private boolean needShowHttpErrorLayout;
    private boolean needShowDialog=true;
    private DialogEvent dialogEvent;
    private String successMessage;
    private String errorMessage;

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
    }

    public boolean isNeedShowHttpErrorLayout() {
        return needShowHttpErrorLayout;
    }

    public void setNeedShowHttpErrorLayout(boolean needShowHttpErrorLayout) {
        this.needShowHttpErrorLayout = needShowHttpErrorLayout;
    }

    public DialogEvent getDialogEvent() {
        return dialogEvent;
    }

    public void setDialogEvent(DialogEvent dialogEvent) {
        this.dialogEvent = dialogEvent;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public boolean isNeedShowDialog() {
        return needShowDialog;
    }

    public void setNeedShowDialog(boolean needShowDialog) {
        this.needShowDialog = needShowDialog;
    }

}
