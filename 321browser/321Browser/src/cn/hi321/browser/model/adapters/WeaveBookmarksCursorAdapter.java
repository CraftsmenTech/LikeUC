
package cn.hi321.browser.model.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import cn.hi321.browser.providers.WeaveColumns;
import cn.hi321.browser2.R;

public class WeaveBookmarksCursorAdapter extends SimpleCursorAdapter {

	public WeaveBookmarksCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View superView = super.getView(position, convertView, parent);
		
		Cursor c = getCursor();
		
		boolean isFolder = c.getInt(c.getColumnIndex(WeaveColumns.WEAVE_BOOKMARKS_FOLDER)) > 0 ? true : false;
		
		ImageView iconView = (ImageView) superView.findViewById(R.id.BookmarkRow_Thumbnail);
		TextView urlView = (TextView) superView.findViewById(R.id.BookmarkRow_Url);
		
		if (isFolder) {
			urlView.setVisibility(View.GONE);					
			iconView.setImageResource(R.drawable.folder_icon);
		} else {						
			urlView.setVisibility(View.VISIBLE);
			iconView.setImageResource(R.drawable.fav_icn_default);
		}
		
		return superView;
	}

}
