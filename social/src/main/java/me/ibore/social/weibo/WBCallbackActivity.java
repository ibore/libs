package me.ibore.social.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

import me.ibore.social.PlatformConfig;
import me.ibore.social.PlatformType;
import me.ibore.social.SocialApi;


public class WBCallbackActivity extends Activity implements IWeiboHandler.Response {

    protected WBHandler mWBHandler = null;

    public WBCallbackActivity() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocialApi api = SocialApi.get(this.getApplicationContext());
        this.mWBHandler = (WBHandler) api.getSSOHandler(PlatformType.WEIBO);
        this.mWBHandler.onCreate(this.getApplicationContext(), PlatformConfig.getPlatformConfig(PlatformType.WEIBO));

        if(this.getIntent() != null) {
            this.handleIntent(this.getIntent());
        }
    }

    protected final void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        SocialApi api = SocialApi.get(this.getApplicationContext());
        this.mWBHandler = (WBHandler) api.getSSOHandler(PlatformType.WEIBO);
        this.mWBHandler.onCreate(this.getApplicationContext(), PlatformConfig.getPlatformConfig(PlatformType.WEIBO));

        this.handleIntent(this.getIntent());
    }

    protected void handleIntent(Intent intent) {
        this.mWBHandler.onNewIntent(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        this.mWBHandler.onResponse(baseResponse);
        finish();
    }
}
