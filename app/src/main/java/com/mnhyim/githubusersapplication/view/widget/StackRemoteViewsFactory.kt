package com.mnhyim.githubusersapplication.view.widget

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.mnhyim.githubusersapplication.R
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.mnhyim.githubusersapplication.db.MappingHelper
import com.mnhyim.githubusersapplication.model.User


class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val TAG: String = StackRemoteViewsFactory::class.java.simpleName

    private val mWidgetItems = ArrayList<User>()
    private var cursor: Cursor? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        mWidgetItems.clear()
        cursor?.close()
    }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged")
        cursor?.close()

        val identityToken = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )
        mWidgetItems.addAll(MappingHelper.mapCursorToArrayList(cursor))

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d(TAG, "getViewAt")
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bitmap: Bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].avatar_url)
            .submit(512, 512)
            .get()

        rv.setImageViewBitmap(R.id.imageView, bitmap)
        rv.setTextViewText(R.id.tv_widget_name, mWidgetItems[position].login)
        rv.setTextViewText(R.id.tv_widget_type, mWidgetItems[position].type)

        return rv
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}
