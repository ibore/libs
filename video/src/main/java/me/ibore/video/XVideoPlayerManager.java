package me.ibore.video;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/22 13:31
 * website: ibore.me
 * </pre>
 */
public class XVideoPlayerManager {

    private XVideoPlayer mVideoPlayer;

    private XVideoPlayerManager() {
    }

    private static XVideoPlayerManager sInstance;

    public static synchronized XVideoPlayerManager instance() {
        if (sInstance == null) {
            sInstance = new XVideoPlayerManager();
        }
        return sInstance;
    }

    public XVideoPlayer getCurrentXVideoPlayer() {
        return mVideoPlayer;
    }

    public void setCurrentXVideoPlayer(XVideoPlayer videoPlayer) {
        if (mVideoPlayer != videoPlayer) {
            releaseXVideoPlayer();
            mVideoPlayer = videoPlayer;
        }
    }

    public void suspendXVideoPlayer() {
        if (mVideoPlayer != null && (mVideoPlayer.isPlaying() || mVideoPlayer.isBufferingPlaying())) {
            mVideoPlayer.pause();
        }
    }

    public void resumeXVideoPlayer() {
        if (mVideoPlayer != null && (mVideoPlayer.isPaused() || mVideoPlayer.isBufferingPaused())) {
            mVideoPlayer.restart();
        }
    }

    public void releaseXVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    public boolean onBackPressd() {
        if (mVideoPlayer != null) {
            if (mVideoPlayer.isFullScreen()) {
                return mVideoPlayer.exitFullScreen();
            } else if (mVideoPlayer.isTinyWindow()) {
                return mVideoPlayer.exitTinyWindow();
            }
        }
        return false;
    }
}
