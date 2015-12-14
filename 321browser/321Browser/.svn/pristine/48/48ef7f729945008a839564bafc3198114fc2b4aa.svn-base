

package cn.hi321.browser.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the EventController.
 */
public final class EventController {
	
	private List<IDownloadEventsListener> mDownloadListeners;
	
	/**
	 * Holder for singleton implementation.
	 */
	private static class EventControllerHolder {
		private static final EventController INSTANCE = new EventController();
	}
	
	/**
	 * Get the unique instance of the Controller.
	 * @return The instance of the Controller
	 */
	public static EventController getInstance() {
		return EventControllerHolder.INSTANCE;
	}
	
	/**
	 * Private Constructor.
	 */
	private EventController() {
		mDownloadListeners = new ArrayList<IDownloadEventsListener>();
	}
	
	/**
	 * Add a listener for download events.
	 * @param listener The listener to add.
	 */
	public synchronized void addDownloadListener(IDownloadEventsListener listener) {
		
		if (!mDownloadListeners.contains(listener)) {
			mDownloadListeners.add(listener);
		}
	}
	
	/**
	 * Remove a listener for download events.
	 * @param listener The listener to remove.
	 */
	public synchronized void removeDownloadListener(IDownloadEventsListener listener) {
		mDownloadListeners.remove(listener);
	}
	
	/**
	 * Trigger a download event.
	 * @param event The event.
	 * @param data Additional data.
	 */
	public synchronized void fireDownloadEvent(String event, Object data) {
		Iterator<IDownloadEventsListener> iter = mDownloadListeners.iterator();
		while (iter.hasNext()) {
			iter.next().onDownloadEvent(event, data);
		}
	}

}
