package cn.hi321.browser.utils;

public interface CommonTipFloatWindowInterface {
	/**
	 * push弹窗的海报图片
	 */
    public static final int GET_POSTER_IMAGE = 0;
    
    /**
     * 用户点击了back键
     */
    public static final int  USER_PRESSED_BACK = 1;
    
    /**
     * 解屏两分钟后回复用户手机为锁屏状态
     */
    public static final int ROLL_BACK_TO_LOCKSCREEN = USER_PRESSED_BACK + 1;
    
    /**
     * 解屏超时时间 2分钟
     */
    public static final int UNLOCK_TIME_OUT =  120000;
    
    /**
     * 用户点击了home键
     */
    public static final int  USER_PRESSED_HOME = 2;
    
    public static final int STATR_CREATE_SHORTCUT = 3;
    
    public static final String CURRENT_OBJECT = "current_object";
    
    public static final String POP_SWITCH = "popswitch";
	
    public static final  String ON_OFF = "OnOff";
    
    public static final String SCREEN_LOCK = "screen_lock";
    
    public static final String USER_CLOSE_WINDOW = "useclose";
    
    public static final String IS_FROM_THIRD_PUSH = "isfromthirdpush";
    
    public static final String LONG_MEDIA = "long";
	
    public static final String SHORT_MEDIA = "short";
	
    public static final String LIVE_MEDIA = "live";
	
    public static final String LOCAL_NOTIFICATION = "local_notification";
    
}
