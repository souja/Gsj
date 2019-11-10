package com.wangxiaobao.pushmanager;
/** 
 * value的长度 
 */  
public class LPosition {
  
	/**
	 * Value长度
	 */
    private int l;
    /**
     * 位置
     */
    private int position;
  
    public LPosition(int l, int position) {
        this.l = l;
        this.position = position;
    }  
  
    public int getL() {
        return l;
    }  
  
    public void setL(int l) {
        this.l = l;
    }  
  
    public int getPosition() {
        return position;
    }  
  
    public void setPosition(int position) {
        this.position = position;
    }  
}  
