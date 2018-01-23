package me.ibore.libs.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.ibore.video.CustomerController
import me.ibore.video.XVideoPlayer.TYPE_IJK


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        roundImageView.setImageResource(R.mipmap.head)
//        var transformation = RoundedCornersTransformation(10, 0)
//        var transformation = RoundedCornersTransformation(10, 0)
//        Glide.with(roundImageView).load(R.mipmap.head).apply(RequestOptions.bitmapTransform(transformation)).into(roundImageView)

        val controller = CustomerController(this)

        xVideoPlayer.setController(controller)
        xVideoPlayer.setPlayerType(TYPE_IJK)
        controller.setTitle("测试标题")
        controller.setLenght(25000)

        xVideoPlayer.setUp("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4", null)

        val okhttp = OkHttpClient()
        okhttp.
    }
}


