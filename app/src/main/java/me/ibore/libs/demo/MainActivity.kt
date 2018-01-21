package me.ibore.libs.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import me.ibore.libs.glide.transformation.RoundedCornersTransformation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roundImageView.setImageResource(R.mipmap.head)
//        var transformation = RoundedCornersTransformation(10, 0)
        var transformation = RoundedCornersTransformation(10, 0)
        Glide.with(roundImageView).load(R.mipmap.head).apply(RequestOptions.bitmapTransform(transformation)).into(roundImageView)
    }
}


