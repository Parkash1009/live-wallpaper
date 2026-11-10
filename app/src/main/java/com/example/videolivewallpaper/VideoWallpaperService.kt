package com.example.videolivewallpaper

import android.media.MediaPlayer
import android.net.Uri
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class VideoWallpaperService : WallpaperService() {

    companion object {
        private const val PREFS = "wallpaper_prefs"
        private const val VIDEO_URI = "video_uri"
    }

    override fun onCreateEngine(): Engine = VideoEngine()

    private inner class VideoEngine : Engine() {
        private var player: MediaPlayer? = null
        private var visible = false

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                startPlayer()
            } else {
                player?.pause()
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            startPlayer(holder)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            stopPlayer()
            super.onSurfaceDestroyed(holder)
        }

        override fun onDestroy() {
            stopPlayer()
            super.onDestroy()
        }

        private fun startPlayer(holder: SurfaceHolder = surfaceHolder) {
            val uriString = getSharedPreferences(PREFS, MODE_PRIVATE)
                .getString(VIDEO_URI, null) ?: return

            stopPlayer()

            try {
                player = MediaPlayer().apply {
                    setDataSource(this@VideoWallpaperService, Uri.parse(uriString))
                    setSurface(holder.surface)
                    isLooping = true
                    setOnPreparedListener { mp ->
                        if (visible || !isPreview) {
                            mp.start()
                        }
                    }
                    prepareAsync()
                }
            } catch (_: Exception) {
                stopPlayer()
            }
        }

        private fun stopPlayer() {
            player?.run {
                try {
                    stop()
                } catch (_: IllegalStateException) {
                }
                release()
            }
            player = null
        }
    }
}
