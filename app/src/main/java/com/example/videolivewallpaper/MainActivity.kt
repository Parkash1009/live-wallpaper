package com.example.videolivewallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PICK_VIDEO = 1001
        private const val PREFS = "wallpaper_prefs"
        private const val VIDEO_URI = "video_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = findViewById<TextView>(R.id.statusText)
        val selectButton = findViewById<Button>(R.id.selectButton)
        val setButton = findViewById<Button>(R.id.setButton)

        val savedUri = getSharedPreferences(PREFS, MODE_PRIVATE)
            .getString(VIDEO_URI, null)

        if (savedUri != null) {
            status.text = "Video selected. Tap the button below to set it."
            setButton.isEnabled = true
        }

        selectButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "video/*"
                addCategory(Intent.CATEGORY_OPENABLE)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            startActivityForResult(intent, PICK_VIDEO)
        }

        setButton.setOnClickListener {
            val component = ComponentName(this, VideoWallpaperService::class.java)
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component)
            }
            startActivity(intent)
        }
    }

    @Deprecated("Use Activity Result APIs when extending this sample.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != PICK_VIDEO || resultCode != Activity.RESULT_OK) return

        val uri: Uri = data?.data ?: return

        try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) {
            // Some providers do not support persistable permissions.
        }

        getSharedPreferences(PREFS, MODE_PRIVATE)
            .edit()
            .putString(VIDEO_URI, uri.toString())
            .apply()

        findViewById<TextView>(R.id.statusText).text =
            "Video selected. Tap the button below to set it."
        findViewById<Button>(R.id.setButton).isEnabled = true
    }
}
