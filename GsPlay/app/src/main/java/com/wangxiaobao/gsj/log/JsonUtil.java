package com.wangxiaobao.gsj.log;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

import org.json.JSONArray;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Json和对象相互转换
 * Created by wenchaoy on 2017/3/21.
 */
public class JsonUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static ExclusionStrategy strategy = new ExclusionStrategy() {

        @Override
        public boolean shouldSkipField(FieldAttributes attr) {
            if (attr != null && attr.getAnnotations() != null) {
                Collection<Annotation> annotations = attr.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Expose) {
                        Expose temp = (Expose) annotation;
                        return !temp.serialize();
                    }
                }
            }

            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> className) {
            return false;
        }
    };

    public static final Gson NAMING_GSON = new GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .addSerializationExclusionStrategy(strategy)
            .create();

    public static String objectToJson(Object object) {
        try {
            return GsonInstance.getInstance().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static Object jsonToObject(String json, Class<?> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if ("[]".equals(json)) {
            return null;
        }
        try {
            return GsonInstance.getInstance().fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * list转jsonArray
     *
     * @param list
     * @return
     */
    public static JSONArray listToJsonArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                jsonArray.put(str);
            }
        }
        return jsonArray;
    }

    public static <T> T getObjectFromJson(String json, Type classOfT) {
        return NAMING_GSON.fromJson(json, classOfT);
    }
    /**
     * @param jsonString
     * @param cls
     * @param <T>
     * @return 解析[]格式的json
     */
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls){
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                T obj = (T) new JsonParser().parse(jsonString).getAsJsonObject();
                list.add(obj);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
        return list;
    }
}
