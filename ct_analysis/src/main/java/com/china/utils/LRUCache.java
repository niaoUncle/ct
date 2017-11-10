package com.china.utils;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache <String,Integer> extends LinkedHashMap<String, Integer> {

    private static final long serialVersionUID = -5907797767584803517L;
    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    /*
     * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {

        return (size() > this.maxElements);
    }


}
