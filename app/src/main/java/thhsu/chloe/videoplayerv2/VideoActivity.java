package thhsu.chloe.videoplayerv2;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

public class VideoActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl, VideoControllerView.MediaPlayerControl, MediaPlayer.OnVideoSizeChangedListener {
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private static final String VIDEO_PATH="https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/taeyeon.mp4";
    private MediaController mediaController;
    private Handler handler = new Handler();
    private VideoControllerView videoController;
    private int mVideoHeight;
    private int mVideoWidth;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);

        surfaceView = (SurfaceView) findViewById(R.id.videoSurface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(VideoActivity.this);


//        try{
//            retriever.setDataSource(VIDEO_PATH);
//
//            mVideoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//            mVideoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//            retriever.release();
//        }catch (IllegalStateException e){
//            e.printStackTrace();
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }

//        surfaceView.getHolder().setFixedSize(800,700);

        surfaceView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if(mediaController != null){
//                    mediaController.show();
////                }
                if(videoController != null){
                    videoController.show();
                }
                return false;
            }
        });



        mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(VIDEO_PATH);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(VideoActivity.this);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());

//            mediaController = new MediaController(this);
            videoController = new VideoControllerView(this);
        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (SecurityException e){
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mediaPlayer.setDisplay(holder);



//        mediaPlayer.prepareAsync();
//        mediaPlayer.setDisplay(surfaceHolder);

//        mediaPlayer = new MediaPlayer();

//        try{
//            mediaPlayer.setDataSource(VIDEO_PATH);
//            mediaPlayer.prepare();
//            mediaPlayer.setOnPreparedListener(VideoActivity.this);
//            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build());
//
////            mediaController = new MediaController(this);
            videoController = new VideoControllerView(this);
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }catch (SecurityException e){
//            e.printStackTrace();
//        }catch (IllegalStateException e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }



    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

//        mediaController.setMediaPlayer(this);
//        mediaController.setAnchorView(surfaceView);

        videoController.setMediaPlayer(this);
        videoController.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
//        videoController.setAnchorView((RelativeLayout)findViewById(R.id.videoFirstLayer));
//        videoController.setAnchorView((RelativeLayout)findViewById(R.id.mainActivity));

        handler.post(new Runnable() {
            @Override
            public void run() {
//                mediaController.setEnabled(true);
//                mediaController.show();

                videoController.setEnabled(true);
                videoController.show();
            }
        });
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        Toast.makeText(getApplicationContext(),
                String.valueOf(mVideoWidth) + "x" + String.valueOf(mVideoHeight),
                Toast.LENGTH_SHORT).show();
        if(mediaPlayer.isPlaying() && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            surfaceHolder.setFixedSize(mVideoWidth,700);
        }else{
            surfaceHolder.setFixedSize(mVideoWidth,mVideoHeight);
        }
    }
}
