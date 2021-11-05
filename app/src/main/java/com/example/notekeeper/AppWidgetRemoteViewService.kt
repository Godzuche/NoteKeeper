package com.example.notekeeper

import android.content.Intent
import android.widget.RemoteViewsService

class AppWidgetRemoteViewService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        //use appContext instead of any specific activity context since we want this to work even when the activity is closed
        return AppWidgetRemoteViewFactory(applicationContext)
    }
}