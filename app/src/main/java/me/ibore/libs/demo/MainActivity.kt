package me.ibore.libs.demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import me.ibore.http.HttpException
import me.ibore.http.callback.FileCallback
import me.ibore.http.interceptor.HttpInterceptor
import me.ibore.http.progress.ProgressInfo
import me.ibore.http.progress.ProgressManager
import me.ibore.widget.shadow.ViewShadow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    var mDownloadUrl: String? = "https://raw.githubusercontent.com/JessYanCoding/MVPArmsTemplate/master/art/MVPArms.gif"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewShadow.setElevation(tv_toast3, 20F, resources.getColor(R.color.shadow))
//        roundImageView.setImageResource(R.mipmap.head)
//        var transformation = RoundedCornersTransformation(10, 0)
//        var transformation = RoundedCornersTransformation(10, 0)
//        Glide.with(roundImageView).load(R.mipmap.head).apply(RequestOptions.bitmapTransform(transformation)).into(roundImageView)
//        val imagePicker = ImagePicker.getInstance()
//        imagePicker.imageLoader = ImageLoader()   //设置图片加载器
//        imagePicker.isShowCamera = true                      //显示拍照按钮
//        imagePicker.isCrop = true                           //允许裁剪（单选才有效）
//        imagePicker.isSaveRectangle = true                   //是否按矩形区域保存
//        imagePicker.selectLimit = 1              //选中数量限制
//        imagePicker.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
//        imagePicker.focusWidth = 800                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.focusHeight = 800                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.outPutX = 1000                         //保存文件的宽度。单位像素
//        imagePicker.outPutY = 1000                         //保存文件的高度。单位像素
//
//
//        ImagePicker.getInstance().selectLimit = 1
//        val intent = Intent(this, ImageGridActivity::class.java)
//        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true) // 是否是直接打开相机
//        startActivityForResult(intent, 1)

        //Okhttp/Retofit 下载监听
        val mHttpInterceptor = HttpInterceptor("DEMO")
        mHttpInterceptor.setPrintLevel(HttpInterceptor.Level.BODY)

        val mOkHttpClient = ProgressManager.getInstance().with(OkHttpClient.Builder().addInterceptor(mHttpInterceptor)).build()

        val file = File(cacheDir, "download")
        val fileCallback = object : FileCallback(file) {
            override fun onSuccess(t: File?) {

            }

            override fun onProgress(progressInfo: ProgressInfo?) {
                Log.d("----", progressInfo.toString())

            }
            override fun onError(e: HttpException?) {

            }
        }
        ProgressManager.getInstance().addResponseListener(mDownloadUrl, fileCallback)
        val request = Request.Builder()
                .url(mDownloadUrl)
                .build()
        mOkHttpClient.newCall(request).enqueue(fileCallback)
    }
}