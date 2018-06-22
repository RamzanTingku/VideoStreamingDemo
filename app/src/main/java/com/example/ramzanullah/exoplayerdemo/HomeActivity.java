package com.example.ramzanullah.exoplayerdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String Play_Back_Position = "video_view_position";
    private static final String Play_Back_Position2 = "video_view_position2";

    private int playbackPosition = 0;
    private int playbackPosition2 = 0;

    private MediaController mediaControls, mediaControls2;
    private Uri uri;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private String videoURL = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";

    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.iv_play_cover)
    ImageView ivPlayCover;
    @BindView(R.id.iv_play_cover2)
    ImageView ivPlayCover2;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_play2)
    ImageView ivPlay2;
    @BindView(R.id.video_view)
    VideoView myVideoView;
    @BindView(R.id.video_view2)
    VideoView myVideoView2;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_bar2)
    ProgressBar progressBar2;
    @BindView(R.id.video_view_container)
    FrameLayout videoContainer;
    @BindView(R.id.video_view_container2)
    FrameLayout videoContainer2;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        Glide.with(this)
                .load(R.drawable.ramzan)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivProfile);


        sharedpreferences = getSharedPreferences("VideoVIew", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        updatePlaybackTime();

        mediaControls = new MediaController(this);
        mediaControls2 = new MediaController(this);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                mediaControls.hide();
                mediaControls2.hide();
            }
        });



        ivPlay.setOnClickListener(this);
        ivPlay2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play:
                ivPlay.setVisibility(View.GONE);
                ivPlayCover.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                initVideo();

                break;
            case R.id.iv_play2:
                ivPlay2.setVisibility(View.GONE);
                ivPlayCover2.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);
                initVideo2();

                break;
        }
    }

    private void initVideo() {

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


                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        progressBar.setVisibility(View.GONE);

                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        progressBar.setVisibility(View.VISIBLE);
                        mediaControls.hide();
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        progressBar.setVisibility(View.GONE);
                    }


                    return false;
                }
            });

        } else {
            Toast.makeText(this, "Video Player is not compitable", Toast.LENGTH_SHORT).show();
        }

        uri = Uri.parse(videoURL);
        myVideoView.setVideoURI(uri);

    }

    private void initVideo2() {
        myVideoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaControls2.setAnchorView(videoContainer2);
                myVideoView2.setMediaController(mediaControls2);
                myVideoView2.seekTo(playbackPosition2);
                myVideoView2.start();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            myVideoView2.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        progressBar2.setVisibility(View.GONE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        progressBar2.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        progressBar2.setVisibility(View.GONE);

                    }
                    return false;
                }
            });

        }else {
            Toast.makeText(this, "Video Player is not compitable", Toast.LENGTH_SHORT).show();
        }

        uri = Uri.parse(videoURL);
        myVideoView2.setVideoURI(uri);
    }

    public void updatePlaybackTime(){

        playbackPosition = sharedpreferences.getInt(Play_Back_Position, 0);
        playbackPosition2 = sharedpreferences.getInt(Play_Back_Position2, 0);

        Log.d("test","getting "+playbackPosition);
        Log.d("test","getting "+playbackPosition2);

    }

    public void savePlaybackTime(){

        playbackPosition = myVideoView.getCurrentPosition();
        playbackPosition2 = myVideoView2.getCurrentPosition();
        editor.putInt(Play_Back_Position, playbackPosition);
        editor.putInt(Play_Back_Position2, playbackPosition2);
        editor.apply();

        Log.d("test","saving "+playbackPosition);
        Log.d("test","saving "+playbackPosition2);
    }



    @Override
    protected void onStart() {
        super.onStart();


        ivPlay.setVisibility(View.VISIBLE);
        ivPlayCover.setVisibility(View.VISIBLE);

        ivPlay2.setVisibility(View.VISIBLE);
        ivPlayCover2.setVisibility(View.VISIBLE);

        updatePlaybackTime();

    }

    @Override
    protected void onPause() {
        super.onPause();

        myVideoView.pause();
        myVideoView2.pause();
        savePlaybackTime();

    }

    @Override
    protected void onStop() {
        super.onStop();

        myVideoView.stopPlayback();
        myVideoView2.stopPlayback();

    }


}
