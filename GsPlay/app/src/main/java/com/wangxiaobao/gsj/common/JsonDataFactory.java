package com.wangxiaobao.gsj.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.wangxiaobao.gsj.http.Response;

public class JsonDataFactory {
    public static <T> T getInstanceByJson(Class<T> clazz, String json) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, clazz);
        return t;
    }

    public static <T> T getInstanceByJson(Class<T> clazz, JsonObject json) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, clazz);
        return t;
    }

    /**
     * @param json
     * @param clazz
     * @return
     * @author I321533
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    public static <T> Response<T> fromJson(String json, Class<T> cla) {
        Type type = $Gson$Types.newParameterizedTypeWithOwner(null, Response.class, cla);
        return new Gson().fromJson(json, type);
    }


    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}