package me.ibore.libs.demo

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import me.ibore.libs.constant.PermissionConstants
import me.ibore.libs.util.PermissionUtils
import me.ibore.libs.util.ToastUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionUtils
                .permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.Callback {
                    override fun onGranted(permissionsGranted: MutableList<String>?) {
                        ToastUtils.showShort("权限通过")
                    }

                    override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage("dddddddddd")
                                .setPositiveButton("确认") { dialog, _ ->
                                    dialog.dismiss()
                                    PermissionUtils.openAppSettings()
                                }
                                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                                .create().show()
                    }
                })
                .request()

    }
}