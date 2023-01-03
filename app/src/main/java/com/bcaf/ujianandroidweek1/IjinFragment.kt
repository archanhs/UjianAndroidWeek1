package com.bcaf.ujianandroidweek1

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_capture.*
import kotlinx.android.synthetic.main.fragment_ijin.*
import kotlinx.android.synthetic.main.fragment_menu.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.Year
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IjinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IjinFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val  c = Calendar.getInstance();
    var isAddImage1 = false;
    var isAddImage2 = false;
    var isAddImage3 = false;

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
        return inflater.inflate(R.layout.fragment_ijin, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IjinFragment.
         */
        private val REQUEST_CODE_PERMISSION = 999;
        private val CAMERA_REQUEST_CAPTURE_1 = 666;
        private val CAMERA_REQUEST_CAPTURE_2 = 777;
        private val CAMERA_REQUEST_CAPTURE_3 = 888;

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IjinFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val spinnerItem = arrayOf<String>("Izin Sakit","Izin Anak Sakit")
        val adapter = ArrayAdapter((activity as MainActivity),
            android.R.layout.simple_spinner_item, spinnerItem)
        spinnerTypeIzin.adapter = adapter

        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        editDarITanggal.setText(sdf.format(c.getTime()));
        editSampaiTanggal.setText(sdf.format(c.getTime()));

        imgDariTanggal.setOnClickListener(View.OnClickListener {
            pickDate(1);
        })
        imgSampaiTanggal.setOnClickListener(View.OnClickListener {
            pickDate(2);
        })

        imgCamera1.setOnClickListener(View.OnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkPermission()){
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, IjinFragment.REQUEST_CODE_PERMISSION)
                } else{
                    captureCamera(CAMERA_REQUEST_CAPTURE_1)
                }
            }
        })
        imgCamera2.setOnClickListener(View.OnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkPermission()){
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, IjinFragment.REQUEST_CODE_PERMISSION)
                } else{
                    captureCamera(CAMERA_REQUEST_CAPTURE_2)
                }
            }
        })
        imgCamera3.setOnClickListener(View.OnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkPermission()){
                    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, IjinFragment.REQUEST_CODE_PERMISSION)
                } else{
                    captureCamera(CAMERA_REQUEST_CAPTURE_3)
                }
            }
        })

        btnKirim.setOnClickListener(View.OnClickListener {
            if (editPerihal.text.equals("")||editKeterangan.text.equals("")||!isAddImage1||!isAddImage2||!isAddImage3){
                Toast.makeText(activity,"Gagal Kirim Izin, Pastikan semua foto dan form terisi !", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(activity,"Berhasil Kirim Izin", Toast.LENGTH_LONG).show()
                isAddImage1 = false;
                isAddImage2 = false;
                isAddImage3 = false;
                (activity as MainActivity).onBackPressed();
            }
        })


    }
    fun captureCamera(code:Int){
        val takeCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takeCamera, code);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==  IjinFragment.CAMERA_REQUEST_CAPTURE_1 && resultCode == AppCompatActivity.RESULT_OK){
            val bitmapImage = data?.extras?.get("data") as Bitmap;
            imgCamera1.setImageBitmap(bitmapImage);
            isAddImage1 = true;
            (activity as MainActivity).saveImage(bitmapImage);
        }else if(requestCode ==  IjinFragment.CAMERA_REQUEST_CAPTURE_2 && resultCode == AppCompatActivity.RESULT_OK){
            val bitmapImage = data?.extras?.get("data") as Bitmap;
            imgCamera2.setImageBitmap(bitmapImage);
            isAddImage2 = true;
            (activity as MainActivity).saveImage(bitmapImage);
        }else if(requestCode ==  IjinFragment.CAMERA_REQUEST_CAPTURE_3 && resultCode == AppCompatActivity.RESULT_OK){
            val bitmapImage = data?.extras?.get("data") as Bitmap;
            imgCamera3.setImageBitmap(bitmapImage);
            isAddImage3 = true;
            (activity as MainActivity).saveImage(bitmapImage);
        }
    }


    fun checkPermission():Boolean{
        if(activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || activity?.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, IjinFragment.REQUEST_CODE_PERMISSION)
            return true;
        }else{
            return false;
        }

    }


    fun pickDate(form:Number){

        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int
            ){
                c.set(Calendar.YEAR,year)
                c.set(Calendar.MONTH,monthOfYear)
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth)

                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                if (form.equals(1)){
                    editDarITanggal.setText(sdf.format(c.getTime()));
                }else{
                    editSampaiTanggal.setText(sdf.format(c.getTime()));
                }
            }
        }

        DatePickerDialog((activity as MainActivity),dateSetListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(
            Calendar.DAY_OF_MONTH)).show() //parameter c.get adalah inisialisasi awal kalender yaitu hari ini
    }


}