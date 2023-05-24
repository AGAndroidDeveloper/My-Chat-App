package com.ankitgupta.dev.mychatapp.model

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.ankitgupta.dev.mychatapp.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ViewModel( private val context: Context)  {









    fun extracted() {
        if (ContextCompat.checkSelfPermission(
             context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askPermission()
        } else {
            showDialog()
        }
    }
     fun showDialog(){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("select Profile Picture")
        dialog.setItems(arrayOf("take photo from camera","use photo from gallery")) { dialog1, which ->
            when (which) {
                0 -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    Log.e("intent", "$intent")
                   startActivityForResult(context as Activity, intent, CAMERA, null)


                }

                1 -> {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(context as Activity, intent, GALLERY_REQUEST_CODE, null)
                }
            }

        }

         dialog.setCancelable(true)
        dialog.show()
    }




    fun saveImageToExternalStorage(bitmap: Bitmap): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API for Android 10 and higher
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val resolver = context.contentResolver
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let {
                try {
                    val outputStream = resolver.openOutputStream(it)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    outputStream?.close()
                    return it
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            null
        } else {
            // For devices below Android 10, save the image to the Pictures directory
            val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }

            val imageFile = File(imageDir, "image_${System.currentTimeMillis()}.jpg")
            try {
                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.close()
                return Uri.fromFile(imageFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            null
        }
    }

     private fun askPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        Toast.makeText(context,"all permission are granted", Toast.LENGTH_SHORT).show()
                        if (report.areAllPermissionsGranted()){
                            showDialog()

                        }else{
                            Toast.makeText(context,"all permission are not granted", Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialog()
                    }


                }).check()
        }else{
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        Toast.makeText(context,"all permission are granted", Toast.LENGTH_SHORT).show()
                        if (report.areAllPermissionsGranted()){
                            showDialog()

                        }else{
                            Toast.makeText(context,"all permission are not granted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialog()
                    }
                }).check()

        }
    }

    fun showRationalDialog() {
        val dialog : AlertDialog.Builder = AlertDialog.Builder(context)
            .setMessage(
                "in order to use the functionality of the app give permission"
            )
            .setIcon(R.drawable.baseline_all_inclusive_24)
            .setPositiveButton("setting") { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", ContactsContract.Directory.PACKAGE_NAME, null)
                intent.data = uri
                Log.e("uri","$uri")
                startActivity(context,intent,null)
                dialog.dismiss()
            }

            .setNegativeButton("cancel") { a,g ->
                a.dismiss()
            }

        dialog.show()

    }

    companion object{
        const val CAMERA = 101
        const val GALLERY_REQUEST_CODE = 102
        const val SEND = "send"
    }


}