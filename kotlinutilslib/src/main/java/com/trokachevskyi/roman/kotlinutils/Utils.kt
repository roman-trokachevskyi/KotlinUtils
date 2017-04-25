package softfarm.helpers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import java.io.File
import java.net.InetAddress
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by duke0808 on 17.11.16.
 */


fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun Context.shareFile(file: File, context: Context, type: String, chooserTitle: String) {
    PermissionHelper.askStoragePermission(object : PermissionHelper.PermissionCallback {
        override fun onGranted() {
            val shareIntent = Intent()
            val uri = Uri.fromFile(file)
            val resInfoList = context.packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = type
            context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
        }

        override fun onDenied() {
            showToast("Permission error")
        }

    })
}


fun Any.clearDateFromHandM(date: Long): Long {
    val sf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    val d = if (date.equals(0)) Date() else Date(date)
    val format = sf.format(d)
    var parse = Date()
    try {
        parse = sf.parse(format)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return parse.time
}

fun Any.isInternetAvailable(): Boolean {
    try {
        val ipAddr = InetAddress.getByName("google.com")
        return !TextUtils.isEmpty(ipAddr.hostAddress)

    } catch (e: Exception) {
        return false
    }
}