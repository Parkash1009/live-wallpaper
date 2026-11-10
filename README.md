# Video Live Wallpaper

A small Android app that lets you choose a video from your phone and set it as an Android live wallpaper.

## Requirements
- Android Studio
- Android SDK 35
- A phone running Android 6.0 (API 23) or newer

## Build
1. Open this folder in Android Studio.
2. Let Gradle sync.
3. Connect your Android phone with USB debugging enabled.
4. Press Run.
5. In the app, tap "Choose Video".
6. Pick an MP4/video.
7. Tap "Set as Live Wallpaper".
8. Confirm Android's wallpaper screen.

The selected video's URI is saved and the wallpaper service loops the video.

## Notes
- Some phone manufacturers restrict live wallpapers on the lock screen.
- Short portrait videos are recommended for best results.
- If your video provider does not allow persistable URI permissions, select the video again after a reboot.
