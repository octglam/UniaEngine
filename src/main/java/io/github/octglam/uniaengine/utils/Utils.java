package io.github.octglam.uniaengine.utils;

import java.util.*;

public class Utils {
    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String capitalizeNextSpace(String str) {
        String[] words = str.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    sb.append(word.substring(1));
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    public static <K, V> HashMap<K, V> getHashMapInReverseOrder(HashMap<K, V> map) {
        List<K> keys = new ArrayList<>(map.keySet());
        Collections.reverse(keys);
        HashMap<K, V> newmap = new HashMap<>();
        for (K key : keys) {
            newmap.put(key, map.get(key));
        }
        return newmap;
    }
}
