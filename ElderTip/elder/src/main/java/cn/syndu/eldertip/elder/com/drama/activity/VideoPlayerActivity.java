package cn.syndu.eldertip.elder.com.drama.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;

import cn.syndu.eldertip.elder.R;
import cn.syndu.eldertip.elder.com.drama.untils.FileHelper;
import cn.syndu.eldertip.elder.com.drama.untils.Player;

/**
 * Created by Boria on 2015/12/14.
 */
public class VideoPlayerActivity extends Activity {
    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnStop, btnMoreStart;
    private SeekBar skbProgress;
    private Player player;
    private int state= 1;
    private FileHelper helper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        helper = new FileHelper(getApplicationContext());
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
        surfaceView.setOnClickListener(new ClickEvent());

        btnPlayUrl = (Button) this.findViewById(R.id.btnPlayUrl);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new ClickEvent());

        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());

        btnMoreStart = (Button) findViewById(R.id.moreStart);
        btnMoreStart.setOnClickListener(new ClickEvent());

        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new Player(surfaceView, skbProgress);



    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
//				player = new Player(surfaceView, skbProgress);
                String SDPATH = Environment.getExternalStorageDirectory().getPath();
                String local = SDPATH + "//" + "123123.3gp";
                String VideoPlace = null;
                File file = new File(local);
                if (file.exists()) {
                    System.out.println("播放本地视频");
                    VideoPlace = local;
                }else {
                    System.out.println("播放网络视频");
                    VideoPlace = "http://192.168.1.104:8080/NoteDemo/video/123123.3gp";
                    helper = new FileHelper(getApplicationContext());
                    try {
                        helper.createSDFile("123123.3gp").getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }	new Thread(
                            new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    helper.writeSDFile("123123.3gp", "http://192.168.1.104:8080/NoteDemo/video/123123.3gp");
                                }
                            }
                    ).start();
                }
                player.playUrl(VideoPlace);


            } else if (arg0 == btnStop) {
                player.stop();
            } else if (arg0 == btnMoreStart) {
                player.play();
            } else if (arg0 == surfaceView) {
                System.out.println("heheh");
                if (state == 1) {
                    try {
                        Thread.sleep(150);
                        btnPlayUrl.setVisibility(View.GONE);
                        btnPause.setVisibility(View.GONE);
                        btnStop.setVisibility(View.GONE);
                        skbProgress.setVisibility(View.GONE);
                        state = 2;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else if (state == 2) {
                    try {
                        Thread.sleep(150);
                        btnPlayUrl.setVisibility(View.VISIBLE);
                        btnPause.setVisibility(View.VISIBLE);
                        btnStop.setVisibility(View.VISIBLE);
                        skbProgress.setVisibility(View.VISIBLE);
                        state = 1;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }

}
