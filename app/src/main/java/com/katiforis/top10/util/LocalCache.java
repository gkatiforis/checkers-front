package com.katiforis.top10.util;

import android.content.Context;

import com.katiforis.top10.DTO.response.RankList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LocalCache {
    public static LocalCache INSTANCE;

    private static final String filename = "top10";
    public static final String NOTIFICATIONS = "notifications";
    public static final String RANK = "rank";

    public static LocalCache getInstance() {
        if (INSTANCE == null) {
            synchronized(LocalCache.class) {
                INSTANCE = new LocalCache();
            }
        }
        return INSTANCE;
    }

    public RankList saveRank(RankList rankList, Context context) {
            Map<String, Object> map = load(context);
            RankList cached = (RankList)map.get(RANK);
            if(cached == null){
                map.put(RANK, rankList);
            }else{
                cached.getPlayers().addAll(0, rankList.getPlayers());
                map.put(RANK, cached);
            }
            save(map, context);
        return rankList;
    }

    private Map save(Map map, Context context) {
       try (FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
           ObjectOutputStream s = new ObjectOutputStream(outputStream)){
           s.writeObject(map);
           return map;
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
        return null;
    }

    private Map load(Context context) {
        try ( FileInputStream fileInputStream = context.openFileInput(filename);
              ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
            Map map = (Map) objectInputStream.readObject();
            if(map == null){
                map = new HashMap<>();
            }
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
