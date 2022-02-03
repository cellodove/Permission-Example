package com.peter.permission

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.peter.permission.databinding.FragmentIntroPermissionBinding

class IntroPermissionFragment : Fragment() {
    private lateinit var binding : FragmentIntroPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroPermissionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPermission.setOnClickListener {
            permissionLauncher.launch(Constants.PERMISSIONS)
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        Log.e("permissionLauncher","permissionLauncher: $result")
        val deniedList: List<String> = result.filter {
            !it.value
        }.map {
            it.key
        }
        if (result.none { !it.value }) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            requireActivity().finishAffinity()
        } else {
            when {
                deniedList.isNotEmpty() -> {
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission)) "DENIED" else "EXPLAINED"
                    }
                    map["DENIED"]?.let {
                        // 거부 한 번 했을경우 재요청
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        requireActivity().finishAffinity()
                    }
                    map["EXPLAINED"]?.let {
                        // 거부 두 번 했을경우 설정
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.apply {
                            setMessage("권한 설정을 하시겠습니까?")
                            setPositiveButton("확인"
                            ) { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                                    Uri.parse("package:${requireContext().packageName}"))
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
                else -> {
                    // All request are permitted
                }
            }
        }
    }
}