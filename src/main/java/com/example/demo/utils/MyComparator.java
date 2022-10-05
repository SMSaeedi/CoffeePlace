package com.example.demo.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MyComparator implements Comparator {
    private Map map;

    public MyComparator(Map map) {
        this.map = map;
    }

    public int compare(Object o1, Object o2) {
        return ((Integer) map.get(o2)).compareTo((Integer) map.get(o1));
    }

    public static Map<String, Integer> sortMapDesc(Map<String, Integer> map) {
        MyComparator comp = new MyComparator(map);

        Map<String, Integer> newMap = new TreeMap(comp);
        newMap.putAll(map);

        return newMap;
    }
}