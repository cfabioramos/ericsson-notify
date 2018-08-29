package br.com.ericsson.smartnotification.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public final class JsonUtil {
    
    private static final GsonBuilder builder;

    private static final Gson gson;

    static{
        builder = new GsonBuilder().setDateFormat(DateUtil.PATTERN_DATE.concat(" ").concat(DateUtil.PATTERN_TIME));
        gson = builder.create();
    }
    
    private JsonUtil() {
    }
    
    
    public static String parseToJsonString(Object object) {
        return gson.toJson(object);
    }
    
    
    public static Gson getGson() {
        return gson;
    }
    
    public static Object parse(String json, Class<?> classe, InstanceCreator<?>... instanceCreators) {
        GsonBuilder customBuilder = null;
        for(InstanceCreator<?> instanceCreator : instanceCreators) {
            if(customBuilder != null) {
                customBuilder = customBuilder.registerTypeAdapter(classe, instanceCreator);
            }else {
                customBuilder = builder.registerTypeAdapter(classe, instanceCreator);
            }
        }
        return customBuilder != null ? customBuilder.create().fromJson(json, classe) : null;
    }


    
}
