package com.example.ramzanullah.exoplayerdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoVIewActivity extends AppCompatActivity {

    private static final String Play_Back_Position = "video_view_position";


    private int playbackPosition = 0;
    private MediaController mediaControls;
    private Uri uri;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @BindView(R.id.video_view)
    VideoView myVideoView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.video_view_container)
    FrameLayout videoContainer;


    private String videoURL = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);

        sharedpreferences = getSharedPreferences("VideoVIew", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        updatePlaybackTime();

        mediaControls = new MediaController(this);


        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaControls.setAnchorView(videoContainer);
                myVideoView.setMediaController(mediaControls);
                myVideoView.seekTo(playbackPosition);
                myVideoView.start();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            myVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    if(what == mp.MEDIA_INFO_VIDEO_RENDERING_START){
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    return true;
                }
            });
        }

    }

    public void updatePlaybackTime(){

        playbackPosition = sharedpreferences.getInt(Play_Back_Position, 0);

    }


    public void savePlaybackTime(){

        playbackPosition = myVideoView.getCurrentPosition();
        editor.putInt(Play_Back_Position, playbackPosition);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updatePlaybackTime();
        uri = Uri.parse(videoURL);
        myVideoView.setVideoURI(uri);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        myVideoView.pause();
        savePlaybackTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePlaybackTime();
        myVideoView.stopPlayback();
    }



}
