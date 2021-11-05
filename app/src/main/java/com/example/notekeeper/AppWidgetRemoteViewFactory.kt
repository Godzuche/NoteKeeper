package com.example.notekeeper

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class AppWidgetRemoteViewFactory(val context: Context):
RemoteViewsService.RemoteViewsFactory {
    override fun onCreate() {
        //nothing yet since our dataset doesn't need an initialization
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return DataManager.notes.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.note_keeper_app_widget)
        rv.setTextViewText(R.id.note_title, DataManager.notes[position].title)
        return rv
    }

    override fun getLoadingView(): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.note_keeper_app_widget)
        return rv
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}