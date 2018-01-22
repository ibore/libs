package me.ibore.social.listener;


import java.util.Map;

import me.ibore.social.PlatformType;

public interface AuthListener {
    void onComplete(PlatformType platform_type, Map<String, String> map);

    void onError(PlatformType platform_type, String err_msg);

    void onCancel(PlatformType platform_type);
}
