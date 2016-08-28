package com.example.zakiva.santa.Helpers;

import android.support.v4.util.LruCache;

/**
 * Created by Ariel on 8/28/2016.
 */
public class Cache {

    private static Cache instance;
    private LruCache<Object, Object> lru;

    private Cache() {

        lru = new LruCache<Object, Object>(1024);

    }

    public static Cache getInstance() {

        if (instance == null) {

            instance = new Cache();
        }

        return instance;

    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}
