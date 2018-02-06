package me.ibore.http.subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.ibore.http.ProgressInfo;
import me.ibore.libs.util.CloseUtils;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/6.
 */

public class StringSubscribe implements ObservableOnSubscribe<ProgressInfo> {


    @Override
    public void subscribe(ObservableEmitter<ProgressInfo> e) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();
    }

}
