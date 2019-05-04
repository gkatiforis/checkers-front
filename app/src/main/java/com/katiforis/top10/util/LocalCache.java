package com.katiforis.top10.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class LocalCache {
    public static LocalCache INSTANCE;

    private static final String filename = "top10";
    public static final String NOTIFICATIONS = "notifications";

    public static LocalCache getInstance() {
        if (INSTANCE == null) {
            synchronized(LocalCache.class) {
                INSTANCE = new LocalCache();
            }
        }
        return INSTANCE;
    }

    public <T> List<T> save(String key, List<T> values, Context context) {
        List<T> objects;
        try {
            Map<String, List<T>> map = load(context);
            objects = map.get(key);
            if(objects == null){
                map.put(key, values);
            }else{
                objects.addAll(0, values);
                map.put(key, objects);
            }

            FileOutputStream outputStream;
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream s = new ObjectOutputStream(outputStream);
            s.writeObject(map);
            s.close();
            outputStream.close();
        } catch (Exception e) {
            return null;
        }
        return values;
    }

    private Map load(Context context) {
        Map map;
        try {
            FileInputStream fileInputStream;
            fileInputStream = context.openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (Map) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
           return null;
        }
        return map;
    }
}
