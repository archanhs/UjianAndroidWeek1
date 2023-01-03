package com.bcaf.ujianandroidweek1

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_capture.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var isAddImage:Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(LoginFragment.newInstance("",""))

//        btnFragmentLogin.setOnClickListener(View.OnClickListener {
//            loadFragment(LoginFragment.newInstance("",""))
//        })
//
//        btnFragmentRegistration.setOnClickListener(View.OnClickListener {
//            loadFragment(RegistrationFragment.newInstance("",""))
//        })
    }
    companion object {
        private val CAMERA_REQUEST_CAPTURE = 666;
    }

    fun loadFragment(fragment: Fragment){
        val fragManager = supportFragmentManager.beginTransaction();
        fragManager.replace(R.id.vFragment,fragment);
        fragManager.addToBackStack(null);
        fragManager.commit();
    }

    fun captureCamera():Boolean{
        val takeCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takeCamera, CAMERA_REQUEST_CAPTURE);
        return isAddImage;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAMERA_REQUEST_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            val bitmapImage = data?.extras?.get("data") as Bitmap;
            imgCamera.setImageBitmap(bitmapImage);
            isAddImage = true;
            saveImage(bitmapImage);
        }else if(resultCode == AppCompatActivity.RESULT_CANCELED){
            Toast.makeText(this,"Foto Dibatalkan", Toast.LENGTH_LONG).show();
        }
    }

    fun saveImage(bitMap : Bitmap){
        val tanggal = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
        val extStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
//        val extStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        val namaFile = extStorage+"/BCAF_"+tanggal+".png";

        var file: File? = null;
        file = File(namaFile);
        file.createNewFile();

        val bos = ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG,0,bos);
        val bitmapData = bos.toByteArray();

        val fos = FileOutputStream(file);

        fos.write((bitmapData));
        fos.flush();
        fos.close();
    }
}