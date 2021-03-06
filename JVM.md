#JVM
   ##新生代
   ####为什么有新生代?
        如果对象都在一个区域,每次GC都要全部扫描,存在效率问题.分代可以分别控制不同的回收频率和不同的回收算法,确保GC性能全局最优
   ####为什么新生代采用复制算法?
        性能快,有90%的对象都会被回收,复制成本算法低,并且保证空间没有碎片,并且时间复杂度比标记整理算法要高 (虽然都不会有碎片)
   ####为什么新生代需要两个Survivor区？
        为了节省空间考虑，如果采用传统的复制算法，只有一个Survivor区，则Survivor区大小需要等于Eden区大小，此时空间消耗是8 * 2，
            而两块Survivor可以保持新对象始终在Eden区创建，存活对象在Survivor之间转移即可，空间消耗是8+1+1，明显后者的空间利用率更高。
   ####新生代的实际可用空间是多少？
        YGC后，总有一块Survivor区是空闲的，因此新生代的可用内存空间是90%。
   
   ####4种回收器:
        1.SerialGC（串行回收器），最古老的一种，单线程执行，适合单CPU场景。
        2.ParNew（并行回收器），将串行回收器多线程化，适合多CPU场景，需要搭配老年代CMS回收器一起使用。
        3.ParallelGC（并行回收器），和ParNew不同点在于它关注吞吐量，可设置期望的停顿时间，它在工作时会自动调整堆大小和其他参数。
        4.G1（Garage-First回收器），JDK 9及以后版本的默认回收器，兼顾新生代和老年代，将堆拆成一系列Region，不要求内存块连续，新生代仍然是并行收集。
    
   ####YGC的触发时机
        当Eden区空间不足时，就会触发YGC。结合新生代对象的内存分配看下详细过程：
    
            1、新对象会先尝试在栈上分配，如果不行则尝试在TLAB分配，否则再看是否满足大对象条件要在老年代分配，最后才考虑在Eden区申请空间。
            
            2、如果Eden区没有合适的空间，则触发YGC。
            
            3、YGC时，对Eden区和From Survivor区的存活对象进行处理，如果满足动态年龄判断的条件或者To Survivor区空间不够则直接进入老年代，
                如果老年代空间也不够了，则会发生promotion failed，触发老年代的回收。否则将存活对象复制到To Survivor区。
            
            4、此时Eden区和From Survivor区的剩余对象均为垃圾对象，可直接抹掉回收。
            
            此外，老年代如果采用的是CMS回收器，为了减少CMS Remark阶段的耗时，也有可能会触发一次YGC，这里不作展开。
   ####可作为YGC时GC Root的对象包括以下几种：
   
       1、虚拟机栈中引用的对象
       2、方法区中静态属性、常量引用的对象
       3、本地方法栈中引用的对象
       4、被Synchronized锁持有的对象
       5、记录当前被加载类的SystemDictionary
       6、记录字符串常量引用的StringTable
       7、存在跨代引用的对象
       8、和GC Root处于同一CardTable的对象
        
            
   
  