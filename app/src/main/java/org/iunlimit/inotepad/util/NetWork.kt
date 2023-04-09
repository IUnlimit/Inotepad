package org.iunlimit.inotepad.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

// 判断网络连接是否可用
fun checkNetworkAvailable(activity: Activity): Boolean {
    val context = activity.applicationContext;
    val manager: ConnectivityManager =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) ?: return false) as ConnectivityManager;

    val info = manager.allNetworkInfo;
    if (info.isNotEmpty()) {
        for (networkInfo in info) {
            if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
    }
    return false;
}