package me.ibore.libs.audio;
/**
 * Created by Administrator on 2018/1/19.
 */


import android.media.MediaRecorder;
import android.os.Handler;
import androidx.annotation.UiThread;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/19 15:52
 * website: ibore.me
 * </pre>
 */
public final class AudioRecord {

    private static final int RECORDING_BITRATE = 32000;
    private static final int RECORDING_SAMPLING = 16000;
    private MediaRecorder mRecorder;
    private Handler mHandler = new Handler();
    private Runnable mHandlerRunnable;
    private File mRecordFile;
    private boolean isRecording = false;
    private OnRecordListener mListener;
    private int mMaxVol = 0x7FFF;
    private int mCount = 0;

    public void startRecord(File recordFile) {
        if (isRecording()) return;
        if (null == recordFile) {
            throw new IllegalStateException("recordFile can't be empty");
        }
        this.mRecordFile = recordFile;
        try {
            if (mRecorder != null) {
                mRecorder.release();
                mRecorder = null;
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(RECORDING_SAMPLING);
            mRecorder.setAudioEncodingBitRate(RECORDING_BITRATE);
            mRecorder.setOutputFile(mRecordFile.getAbsolutePath());
            mRecorder.prepare();
            isRecording = true;
            mRecorder.start();
            initTimer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTimer() {
        mHandlerRunnable = () -> {
            if (mListener != null){
                mListener.onRecord(isRecording()? mRecorder.getMaxAmplitude() * mMaxVol / 0x7FFF : 0, (mCount++) * 100);
                mHandler.postDelayed(mHandlerRunnable, 100);
            }
        };
        mHandler.postDelayed(mHandlerRunnable, 100);
    }

    public File stopRecord() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        isRecording = false;
        mCount = 0;
        mHandler.removeCallbacks(mHandlerRunnable);
        return mRecordFile;
    }

    public void discardRecord() {
        mRecorder.stop();
        mRecorder.release();
        if (mRecordFile != null && mRecordFile.exists() && mRecordFile.delete()) {
            isRecording = false;
        }
        mCount = 0;
        mHandler.removeCallbacks(mHandlerRunnable);
    }

    public File getRecordFile() {
        return mRecordFile;
    }

    public boolean isRecording() {
        return mRecorder != null && isRecording;
    }

    public void setOnRecordListener(OnRecordListener listener) {
        this.mListener = listener;
    }

    public void setMaxVol(int mMaxVol) {
        this.mMaxVol = mMaxVol;
    }

    public interface OnRecordListener {
        @UiThread
        void onRecord(int amplitude, int recordTime);
    }
}
