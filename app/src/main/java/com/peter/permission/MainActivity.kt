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
            if (checkDeniedPermission()){
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    .add(R.id.container, IntroPermissionFragment())
                    .commit()
            }else{
                val dialog = AlertDialog.Builder(this)
                dialog.apply {
                    title = "권한 설정"
                    setMessage("권한 설정을 하시겠습니까?")
                    setPositiveButton("확인"
                    ) { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    setNegativeButton("취소"){
                        _,_ ->
                        UInt
                    }
                }
                dialog.create().show()

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