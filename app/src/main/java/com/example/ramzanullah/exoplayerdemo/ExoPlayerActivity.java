package com.example.ramzanullah.exoplayerdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoPlayerActivity extends AppCompatActivity implements Player.EventListener{

    private static final String Play_Back_Position = "exo_player_position";

    @BindView(R.id.exoPlayerView)
    PlayerView playerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    SimpleExoPlayer exoPlayer;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private long playbackPosition = 0;
    //private String videoURL = "http://blueappsoftware.in/layout_design_android_blog.mp4";
    private String videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    //private String videoURL = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        ButterKnife.bind(this);
        sharedpreferences = getSharedPreferences("ExoPlayer", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        initializeExoplayer();
        Log.d("RMZ","start");
    }

    @Override
    protected void onPause() {
        releasePlayer();
        super.onPause();
    }

    @Override
    protected void onStop() {
        releasePlayer();
        super.onStop();
    }


    public void  initializeExoplayer() {
        updatePlaybackTime();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this);
        DefaultLoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

        prepareExoplayer();

        playerView.setPlayer(exoPlayer);
        exoPlayer.seekTo(playbackPosition);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);
    }

    public void prepareExoplayer() {

        Uri videoURI = Uri.parse(videoURL);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.prepare(mediaSource);

    }

    public void updatePlaybackTime(){

        playbackPosition = sharedpreferences.getLong(Play_Back_Position, 0);

    }


    public void savePlaybackTime(){

        playbackPosition = exoPlayer.getCurrentPosition();
        editor.putLong(Play_Back_Position, playbackPosition);
        editor.apply();
    }

    private void releasePlayer() {

        savePlaybackTime();
        exoPlayer.stop();
        exoPlayer.release();
    }



    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING)
            progressBar.setVisibility(View.VISIBLE);
        else if (playbackState == Player.STATE_READY)
            progressBar.setVisibility(View.GONE);

    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
