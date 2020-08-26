package com.rlf.module.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author RU
 * @date 2020/8/24
 * @Desc
 */

/**
 * LRU的实现比较简单，因为java中的LinkedHashMap有很多特点正好适合LRU的思想。LRU（least recently used）最近最少使用，
 * 首先淘汰最长时间未被使用的页面。使用链表实现，当一个元素被访问了，把元素移动到链表的顶端，插入新元素也放到顶端，
 * 当缓存数量到达限制，直接从链表底端移除元素。
 * @param <K>
 * @param <V>
 */
public class LRUCache_2<K,V> extends LinkedHashMap<K,V> {

    private final int CACHE_SIZE;

    /**
     * 传递进来最多能缓存多少数据
     *
     * @param cacheSize 缓存大小
     */
    public LRUCache_2(int cacheSize) {
        // true 表示让 linkedHashMap 按照访问顺序来进行排序，最近访问的放在头部，最老访问的放在尾部。
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
        CACHE_SIZE = cacheSize;
    }

    /**
     * 钩子方法，通过put新增键值对的时候，若该方法返回true
     * 便移除该map中最老的键和值
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if(size() > CACHE_SIZE){
            System.out.println("移除的元素为：" + eldest.getValue());
        }
        // 当 map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据。
        return size() > CACHE_SIZE;
    }

    public static void main(String[] args) {
        Map<Integer, Integer> map = new LRUCache_2<>(5);
        for (int i = 1; i <= 11; i++) {
            map.put(i, i);
            System.out.println("cache的容量为：" + map.size());
            if (i == 6) {
                System.out.println("map.get(1):"+map.get(1));
            }
        }

        System.out.println("=-=-=-=-=-=-=-map元素:");
       // map.entrySet().forEach(integerIntegerEntry -> System.out.println(integerIntegerEntry.getValue()));
        map.forEach((k,v)-> System.out.println(v));
    }

}
