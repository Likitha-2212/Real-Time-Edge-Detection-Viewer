package com.example.edgeapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var textureView: TextureView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textureView = TextureView(this)
        setContentView(FrameLayout(this).apply { addView(textureView) })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera()
            else Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show()
        }

    private fun startCamera() {
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(st: SurfaceTexture, width: Int, height: Int) {
                val surface = Surface(st)
                // In a real app, set up Camera2 and feed frames to native processing and GL renderer.
                // For this scaffold we call native init with width/height
                System.loadLibrary("edge_native")
                nativeInit(width, height)
            }
            override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, width: Int, height: Int) {}
            override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean { nativeStop(); return true }
            override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}
        }
    }

    companion object {
        @JvmStatic external fun nativeInit(w:Int, h:Int)
        @JvmStatic external fun nativeStop()
    }
}
