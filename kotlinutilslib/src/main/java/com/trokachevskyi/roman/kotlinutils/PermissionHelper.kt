package softfarm.helpers

import android.Manifest

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Created by duke0808 on 02.11.16.
 */

object PermissionHelper {
    fun askLocationPermissions(callback: PermissionCallback?) {
        if (Dexter.isRequestOngoing()) {
            return
        }
        val permissionListener = object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                callback?.onGranted()
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                callback?.onDenied()
                token.continuePermissionRequest()
            }
        }
        Dexter.checkPermissions(permissionListener, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun askStoragePermission(callback: PermissionCallback?) {
        if (Dexter.isRequestOngoing()) {
            return
        }
        val permissionListener = object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                callback?.onGranted()
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                callback?.onDenied()
                token.continuePermissionRequest()
            }
        }
        Dexter.checkPermissions(permissionListener, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }

    interface PermissionCallback {
        fun onGranted()

        fun onDenied()
    }
}
