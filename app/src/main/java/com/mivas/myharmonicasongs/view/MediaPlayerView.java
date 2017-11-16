package com.mivas.myharmonicasongs.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.handler.ScrollTimerDbHandler;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.exception.MediaPlayerException;
import com.mivas.myharmonicasongs.listener.MediaPlayerListener;
import com.mivas.myharmonicasongs.util.ExportHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MediaPlayerView extends RelativeLayout {

    private Context context;
    private MediaPlayer mediaPlayer;
    private ImageView playButton;
    private SeekBar progressBar;
    private Handler mediaHandler;
    private TextView currentTimeText;
    private TextView totalTimeText;
    private DbSong dbSong;
    private Runnable updateSongViewsRunnable;
    private boolean mediaViewDisplayed = false;
    private List<DbScrollTimer> dbScrollTimers;
    private DbScrollTimer currentScrollTimer;
    private MediaPlayerListener listener;

    public MediaPlayerView(Context context) {
        super(context);
        this.context = context;
        initViews();
        initListeners();
    }

    public MediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
        initListeners();
    }

    public MediaPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
        initListeners();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_media_player, this, true);
        playButton = findViewById(R.id.button_play);
        progressBar = findViewById(R.id.seek_bar_progress);
        currentTimeText = findViewById(R.id.text_current_time);
        totalTimeText = findViewById(R.id.text_total_time);
        setVisibility(GONE);
    }

    private void initListeners() {
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.selector_play_button);
                } else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.selector_pause_button);
                }
            }
        });
    }

    public void initialize() throws MediaPlayerException {
        if (dbSong.getAudioFile() != null) {

            // prepare song
            Uri songUri = ExportHelper.getInstance().getAudioFileUri(context, dbSong);
            mediaPlayer = MediaPlayer.create(context, songUri);
            if (mediaPlayer == null) {
                throw new MediaPlayerException();
            }

            // init progress bar
            final int finalTime = mediaPlayer.getDuration();
            progressBar.setMax(finalTime);
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                        updateTimeText(currentTimeText, progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    refreshScrollTimers();
                }
            });

            // init time texts and play button
            updateTimeText(currentTimeText, mediaPlayer.getCurrentPosition());
            updateTimeText(totalTimeText, finalTime);
            playButton.setImageResource(R.drawable.selector_play_button);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setImageResource(R.drawable.selector_play_button);
                }
            });

            // init scroll timers
            dbScrollTimers = ScrollTimerDbHandler.getScrollTimersBySongId(dbSong.getId());
            if (!dbScrollTimers.isEmpty()) {
                currentScrollTimer = dbScrollTimers.get(0);
            }

            // init update handler
            mediaHandler = new Handler();
            updateSongViewsRunnable = new Runnable() {
                public void run() {
                    int currentTime = mediaPlayer.getCurrentPosition();
                    progressBar.setProgress(currentTime);
                    updateTimeText(currentTimeText, currentTime > finalTime ? finalTime : currentTime);

                    if (currentScrollTimer != null && currentTime > currentScrollTimer.getTime()) {
                        listener.onScrollToSection(currentScrollTimer.getSectionId());
                        dbScrollTimers.remove(0);
                        currentScrollTimer = dbScrollTimers.isEmpty() ? null : dbScrollTimers.get(0);
                    }
                    mediaHandler.postDelayed(this, 100);
                }
            };
            mediaHandler.postDelayed(updateSongViewsRunnable, 100);

        }
    }

    public void refreshScrollTimers() {
        int mediaPlayerTime = mediaPlayer.getCurrentPosition();
        dbScrollTimers = ScrollTimerDbHandler.getScrollTimersBySongId(dbSong.getId());
        Iterator<DbScrollTimer> iterator = dbScrollTimers.iterator();
        while (iterator.hasNext()) {
            DbScrollTimer dbScrollTimer = iterator.next();
            if (dbScrollTimer.getTime() < mediaPlayerTime) {
                iterator.remove();
            }
        }
        currentScrollTimer = dbScrollTimers.isEmpty() ? null : dbScrollTimers.get(0);
    }


    public void animate(final boolean expand) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), expand ? R.anim.anim_bottom_up : R.anim.anim_bottom_down);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!expand) {
                    setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setVisibility(VISIBLE);
        startAnimation(animation);
        mediaViewDisplayed = !mediaViewDisplayed;
    }

    private void updateTimeText(TextView textView, int time) {
        textView.setText(String.format(Locale.US, "%d:%02d", TimeUnit.MILLISECONDS.toMinutes(time), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))));
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaHandler.removeCallbacks(updateSongViewsRunnable);
            mediaPlayer.release();
        }
    }

    public boolean isDisplayed() {
        return mediaViewDisplayed;
    }

    public void setDbSong(DbSong dbSong) {
        this.dbSong = dbSong;
    }

    public void show(boolean show) {
        mediaViewDisplayed = show;
        setVisibility(show ? VISIBLE : GONE);
    }

    public void setListener(MediaPlayerListener listener) {
        this.listener = listener;
    }
}
