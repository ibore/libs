//package me.ibore.http;
//
//import android.os.Handler;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//
//import java.io.IOException;
//import java.util.List;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import okio.Buffer;
//import okio.BufferedSink;
//import okio.ForwardingSink;
//import okio.Okio;
//import okio.Sink;
//
///**
// * Created by Administrator on 2018/2/7.
// */
//
//public class ProgressRequestBody extends RequestBody {
//
//    protected int mRefreshTime;
//    protected final RequestBody mDelegate;
//    protected final HttpListener mListener;
//    protected final ProgressInfo mProgressInfo;
//    private BufferedSink mBufferedSink;
//
//
//    public ProgressRequestBody(RequestBody delegate, HttpListener listeners) {
//        this.mDelegate = delegate;
//        this.mListener = listeners;
//        this.mProgressInfo = new ProgressInfo();
//        mProgressInfo.setMode(ProgressInfo.UPLOAD);
//    }
//
//    @Override
//    public MediaType contentType() {
//        return mDelegate.contentType();
//    }
//
//    @Override
//    public long contentLength() {
//        try {
//            return mDelegate.contentLength();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    @Override
//    public void writeTo(BufferedSink sink) throws IOException {
//        if (mBufferedSink == null) {
//            mBufferedSink = Okio.buffer(new CountingSink(sink));
//        }
//        mDelegate.writeTo(mBufferedSink);
//        mBufferedSink.flush();
//    }
//
//    protected final class CountingSink extends ForwardingSink {
//        private long bytesWritten = 0L;
//        private long lastRefreshUiTime = 0L;  //最后一次刷新的时间
//        private long lastWriteBytes = 0L;
//
//        public CountingSink(Sink delegate) {
//            super(delegate);
//        }
//
//        @Override
//        public void write(Buffer source, long byteCount) throws IOException {
//            super.write(source, byteCount);
//            if (mProgressInfo.getTotal() == 0) { //避免重复调用 contentLength()
//                mProgressInfo.setTotal(contentLength());
//            }
//            bytesWritten += byteCount;
//            if (mListener != null) {
//                long curTime = System.currentTimeMillis();
//                long lastWriteBytes = 0;
//                if (curTime - lastRefreshUiTime >= XHttp.REFRESH_TIME || bytesWritten == mProgressInfo.getTotal()) {
//                    long diffTime = (curTime - lastRefreshUiTime) / 1000;
//                    if (diffTime == 0) diffTime += 1;
//                    long diffBytes = bytesWritten - lastWriteBytes;
//                    final long networkSpeed = diffBytes / diffTime;
//                    lastRefreshUiTime = curTime;
//                    lastWriteBytes = bytesWritten;
//                    mProgressInfo.setSpeed(networkSpeed);
//                    mProgressInfo.setCurrent(bytesWritten);
//                    mProgressInfo.setProgress((int) (bytesWritten * 10000 / mProgressInfo.getTotal()));
//                    XHttp.Handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mListener.onProgress(mProgressInfo);
//                        }
//                    });
//                }
//            }
//        }
//    }
//}
