package com.peter.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.peter.permission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (checkPermission()) {

        }else{
            if (checkDeniedPermission()){
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    .add(R.id.container, IntroPermissionFragment())
                    .commit()
            }else{

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:$packageName"))
                startActivity(intent)


/*
                CommonDialog.getInstance(
                    isImgVisible = true,
                    title = getString(R.string.permission_request_title),
                    msg = getString(R.string.permission_request_sub_title),
                    rightBtnText = getString(R.string.confirm),
                    leftBtnText = getString(R.string.cancel),
                    onLeftClick = {
                        finish()
                    },
                    onRightClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                            Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                ).show(supportFragmentManager.beginTransaction(),"")
*/

            }
        }
    }

    private fun checkPermission(): Boolean {
        return Constants.PERMISSIONS.none { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
    }

    private fun checkDeniedPermission(): Boolean {
        return Constants.PERMISSIONS.none { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }
    }
}