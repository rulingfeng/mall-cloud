##HashMap
####1.7与1.8的区别
    1.7是数组+链表
    1.7中resize()方法负责扩容，inflateTable()负责创建表
    1.7版本中对于键为null的情况调用putForNullKey()方法
    1.7节点添加到链表头部,头插法
    1.7中是通过更改hashSeed值修改节点的hash值从而达到rehash时的链表分散
    1.7中rehash时有可能改变链表的顺序（头插法导致）
    1.7在插入数据之前扩容
    
    1.8则是数组+链表+红黑树结构
    1.8中resize()方法在表为空时，创建表；在表不为空时，扩容
    1.8中没有区分键为null的情况
    1.8节点添加到链表尾部,尾插法(不容易出现环型链表)
    1.8中键的hash值不会改变
    1.8rehash时保证原链表的顺序
    1.8插入数据成功之后扩容
    
##ConcurrentHashMap
####1.7与1.8的区别
    https://www.cnblogs.com/leeego-123/p/12156165.html
    JDK1.7中,采用头插法
    使用segment+hashentry来实现。初始化时，计算出segement数组的大小ssize和每个segment中HashEntry数组的大小cap，
        并初始化segement数组的第一个元素，其中ssize大小为2的幂次方，默认为16，cap大小也是2的幂次方，最小值为2。
        segement在实现上继承了ReetrantLock，这样就自带了锁的功能。
    
    JDK1.8中,采用尾插法
    放弃了segment的设计，取而代之的是Node+CAS+Synchronized来保证并发安全。只有在执行第一次put方法时，才会调用initTable（）初始化Node数组。
    
 
 
  