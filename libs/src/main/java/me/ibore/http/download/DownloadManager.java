package me.ibore.http.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.ibore.http.ProgressInfo;
import me.ibore.http.XHttp;
import me.ibore.http.observer.DownloadObserver;
import me.ibore.libs.util.CloseUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/6.
 */

public class DownloadManager {

    private static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>();
    private HashMap<String, Call> downCalls;//用来存放各个下载的请求
    private OkHttpClient mClient;//OKHttpClient;

    //获得一个单例类
    public static DownloadManager getInstance() {
        //死循环  一直执行到 程序退出
        for (; ; ) {
            DownloadManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            //在本类中 new 出自己
            current = new DownloadManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private DownloadManager() {
        downCalls = new HashMap<>();
        // Http应该同意管理
        mClient = XHttp.getOkHttpClient();
    }

    /**
     * 开始下载
     *
     * @param url       下载请求的网址
     * @param downloadObserver 用来回调的接口
     */
    public void download(String url, File fileDirs, DownloadObserver downloadObserver) {
        Observable.just(url)
                .filter(s -> !downCalls.containsKey(s))//call的map已经有了,就证明正在下载,则这次不下载
                .flatMap(s -> Observable.just(createDownInfo(s, fileDirs)))
                .map(info -> getRealFileName(info, fileDirs))//检测本地文件夹,生成新的文件名
                .flatMap(downloadInfo -> Observable.create(new DownloadSubscribe(downloadInfo)))//下载
                .observeOn(AndroidSchedulers.mainThread())//在主线程回调
                .subscribeOn(Schedulers.io())//在子线程执行
                .subscribe(downloadObserver);//添加观察者

    }

    public void cancel(String url) {
        Call call = downCalls.get(url);
        if (call != null) {
            call.cancel();//取消 网络请求
        }
        downCalls.remove(url);
    }

    /**
     * 创建DownInfo
     *
     * @param url 请求网址 给实体赋值
     * @param fileDirs
     * @return DownInfo
     */
    private ProgressInfo createDownInfo(String url, File fileDirs) {
        ProgressInfo progressInfo = new ProgressInfo();
        progressInfo.setUrl(url);
        long contentLength = getContentLength(url);//只是获取下载文件大小长度
        progressInfo.setTotal(contentLength);      //给实体赋值
        String fileName = url.substring(url.lastIndexOf("/"));  //截取 要下载的文件 http://192.168.43.246:8080/lieji/musics/cc.mp3   中的cc.mp3
        progressInfo.setData(new File(fileDirs, fileName));                     //fileName=cc.mp3  给实体赋值
        return progressInfo;
    }

    private ProgressInfo getRealFileName(ProgressInfo progressInfo, File fileDirs) {
        File filePath = (File) progressInfo.getData();  //获得封装后的 文件名
        long downloadLength = 0, contentLength = progressInfo.getTotal();//获得封装后的 文件总长度
        System.out.println("获得下载文件内容长度"+contentLength);

        System.out.println("获得下载文件是否存在"+filePath.exists());
        if (filePath.exists()) {
            downloadLength = filePath.length();
            System.out.println("下载的地址"+filePath.getPath());
        }
        //之前下载过,如果原文件长度  与  新文件长度不一致，需要重新来一个文件
        int i = 1;
        while (downloadLength >= contentLength) {
            int dotIndex = filePath.getName().lastIndexOf(".");       //cc.mp3   搜索最后 . 的位置  从0 开始 int dotIndex=2
            String fileNameOther;
            if (dotIndex == -1) {
                fileNameOther = filePath.getName() + "(" + i + ")";
            } else {
                fileNameOther = filePath.getName().substring(0, dotIndex)     //提取信息  substring(beginindex(从0开始计算), endIndex(从1开始计算)) fileNameOther=cc
                        + "(" + i + ")" + filePath.getName().substring(dotIndex);  // substring(dotIndex) :截取到结尾     cc + .mp3   ==>cc1.mp3
            }
            File newFile = new File(fileDirs, fileNameOther);
            filePath = newFile;
            downloadLength = newFile.length();
            i++;
        }
        //设置改变过的文件名/大小
        progressInfo.setCurrent(downloadLength);
        progressInfo.setData(filePath);
        return progressInfo;
    }

    private class DownloadSubscribe implements ObservableOnSubscribe<ProgressInfo> {

        private ProgressInfo downloadInfo;

        public DownloadSubscribe(ProgressInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void subscribe(ObservableEmitter<ProgressInfo> e) throws Exception {
            String url = downloadInfo.getUrl();
            long downloadLength = downloadInfo.getProgress();  //已经下载好的长度
            long contentLength = downloadInfo.getTotal();      //文件的总长度
            //初始进度信息
            e.onNext(downloadInfo);

            Request request = new Request.Builder()
                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分   断点下载
                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                    .url(url)
                    .build();
            Call call = mClient.newCall(request);
            downCalls.put(url, call);//把这个添加到call里,方便取消
            Response response = call.execute();

            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            try {
                is = response.body().byteStream();
                fileOutputStream = new FileOutputStream((File) downloadInfo.getData(), true);
                byte[] buffer = new byte[2048];//缓冲数组2kB
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    downloadLength += len;
                    downloadInfo.setCurrent(downloadLength);
                    e.onNext(downloadInfo);
                }
                fileOutputStream.flush();
                downCalls.remove(url);
            } finally {
                //关闭IO流
                CloseUtils.closeIO(is, fileOutputStream);
            }
            e.onComplete();//完成
        }
    }

    /**
     * 网络请求 只是获取下载文件长度
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();           //关闭网络请求
                return contentLength == 0 ? -1 : contentLength;
                //解释: 如果contentLength==0 那contentLength=DownloadInfo.TOTAL_ERROR  反contentLength==contentLength
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
