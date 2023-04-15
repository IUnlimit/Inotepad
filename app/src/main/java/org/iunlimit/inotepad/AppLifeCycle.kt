package org.iunlimit.inotepad

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import org.iunlimit.inotepad.sdk.generateSTS

class AppLifeCycle: Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        generateSTS()
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        val file = activity.cacheDir
        if (file != null && file.exists() && file.isDirectory && file.listFiles() != null) {
            for (item in file.listFiles()!!) item.delete()
            file.delete()
        }
        activity.baseContext.let {
            it.deleteDatabase("webview.db")
            it.deleteDatabase("webviewCache.db")
        }
        Log.v("app", "Clear cache")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

}