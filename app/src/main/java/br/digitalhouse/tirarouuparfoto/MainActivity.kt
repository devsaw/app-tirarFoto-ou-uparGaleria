package br.digitalhouse.tirarouuparfoto

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        botaoTirar()
        botaoAbrir()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val foto = data?.getParcelableExtra<Bitmap>("data")
            var imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(foto)

            var extras = data?.extras
            var img = extras!!.get("data")
            imageView.setImageBitmap(img as Bitmap)

        }

        if (requestCode == 2) {
            val source = ImageDecoder.createSource(this.contentResolver, data?.data!!)
            val bitmap = ImageDecoder.decodeBitmap(source)
            var imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(bitmap)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1)
            }
        }

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
    }

    fun botaoAbrir() {
        val buttonAbrir = findViewById<Button>(R.id.btnabrir)
        buttonAbrir.setOnClickListener { validaAbrir() }
    }

    fun botaoTirar() {
        val buttonTirar = findViewById<Button>(R.id.btntirar)
        buttonTirar.setOnClickListener { validaTirar() }
    }

    fun validaTirar() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                1
            )
        } else {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }
    }


    fun validaAbrir() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2
            )
        } else {
            var intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }
}