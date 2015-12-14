package cn.hi321.browser.download;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.FileObserver;
import cn.hi321.browser.model.DownloadInfo;

public class SDCardListener extends FileObserver {
	
	private Context context = null;
	Intent i = null;

	public SDCardListener(Context c, ArrayList<DownloadInfo> aList,String path) {
		super(path);
		this.context = c;
		i = new Intent();
		i.setAction("com.funshion.video.download");
		i.putExtra("stateChange", "stateChange");
	}

	@Override
	public void onEvent(int event, String path) {
		switch(event) {  
		case FileObserver.DELETE:
        case FileObserver.MOVED_FROM:
        	context.sendBroadcast(i);
            break;
        }

	}

}
