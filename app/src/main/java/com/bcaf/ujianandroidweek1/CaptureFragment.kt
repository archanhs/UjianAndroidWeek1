package com.bcaf.ujianandroidweek1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.checkSelfPermission
import kotlinx.android.synthetic.main.fragment_capture.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CaptureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CaptureFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var isAddImage:Boolean = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CaptureFragment.
         */
        private val REQUEST_CODE_PERMISSION = 999;
        private val CAMERA_REQUEST_CAPTURE = 666;
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CaptureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cardCamera.setOnClickListener(View.OnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions,REQUEST_CODE_PERMISSION)
                } else{
                    captureCamera()
                }
            }
        })
        btnCamera.setOnClickListener(View.OnClickListener {
            if (!isAddImage){
                Toast.makeText(activity,"Foto Belum Ada", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(activity,"Sukses Check in", Toast.LENGTH_LONG).show()
                isAddImage = false;
//                (activity as MainActivity).onBackPressed();
                (activity as MainActivity).loadFragment(SuccessFragment.newInstance("",""))
            }
        })

    }

    fun captureCamera(){
        val takeCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takeCamera, CAMERA_REQUEST_CAPTURE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAMERA_REQUEST_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            val bitmapImage = data?.extras?.get("data") as Bitmap;
            imgCamera.setImageBitmap(bitmapImage);
            isAddImage = true;
            (activity as MainActivity).saveImage(bitmapImage);
        }else if(resultCode == AppCompatActivity.RESULT_CANCELED){
            Toast.makeText(activity,"Foto Dibatalkan", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CaptureFragment.REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED){
                    captureCamera();
                }else{
                    Toast.makeText(activity,"Maaf Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}