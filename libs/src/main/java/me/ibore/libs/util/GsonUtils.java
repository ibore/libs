package me.ibore.libs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-23 23:24
 * website: ibore.me
 */

public class GsonUtils {

    private static Gson gson = null;

    private GsonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static String object2Json(Object obj) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(obj);
        }
        return jsonStr;
    }


    public static String object2Json(Object obj, Type type) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(obj, type);
        }
        return jsonStr;
    }


    public static String objectToJsonDateSerializer(Object ts, final String dateformat) {
        String jsonStr = null;
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class,
                        new JsonSerializer<Date>() {
                            public JsonElement serialize(Date src,
                                                         Type typeOfSrc,
                                                         JsonSerializationContext context) {
                                SimpleDateFormat format = new SimpleDateFormat(dateformat);
                                return new JsonPrimitive(format.format(src));
                            }
                        }).setDateFormat(dateformat).create();
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }


    public static List<?> json2List(String jsonStr) {
        List<?> objList = null;
        if (gson != null) {
            Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }


    public static List<?> json2List(String jsonStr, Type type) {
        List<?> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }


    public static Map<?, ?> json2Map(String jsonStr) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
            }.getType();
            objMap = gson.fromJson(jsonStr, type);
        }
        return objMap;
    }

    public static Object json2Bean(String jsonStr, Class<?> cl) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return obj;
    }

    public static Object json2Bean(String jsonStr, Type type) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, type);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl,
                                                 final String pattern) {
        Object obj = null;
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                            throws JsonParseException {
                        SimpleDateFormat format = new SimpleDateFormat(pattern);
                        String dateStr = json.getAsString();
                        try {
                            return format.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).setDateFormat(pattern).create();
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return (T) obj;
    }


    public static Object getJsonValue(String jsonStr, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = json2Map(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }
}
