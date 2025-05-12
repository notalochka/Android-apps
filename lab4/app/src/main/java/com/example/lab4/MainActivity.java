package com.example.lab4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private VideoView videoView;
    private ImageView audioCover;
    private TextView tvFileName;
    private MediaPlayer mediaPlayer;
    private Button btnPlay, btnPause, btnStop, btnSelectFile;
    private Uri mediaUri;
    private boolean isVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        audioCover = findViewById(R.id.audioCover);
        tvFileName = findViewById(R.id.tvFileName);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnSelectFile = findViewById(R.id.btnSelectFile);

        btnSelectFile.setOnClickListener(v -> openFilePicker());
        btnPlay.setOnClickListener(v -> playMedia());
        btnPause.setOnClickListener(v -> pauseMedia());
        btnStop.setOnClickListener(v -> stopMedia());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            mediaUri = data.getData();
            String fileName = getFileName(mediaUri);
            tvFileName.setText(fileName);
            tvFileName.setVisibility(View.VISIBLE);

            String mimeType = getContentResolver().getType(mediaUri);

            if (mimeType != null) {
                if (mimeType.startsWith("video/")) {
                    setupVideoPlayer();
                } else if (mimeType.startsWith("audio/")) {
                    setupAudioPlayer();
                } else {
                    Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void setupVideoPlayer() {
        isVideo = true;
        videoView.setVisibility(View.VISIBLE);
        audioCover.setVisibility(View.GONE);
        videoView.setVideoURI(mediaUri);
        enableControls(true);
    }

    private void setupAudioPlayer() {
        isVideo = false;
        videoView.setVisibility(View.GONE);
        audioCover.setVisibility(View.VISIBLE);
        mediaPlayer = MediaPlayer.create(this, mediaUri);
        enableControls(true);
    }

    private void enableControls(boolean enabled) {
        btnPlay.setEnabled(enabled);
        btnPause.setEnabled(enabled);
        btnStop.setEnabled(enabled);
    }

    private void playMedia() {
        if (isVideo) {
            videoView.start();
        } else {
            mediaPlayer.start();
        }
    }

    private void pauseMedia() {
        if (isVideo) {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    private void stopMedia() {
        if (isVideo) {
            videoView.stopPlayback();
            videoView.setVideoURI(mediaUri); // Reset for next play
        } else {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (videoView != null) {
            videoView.suspend();
        }
    }
}