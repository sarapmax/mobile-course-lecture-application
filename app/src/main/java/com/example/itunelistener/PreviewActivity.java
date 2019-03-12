package com.example.itunelistener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PreviewActivity extends AppCompatActivity {
    String title;
    Bitmap cover;
    String url;

    Button btnPlay;
    Button btnPause;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler seekBarUpdateHandler;
    Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        cover = intent.getParcelableExtra("cover");
        url = intent.getStringExtra("url");

        TextView textView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.imageView);

        textView.setText(title);
        imageView.setImageBitmap(cover);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        seekBar = findViewById(R.id.seekBar);

        setUiEnabled(false);

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    setUiEnabled(true);
                }
            });

            mediaPlayer.setDataSource(this, Uri.parse(url));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        seekBarUpdateHandler = new Handler();

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBarUpdateHandler.postDelayed(this, 50);
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUiEnabled(boolean enabled) {
        btnPlay.setEnabled(enabled);
        btnPause.setEnabled(enabled);
        seekBar.setEnabled(enabled);
    }

    public void onButtonClick(View view) {
        if (view == btnPlay) {
            mediaPlayer.start();

            seekBarUpdateHandler.postDelayed(updateSeekBar, 0);
        } else {
            mediaPlayer.pause();

            seekBarUpdateHandler.removeCallbacks(updateSeekBar);
        }
    }
}
