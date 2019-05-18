package com.katiforis.top10.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LocalCache {
    public static LocalCache INSTANCE;

    private static final String filename = "top10";

    public static LocalCache getInstance() {
        if (INSTANCE == null) {
            synchronized(LocalCache.class) {
                INSTANCE = new LocalCache();
            }
        }
        return INSTANCE;
    }

    public <T> T save(T objectToCache, CachedObjectProperties properties, Context context) {
        Map<String, CachedObject> map = load(context);
        CachedObject cached = new CachedObject();
        cached.setObject(objectToCache);
        cached.setSavedTime(new Date());
        map.put(properties.getKey(), cached);
        save(map, context);
        return objectToCache;
    }

    public <T> T get(CachedObjectProperties properties, Context context) {
        Map<String, CachedObject> map = load(context);
        CachedObject cached = map.get(properties.getKey());
        if(cached == null){
           return null;
        }else{
            if(cached.hasExpire(properties.getExpireLimitInSeconds())){
                //remove cached object
                map.put(properties.getKey(), null);
                save(map, context);
                return null;
            }else{
                return (T)cached.getObject();
            }
        }
    }

    private Map<String, CachedObject> save(Map<String, CachedObject> map, Context context) {
        try (FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream s = new ObjectOutputStream(outputStream)){
            s.writeObject(map);
            return map;
        } catch (FileNotFoundException e) {
            //ignore
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, CachedObject> load(Context context) {
        try (FileInputStream fileInputStream = context.openFileInput(filename);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
            Map<String, CachedObject> map = (Map) objectInputStream.readObject();
            if(map == null){
                map = new HashMap<>();
            }
            return map;
        } catch (FileNotFoundException e) {
            //ignore
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
