package me.ibore.libs.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import me.ibore.libs.util.DeviceUtils
import me.ibore.libs.util.StringUtils


class MainActivity : AppCompatActivity() {

    var mDownloadUrl: String? = "https://raw.githubusercontent.com/JessYanCoding/MVPArmsTemplate/master/art/MVPArms.gif"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        xImageView.setImageResource(R.mipmap.sunset)
//        ViewShadow.setElevation(xImageView, 20F, resources.getColor(R.color.shadow))

//        val httpInterceptor = HttpInterceptor("HTTP")
//        httpInterceptor.setPrintLevel(HttpInterceptor.Level.BODY)
//        val mOkHttpClient : OkHttpClient = OkHttpClient.Builder().addInterceptor(httpInterceptor).build()
//        XHttp.init(applicationContext, mOkHttpClient, 3 , 300)
//        XHttp.download("https://www.so.com/?src=so.com", object : StringObserver(){
//            override fun onSuccess(t: StringInfo?) {
////                xImageView.setImageBitmap(BitmapFactory.decodeStream(ByteArrayInputStream(t!!.data.toByteArray())))
//            }
////
////            override fun onSuccess(t: DownloadInfo?) {
////                xImageView.setImageBitmap(BitmapFactory.decodeFile(t!!.file.absolutePath))
////            }
//
//            override fun onError(e: HttpException?) {
//                e!!.printStackTrace()
//                ToastUtils.showShort(e.message)
//            }
//
//            override fun onProgress(progressInfo: ProgressInfo?) {
//                Log.d("----", progressInfo.toString())
//            }
//        })
        Log.d("----", DeviceUtils.getDeviceUuid().toString())
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
//        val mHttpInterceptor = HttpInterceptor("DEMO")
//        mHttpInterceptor.setPrintLevel(HttpInterceptor.Level.BODY)

//        val mOkHttpClient = ProgressManager.getInstance().with(OkHttpClient.Builder().addInterceptor(mHttpInterceptor)).build()
//
//        val file = File(cacheDir, "download")
//        val fileCallback = object : FileCallback(file) {
//            override fun onSuccess(t: File?) {
//
//            }
//
//            override fun onProgress(progressInfo: ProgressInfo?) {
//                Log.d("----", progressInfo.toString())
//
//            }
//            override fun onError(e: HttpException?) {
//
//            }
//        }
//        ProgressManager.getInstance().addResponseListener(mDownloadUrl, fileCallback)
//        val request = Request.Builder()
//                .url(mDownloadUrl)
//                .build()
//        mOkHttpClient.newCall(request).enqueue(fileCallback)

    }
}