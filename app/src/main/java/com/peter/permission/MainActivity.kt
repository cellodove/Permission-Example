package com.peter.permission

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.peter.permission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (checkPermission()) {
            Toast.makeText(this,"권한 허용 완료",Toast.LENGTH_SHORT).show()
        }else{
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .add(R.id.container, IntroPermissionFragment())
                .commit()
        }
    }

    private fun checkPermission(): Boolean {
        return Constants.PERMISSIONS.none { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
    }
}