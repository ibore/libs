package me.ibore.libs.audio;
/**
 * Created by Administrator on 2018/1/19.
 */

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import me.ibore.libs.util.LogUtils;
import me.ibore.libs.util.ToastUtils;

import static android.os.Build.VERSION_CODES.M;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/19 15:32
 * website: ibore.me
 * </pre>
 */
public final class AudioPlayer {


    private static MediaPlayer mMediaPlayer;
    private static final Handler mHander = new Handler();

    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private CheckBox mPlayPause;
    private OnPlayerListenter mListenter;

    private boolean canPlay;
    private String mUrl = "";

    private void initPlayer(final boolean needPlay, int progress) {
        if (null != mSeekBar)  {
            mSeekBar.setMax(10000);
            mSeekBar.setProgress(progress);
        }
        if (progress == 0) {
            if (null != mCurrentTime) mCurrentTime.setText(sec2Time(0));
            if (null != mTotalTime) mTotalTime.setText(sec2Time(0));
            if (null != mPlayPause) mPlayPause.setChecked(true);
        }
        release();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mp -> {
            canPlay = true;
            if (needPlay) start();
            if (null != mTotalTime)mTotalTime.setText(sec2Time(getDuration() / 1000));
        });
        mMediaPlayer.setOnCompletionListener(mp -> {
            upDateProgress(10000, getDuration(), getDuration());
            if (null != mListenter) mListenter.onComplete();
            if (null != mPlayPause) mPlayPause.setChecked(true);
        });
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            if (null != mListenter && what != -38) {
                mListenter.onError();
            }
            release();
            canPlay = false;
            return false;
        });
    }

    public void initPlayer(SeekBar seekBar, TextView currentTime, TextView totalTime, CheckBox playPause) {
        mSeekBar = seekBar;
        mCurrentTime = currentTime;
        mTotalTime = totalTime;
        mPlayPause = playPause;
    }

    public void play(String url) {
        play(url, false);
    }

    public void play(String url, boolean needPlay) {
        play(url, null, needPlay);
    }

    public void play(String url, OnPlayerListenter onPlayerListenter) {
        play(url, onPlayerListenter, false);
    }

    public void play(String url, OnPlayerListenter onPlayerListenter, boolean needPlay) {
        play(url, onPlayerListenter, needPlay, 0);
    }

    public void play(String url, OnPlayerListenter onPlayerListenter, boolean needPlay, int progress) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        this.mUrl = url;
        this.mListenter = onPlayerListenter;
        if (null != mListenter) mListenter.onProgress(0, 0, 0);
        try {
            initPlayer(needPlay, progress);
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mListenter) {
                mListenter.onError();
                mListenter.onComplete();
            }
        }
        if (null != mPlayPause) {
            mPlayPause.setChecked(true);
            mPlayPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        pause();
                    } else {
                        start();
                    }
                }
            });
        }
        if (null != mSeekBar) mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo((int) ((double) seekBar.getProgress() / (double) seekBar.getMax() * getDuration()));
            }
        });
    }

    public void start() {
        mHander.removeCallbacks(mProgressRun);
        if (mMediaPlayer != null && !isPlaying() && canPlay) {
            mHander.post(mProgressRun);
            if (null != mPlayPause) mPlayPause.setChecked(false);
            if (null != mSeekBar) {
                if (mSeekBar.getProgress() < mSeekBar.getMax()) {
                    seekTo((int) ((double) mSeekBar.getProgress() / (double) mSeekBar.getMax() * getDuration()));
                }
            }
            mMediaPlayer.start();
            if (null != mListenter) mListenter.onStart();
        } else {
            int progress = null == mSeekBar ? 0 : mSeekBar.getProgress();
            play(mUrl, mListenter, true, progress);
        }
    }

    public void pause() {
        mHander.removeCallbacks(mProgressRun);
        if (null != mPlayPause) mPlayPause.setChecked(true);
        if (mMediaPlayer != null && isPlaying()) {
            mMediaPlayer.pause();
            if (null != mListenter) mListenter.onPause();
        }
    }

    public void release() {
        mHander.removeCallbacks(mProgressRun);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void seekTo (int msec) {
        if (null != mMediaPlayer) mMediaPlayer.seekTo(msec);
    }

    public int getCurrent() {
        if (null != mMediaPlayer) return mMediaPlayer.getCurrentPosition();
        else return 0;
    }

    public int getDuration() {
        if (null != mMediaPlayer) return mMediaPlayer.getDuration();
        return 0;
    }

    public boolean isPlaying() {
        if (null != mMediaPlayer) return mMediaPlayer.isPlaying();
        else return false;
    }


    public boolean setPlaybackSpeed(float speed) {
        if (Build.VERSION.SDK_INT > M) {
            PlaybackParams playbackParams = new PlaybackParams();
            playbackParams.setSpeed(speed);
            if (null != mMediaPlayer) mMediaPlayer.setPlaybackParams(playbackParams);
            return true;
        } else {
            LogUtils.d("暂时不支持该手机快进");
            return false;
        }
    }

    private Runnable mProgressRun = new Runnable() {
        @Override
        public void run() {
            int percent = 0;
            int current = 0;
            int total = 0;
            if (isPlaying()) {
                percent = (int) (100 * ((double) getCurrent() / getDuration()));
                current = getCurrent();
                total = getDuration();
                mHander.postDelayed(this, 300);
            }
            if (null != mListenter) mListenter.onProgress(percent, current, total);
            upDateProgress((int) (10000 * ((double) getCurrent() / getDuration())), current, total);
        }
    };

    private void upDateProgress(int percent, int current, int total) {
        if (null != mSeekBar) mSeekBar.setProgress(percent);
        if (null != mCurrentTime) mCurrentTime.setText(sec2Time(current / 1000));
        if (null != mTotalTime) mTotalTime.setText(sec2Time(total / 1000));
    }


    public abstract static class OnPlayerListenter {

        protected void onStart() {

        }

        protected void onPause() {

        }

        protected void onProgress(int percent, int current, int total) {

        }

        protected void onError() {

        }

        protected abstract void onComplete();
    }

    public synchronized static int getDuration(String url) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            return mediaPlayer.getDuration() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String sec2Time(int sec) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (sec <= 0) return "00:00";
        else {
            minute = sec / 60;
            if (minute < 60) {
                second = sec % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = sec - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
