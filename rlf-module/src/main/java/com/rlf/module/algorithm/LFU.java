package com.rlf.module.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RU
 * @date 2020/8/26
 * @Desc
 */
/**LFU(Least Frequently Used)淘汰一定时期内被访问次数最少的元素。如果元素的一定时间内的访问次数相同时，则比较他们的最新一次的访问时间。*/


/**
 * 这里实现略微复杂，首先要维持一个缓存的map还要维持一个访问次数以及时间的map。
 * 1、首先内部类有3个属性，有缓存的key，缓存的访问次数，最近一次的访问时间。内部实现比较器，按照先访问次数后时间的比较顺序。整个类作为对象放在一个hashmap的value里。
 * 2、put方法：当一个元素要放入缓存的时候，先去缓存检查是否有相同的key，也就是要填加的（key，value）在cache的map里get（key）是否为空。这里不用检查value是否有相同的，标识一个缓存的唯一性是key，key不同就是一个不同的缓存元素。
 * 当get（key）为空时，要放入缓存新元素了，首先检查缓存的容量是否达到限制值，达到了，执行移除一个元素的方法，然后在count的map里加入相应的访问信息，初始值为1。如果没达到限制值，直接在count的map里加入相应的访问信息，初始值为1。
 * 当get（key）不为空直接执行addHitCount方法。将访问次数加1，更新最新访问时间。
 * 最后不管什么情况，执行map.put方法。
 * @param <k>
 * @param <v>
 */
public class LFU<k,v> {
    private final int capcity;

    private Map<k, v> cache = new HashMap<>();

    private Map<k, HitRate> count = new HashMap<>();

    public LFU(int capcity) {
        this.capcity = capcity;
    }

    public void put(k key, v value) {
        v v = cache.get(key);
        //该key没有存储过
        if (v == null) {
            if (cache.size() == capcity) {
                removeElement();
            }
            count.put(key, new HitRate(key, 1, System.nanoTime()));
            //存储过则加访问次数 +1
        } else {
            addHitCount(key);
        }
        cache.put(key, value);
    }

    public v get(k key) {
        v value = cache.get(key);
        if (value != null) {
            addHitCount(key);
            return value;
        }
        return null;
    }

    //移除元素
    private void removeElement() {
        HitRate hr = Collections.min(count.values());
        cache.remove(hr.key);
        count.remove(hr.key);
    }

    //更新访问元素状态
    private void addHitCount(k key) {
        HitRate hitRate = count.get(key);
        hitRate.hitCount = hitRate.hitCount + 1;
        hitRate.lastTime = System.nanoTime();
    }

    //内部类
    class HitRate implements Comparable<HitRate> {
        private k key;
        private int hitCount;
        private long lastTime;

        private HitRate(k key, int hitCount, long lastTime) {
            this.key = key;
            this.hitCount = hitCount;
            this.lastTime = lastTime;
        }

        @Override
        public int compareTo(HitRate o) {
            int compare = Integer.compare(this.hitCount, o.hitCount);
            return compare == 0 ? Long.compare(this.lastTime, o.lastTime) : compare;
        }
    }


    public static void main(String[] args) {
        LFU<Integer, Integer> cache = new LFU<>(3);
        cache.put(2, 2);
        cache.put(1, 1);

        System.out.println(cache.get(2));
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));

        cache.put(3, 3);
        cache.put(4, 4);

        //1、2元素都有访问次数，放入3后缓存满，加入4时淘汰3
        System.out.println(cache.get(3));
        System.out.println(cache.get(2));
        //System.out.println(cache.get(1));
        System.out.println(cache.get(4));

        cache.put(5, 5);
        //目前2访问2次，1访问一次，4访问一次，由于4的时间比较新，放入5的时候移除1元素。
        System.out.println("-=-=-=-");
//        cache.cache.entrySet().forEach(entry -> {
//            System.out.println(entry.getValue());
//        });
        cache.cache.forEach((k,v)-> System.out.println(v));

    }
}
