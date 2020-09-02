#多线程
   ##synchronized
        偏向锁、
        自旋锁、
        适应性自旋锁、
        锁消除: 一个方法中循环调用new StringBuffer(synchronized修饰的方法),不会被其他线程调用,就会把锁消除
                不可能存在竞争，那么就执行锁消除。锁消除可以节省毫无意义的请求锁的时间。
        锁粗化: StringBuffer 循环调用 可以把锁放在循环外面 (同try放到循环外面)
        轻量级锁
        等
        
        
        synchronized 应用在方法上时，在字节码中是通过方法的 ACC_SYNCHRONIZED 标志来实现的。
        synchronized 应用在同步块上时，在字节码中是通过 monitorenter 和 monitorexit 实现的
        以上2者都是 monitor监视器 来实现同步的
        
        
   #####讲一下 synchronized 关键字的底层原理
   
   ##AQS abstractQueuedSynchronizer
        抽象队列同步器框架,提供了一个FIFO的双向链表,是一个用来实现同步锁以及其他涉及到同步功能的核心组件,常见的有:ReentrantLock\CountDownLatch
        
        AQS使用一个可见性的int成员变量来表示同步状态，通过内置的FIFO队列来完成获取资源线程的排队工作。并使用CAS对该同步状态进行原子操作实现对其值的修改。
        
        每个结构的特点是每个数据结构都有两个指针,分别指向前节点和后节点,每个Node由线程封装,当线程抢锁失败后会封装成Node放到AQS\FIFO队列去
   #####AQS定义两种资源共享方式
        1.Exclusive（独占）：只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁：
              公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
              非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的
        2.Share（共享）：多个线程可同时执行，
              如Semaphore/CountDownLatch/CyclicBarrier/ReadWriteLock 
              
   ##什么是线程安全?
        与其说线程安全,不如说内存安全,在堆内存中的数据由于可以被任何线程访问到，在没有限制的情况下存在被意外修改的风险.
        即堆内存空间在没有保护机制的情况下，对多线程来说是不安全的地方，因为你放进去的数据，可能被别的线程“破坏”。
   
   ##CAS 
        1. 是乐观锁的一种实现方式，是一种轻量级锁,
        2. 容易发生ABA问题,需要进行值和版本号比较(compare and swap 只比较值)
        3. CAS操作长时间不成功的话，会导致一直自旋，相当于死循环了，CPU的压力会很大。
        
        
        
        
        
        
        
            
   
  