#redis和memcached的区别
    redis: 原生集群,多数据结构,持久性,单线程(非阻塞IO多路复用) 6.0以后多线程
    memcached: 没有原生集群 ,只有String数据类型,没有持久性,多线程(非阻塞IO多路复用)

#redis和tair的区别
    tair支持跨机房管理、多集群管理、是否支持副本，而redis不支持
    

#Redis 的线程模型: 
	多个 socket
    IO 多路复用程序
	文件事件分派器
	事件处理器（连接应答处理器、命令请求处理器、命令回复处理器）
		每个请求就是一个socket,多个 socket 可能会并发产生不同的操作，每个操作对应不同的文件事件，但是 IO 多路复用程序会监听多个 socket，
		会将产生事件的 socket 放入队列中排队，事件分派器每次从队列中取出一个 socket，根据 socket 的事件类型交给对应的事件处理器进行处理。
    
    什么是IO多路复用? : 单个线程通过记录跟踪每个Sock(I/O流)的状态,通过拨开关的方式，来同时传输多个I/O流 ,尽量多的提高服务器的吞吐能力。 
    
    select 、poll、epoll的区别  
        epoll vs poll&select
        在需要同时监听的文件描述符数量增加时，select&poll是O(N)的复杂度，epoll是O(1)，在N很小的情况下，差距不会特别大，
        但如果N很大的前提下，一次O(N)的循环可要比O(1)慢很多，所以高性能的网络服务器都会选择epoll进行IO多路复用。
        epoll内部用一个文件描述符挂载需要监听的文件描述符，这个epoll的文件描述符可以在多个线程/进程共享，所以epoll的使用场景要比select&poll要多。
    
    select
        需要把fd_set从用户态拷贝到内核态（耗性能）,是一个bitmap结构的，默认长度1024，
        wait时需要遍历fd_set,而且长度越长，遍历开销就越大
        复杂度：O（N）
        阻塞式
        
    poll
        和select相似，把fd_set换成了pollfd，是一个数组
        复杂度：O（N）
        阻塞式
        
    epoll
        注册新的fd到epfd的epool对象空间中，并且会有一个回调，一旦中断，就会被放到ready队列里，
                    再在ready队列里取就绪的fd，并使用内存映射mmap拷贝到用户空间。
        fd支持较大数量，并且io效率不随fd数量的增加而下降，是用mmap加速内核与用户态的消息传递
        复杂度：O（1）
        默认阻塞式（延迟参数为0，就可以代表非阻塞式）
    
    
#Redis 单线程模型也能效率这么高？
	1.纯内存操作
	2.非阻塞IO多路复用
	3.避免了上下文切换问题
	4.C语言实现

    既然是单线程的,那么一台机器如何提高多核cpu利用率?
        可以在同一个服务器部署多个Redis的实例，并把他们当作不同的服务器来使用，在某些时候， 无论如何一个服务器是不够的， 所以，如果你想使用多个CPU，你可以考應一下分片 (shard)
#Redis 分区
    为什么要做Redis分区？
      分区可以让Red is管理更大的内存，Redis将可以使用所有机器的内存。如果没有分区，你最多只能使用一台机器的内存。
      分区使Red1s的计算能力通过简单地增加计算机得到成倍提升,Redis的网络带宽也会随着计算机和网卡的增加而成倍增长
    
    分区实现方案?
      1,客户端分区就是在客户端就已经决定数据会被存储到哪个redis节点或者从哪个redis节点读取。大多數客户端已经实现了客户端分区，
      2.代理分区
          意味着客户端将请求发送给代理，然后代理决定去哪个节点写数据或者读数据。代理根据分区规则决定请求哪些Redis实例，然后根据Redis的响应结果返回给客户端。redis和memcached的一种代理实现就是Twemproxy
      3、查询路由(Query routing）的意思是客户端随机地请求任意一个redis实例，然后由Redis将请求转发给正确的Redis节点。Redis Cluster实现了一种混合形式的查询路由，
          但井不是直接将请求从一个redis节点转发到另一个redis节点，而是在客户端的帮助下直接redirected到正确的redis节点。
      
#6.0后的多线程redis : 
    多线程部分只是用来处理网络数据的读写和协议解析，执行命令仍然是单线程
	总结: Redis 选择使用单线程模型处理客户端的请求主要还是因为 CPU 不是 Redis 服务器的瓶颈，
		所以使用多线程模型带来的性能提升并不能抵消它带来的开发成本和维护成本，系统的性能瓶颈也主要在网络 I/O 操作上；
		而 Redis 引入多线程操作也是出于性能上的考虑，对于一些大键值对的删除操作，通过多线程非阻塞地释放内存空间也能减少对 Redis 主线程阻塞的时间，提高执行的效率。

#五大数据结构及高级数据结构
    String: K-V缓存 ,计数器,共享用户Session,INCR、 DECR(递增、递减)----->点赞数和喜欢人数的实现,分布式锁
            
    Hash:  Map的嵌套结构,相当于java:Map<String,Map<String, String>>,
            1.购物车场景，用户id为key，商品id为value的key，单个商品的数量为value的value
    List: 消息队列,粉丝列表, 微博下拉不断分页,
          微信订阅号文章信息:key就是当前用户的id，只要关注的人发了文章，那么就是lpush到这个用户的列表里
    Set: 去重,  交集、并集、差集、微信朋友圈点赞、共同关注的人
         抽奖小程序
             value就是用户id，随机抽取几个用户----》SRANDMEMBER key value, SPOP key value(抽选数量）  前者抽选不删除value，后者抽选后弹出key
        
        
    Sorted Set: 延迟队列  geo:经纬度会换算成一个分值,视频做排行榜,按照时间、按照播放量、按照获得的赞数、商品销量排行榜、热搜的分数等 
                基于跳表实现: 复杂度和红黑树一样,但是实现简单,红黑树在插入和删除下需要重新平衡,性能不如跳表
                redis 跳表:
                  对比平街树红黑树，避免了rebalance的操作
                  复杂度与红黑树相同，实现跳表更简单
                  跳表更新时比平衡树要快，平衡数要考虑rebalance
    bitMap: 用户签到,统计活跃用户,当前在线用户量
    HyperLogLog: 做UV统计比set数据结构更好
    geo: 车场位置经纬度查找,用到了zset的数据结构,会把设置的经纬度转换成分值存储
    bloomFilter: 防止redis穿透发生等
    Pub/Sub: 订阅消费
    rediSearch: 强大的搜索引擎


#BloomFilter:
    布隆过滤器的原理是: 当一个元素被加入集合时，通过K个散列函数将这个元素映射成一个位数组中的K个点，把它们置为1。检索时，
                        我们只要看看这些点是不是都是1就（大约）知道集合中有没有它了.
                        如果这些点有任何一个0，则被检元素一定不在；如果都是1，则被检元素很可能在。这就是布隆过滤器的基本思想。
    Bloom Filter跟单哈希函数Bit-Map不同之处在于：Bloom Filter使用了k个哈希函数，每个字符串跟k个bit对应。从而降低了冲突的概率。
    缺陷: 提高了时间和空间上的效率,也牺牲了误判率和删除的便利性
          1.误判: 可能要查到的元素并没有在容器中，但是hash之后得到的k个位置上值都是1,导致误判
          2.删除: 如果直接把k个散列函数上都置为0, 那么会影响其他元素的误判,可采用  Counting Bloom Filter ;
    实现: 我们预估要存的数据量为n，期望的误判率为fpp
          bit数组大小的m的计算方式: -(n * ln * fpp) /(2 * ln)²;
          hash函数的个数k: 2 * ln * (m / n);
    
    常见的几个应用场景：
        1.cerberus在收集监控数据的时候, 有的系统的监控项量会很大, 需要检查一个监控项的名字是否已经被记录到db过了, 如果没有的话就需要写入db.
    
        2.爬虫过滤已抓到的url就不再抓，可用bloom filter过滤
    
        3.垃圾邮件过滤。如果用哈希表，每存储一亿个 email地址，就需要 1.6GB的内存（用哈希表实现的具体办法是将每一个 email地址对应成一个八字节的信息指纹，
          然后将这些信息指纹存入哈希表，由于哈希表的存储效率一般只有 50%，因此一个 email地址需要占用十六个字节。一亿个地址大约要 1.6GB，即十六亿字节的内存）。
          因此存贮几十亿个邮件地址可能需要上百 GB的内存。而Bloom Filter只需要哈希表 1/8到 1/4 的大小就能解决同样的问题。
    

#雪崩  :   
    缓存机器发生了全盘宕机,导致全部请求都落到了数据库  导致数据库也挂了

    方案:	事前：Redis 高可用，主从+哨兵，Redis cluster , 选择合适的内存淘汰策略.
            事中：本地 ehcache 缓存 + hystrix 限流&降级，避免 MySQL 被打死。
            事后：Redis 持久化，一旦重启，自动从磁盘上加载数据，快速恢复缓存数据。



#缓存穿透 :  
    黑客发送很多的无效key  ,缓存中没有 导致大量去请求全部去数据库查 
    方案:  ----- >  布隆过滤器   + 同步方法   参考DCL单例 
                    ip拦截:正常一个ip不可能在很短时间内访问多次
                

#缓存击穿 : 
    一个热点key失效的瞬间 ,大量请求访问数据库  
    方案 : 1. 若缓存的数据是基本不会发生更新的，则可尝试将该热点数据设置为永不过期。
           2. 若缓存的数据更新不频繁，且缓存刷新的整个流程耗时较少的情况下，则可以采用基于 Redis、zookeeper 等分布式中间件的分布式互斥锁，
          或者本地互斥锁以保证仅少量的请求能请求数据库并重新构建缓存，其余线程则在锁释放后能访问到新缓存。
           3.若缓存的数据更新频繁或者在缓存刷新的流程耗时较长的情况下，可以利用定时线程在缓存过期前主动地重新构建缓存或者延后缓存的过期时间，
          以保证所有的请求能一直访问到对应的缓存。
        4.查询数据库的时候方法加同步, 让一个先去查,然后在set到redis中, 同步方法进去后,在从redis中拿一下, 提供效率, 类似 DCL单例


                  
                      
                
    

#redis并发竞争key的问题 ?
       所谓 Redis 的并发竞争 Key 的问题也就是多个系统同时对⼀个 key 进⾏操作，但是最后执⾏的顺序
       和我们期望的顺序不同，这样也就导致了结果的不同！
    方案 : zookeeper分布式锁
             基于zookeeper临时有序节点可以实现的分布式锁。⼤致思想为：每个客户端对某个⽅法加锁时，在
        zookeeper上的与该⽅法对应的指定节点的⽬录下，⽣成⼀个唯⼀的瞬时有序节点。 判断是否获取锁的
        ⽅式很简单，只需要判断有序节点中序号最⼩的⼀个。 当释放锁的时候，只需将这个瞬时节点删除即
        可。同时，其可以避免服务宕机导致的锁⽆法释放，⽽产⽣的死锁问题。完成业务流程后，删除对应的
        ⼦节点释放锁。
        
        临时节点:临时节点的生命周期和客户端会话绑定在一起，客户端会话失效，则这个节点就会被自动清除(防止宕机产生死锁)
        永久节点:需要有删除操作来主动删除这个节点
        

#保证redis和数据库双写的一致性 ?
	读时,先读redis,没有则读数据库,并且回写到redis
	更新时:
	    先删除缓存,在更新数据库
	        避免脏数据 可采用 延时双删 ,第二次删除作为异步,保证吞吐量,但是不能完全避免脏数据,尽可能把脏数据有有效时间变更短
	    
	    先更新数据库,在删除缓存 
	        避免脏数据 可采用 延时双删
	
	


#Redis 过期策略: 
	定期删除: 指的是 Redis 默认是每隔 100ms 就随机抽取一些设置了过期时间的 key，检查其是否过期，如果过期就删除。
	惰性删除: 获取 key 的时候，如果此时 key 已经过期，就删除，不会返回任何东西。
    定时删除
      含义：在设置key的过期时间的同时，为该key创建一个定时器，让定时器在key的过期时间来临时对key进行删除
      优点：保证内存被尽快释放
      缺点：若过期key很多，别除这些key会占用很多的CPU时间，在CPU时间紧张的情况下，CPU不能把所有的时间用来做要紧的事儿，
           还需要去花时间州除汶些kev定时器的创建耗时,若为每一个设置过期时间的key创建一个定时器（将会有大量的定时器产生），性能影响严重
#内存淘汰机制: 
	allkeys-lru：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的 key（这个是最常用的）。
	    说明: 底层大概就是一个双向链表+key/value字典，链表元素被使用的顺序进行排列（某个元素被访问后，
	          会移动到链表头部，所以链表元素排列顺序就是元素最近被使用的时间顺序）。
	allkeys-lfu（Least Frequently Used ，最近最少使用算法）也是一种常见的缓存算法。
        说明：如果一个数据在最近一段时间很少被访问到，那么可以认为在将来它被访问的可能性也很小。因此，当空间满时，最小频率访问的数据最先被淘汰。     

#Redis 的持久化方式
	1.  RDB：RDB 持久化机制，是对 Redis 中的数据执行周期性的持久化。
	2.  AOF：AOF 机制对每条写入命令作为日志，以 append-only 的模式写入一个日志文件中，在 Redis 重启的时候，可以通过回放 AOF 日志中的写入指令来重新构建整个数据集。
	如果同时使用 RDB 和 AOF 两种持久化机制，那么在 Redis 重启的时候，会使用 AOF 来重新构建数据，因为 AOF 中的数据更加完整。
	
	RDB 优缺点:
		1.RDB 会生成多个数据文件，每个数据文件都代表了某一个时刻中 Redis 的数据，非常适合做冷备，
		    可以将这种完整的数据文件发送到一些远程的安全存储上去，比如说 Amazon 的 S3 云服务上去，在国内可以是阿里云的 ODPS 分布式存储上，
		    以预定好的备份策略来定期备份 Redis 中的数据。
		2. 可以让 Redis 保持高性能，因为 Redis 主进程只需要 fork 一个子进程，让子进程执行磁盘 IO 操作来进行 RDB 持久化即可。
		3.丢失的数据会比AOF的多,RDB每隔5分钟一次,AOF可以做到实时或者1秒钟一次
		4.RDB 每次在 fork 子进程来执行 RDB 快照数据文件生成的时候，如果数据文件特别大，可能会导致对客户端提供的服务暂停数毫秒，或者甚至数秒。

	AOF 优缺点:
		1.更好的保证数据不丢失,并通过后台线程执行 , 最多丢失1秒的数据
		2.AOF日志文件已append-only模式写入,没有磁盘寻址开销,性能高
		3.数据恢复: 如误删后,可以将aof文件的命令删除,然后在将aof文件放回去,通过恢复机制自动恢复所有数据.
		4.AOF 开启后，支持的写 QPS 会比 RDB 支持的写 QPS 低,如果每秒一次写AOF,性能还是可以的,如果实时写,性能会大大降级.
	
	AOP重写:
	        aof重写作用?
	        降低磁盘占用量,提高磁盘利用率
	        提高持久化效率,降低持久化写时间,提高IO性能
	        降低数据恢复用时,提高数据恢复效率
		1.AOF重写可以产⽣⼀个新的AOF⽂件，这个新的AOF⽂件和原有的AOF⽂件所保存的数据库状态⼀样，但体积更⼩。	
		2.在执⾏ BGREWRITEAOF 命令时，Redis 服务器会维护⼀个 AOF 重写缓冲区，该缓冲区会在⼦进程创建
		  新AOF⽂件期间，记录服务器执⾏的所有写命令。     当⼦进程完成创建新AOF⽂件的⼯作之后，服务器会将
		  重写缓冲区中的所有内容追加到新AOF⽂件的末尾，使得新旧两个AOF⽂件所保存的数据库状态⼀致。最
		  后，服务器⽤新的AOF⽂件替换旧的AOF⽂件，以此来完成AOF⽂件重写操作
	        AOF重写触发机制:
		1.aof_current_size > auto-aof-rewrite-min-size	当前AOF尺寸超过最小重写需要的尺寸时
		2.aof_current_size-aof_base_size/aof_base_size > auto-aof-rewrite-percentage	AOF文件增长率超过多少时
	总结: 
		1.不要仅使用RDB,这会让你丢失更多的数据,虽然它的性能很高,用到了子线程写,并且是通过磁盘,不占用cpu
		2.也不要仅使用AOF,做冷备恢复速度没有RDB快,RBD可以避免 AOF 这种复杂的备份和恢复机制的 bug
		3.redis4.0后支持同时开启两种,用AOF保证了数据的不丢失,用RDB做冷备,用来快速恢复数据. 
			(AOF 重写的时候就直接把 RDB 的内容写到 AOF ⽂件开头)
			
#Redis cluster集群:
    工作原理:
	1.自动将数据进行分片，每个 master 上放一部分数据
	2.提供内置的高可用支持，部分 master 不可用时，还是可以继续工作的
         节点间的内部通信机制:
	1.集中式,
		将集群元数据（节点信息、故障等等）几种存储在某个节点上,底层基于 zookeeper（分布式协调的中间件）对所有元数据进行存储维护
	        好处: 元数据的读取和更新，时效性非常好，一旦元数据出现了变更，就立即更新到集中式的存储中，其它节点读取的时候就可以感知到
	        坏处: 所有的元数据的更新压力全部集中在一个地方，可能会导致元数据的存储有压力。
	2.gossip协议 
		集群模式下,每个 Redis 要放开两个端口号，一个是 6379，一个就是 加1w 的端口号，比如 16379。
		16379 端口号是用来进行节点间通信的，也就是cluster bus 的通信，用来进行故障检测、配置更新、故障转移授权。
		cluster bus 用了另外一种二进制的协议， gossip 协议，用于节点间进行高效的数据交换，占用更少的网络带宽和处理时间。

        gossip包含4种消息:
			meet (遇见): 新节点加入
			ping: 频繁给其他节点发送,自己的信息和需要维护的元数据
			pong: 返回 ping 和 meet 包含自己信息和其他信息
			fail: 一个节点判定另一个节点失败后,会通知其他节点
                        好处: 元数据的更新比较分散，不是集中在一个地方，更新请求会陆陆续续打到所有节点上去更新，降低了压力
                        坏处: 元数据的更新有延时，可能导致集群中的一些操作会有一些滞后
                        
	总结:先是集中式存储,维护时会用gossip协议通信,所有节点都持有一份元数据，不同的节点如果出现了元数据的变更，就不断将元数据发送给其它的节点，让其它节点也进行元数据的变更。
	
         分布式寻址算法:
	1.hash 算法（大量缓存重建）
		首先计算哈希值，然后对节点数量取模,如果节点增加或者减少必然会导致获取到的缓存数据出错,最终导致宕机雪崩的可能发生
	2.一致性hash算法（自动缓存迁移）+ 虚拟节点（自动负载均衡）
		??????
	3.redis集群的hash slot算法  哈希槽
		Redis cluster 有固定的 16384 个 hash slot，对每个 key 计算 CRC16 值，然后对 16384 取模，可以获取 key 对应的 hash slot。

    Redis cluster 的高可用与主备切换原理:
        和哨兵模式几乎一样,从主观宕机到客观宕机,然后进行选举最合适从节点,小于超时时间,offset越大越有机会得到选举,选出从节点,在继续投票决定,大部分同意后再会正式切换
        和哨兵模式比较 :  非常相似, redis集群的强大,直接集成了 repilcation(复制) 和 sentinel (哨兵)的功能

    Redis 集群模式性能优化
        1.主节点可以不做RDB或者AOF,可以让从节点做AOF,每一秒一次
        2.为了复制速度和链接的稳定性,主从最好在一个局域网内
        3.主从复制做单向链表复制,Master <- Slave1 <- Slave2 <- Slave3,容易解决单点故障问题.
        

#Redis 主从架构:    
- 参考： [https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/redis-master-slave.md]()

        一主多从，主负责写，并且将数据复制到其它的 slave 节点，从节点负责读。所有的读请求全部走从节点。这样也可以很轻松实现水平扩容，支撑读高并发。
        Redis主从复制核心机制:
            1.异步方式复制到从节点
            2.可以一主多从
            3.多个从节点可以相连接
            4.主复制从时,不会阻塞主节点的正常工作
            5.从节点在做复制的时候,不会阻塞自己的查询操作,   期间会用旧数据来提供服务  ,但是复制完成时,需要更新数据集,这时候会停止服务
            6.从节点提高了读的吞吐量
        Redis 主从复制的核心原理:
            1.当启动一个从节点,它会发一个psync给主节点
            2.若是初次链接,做一次全量复制 (主节点启动一个后台线程,生成一份RDB快照 , 同时还会将从客户端 client 新收到的所有写命令缓存在内存中,
                而后将RDB文件发送给从节点 ,并且如果有旧数据清空旧数据 , 从节点写先磁盘,然后在从磁盘加载到内存,再将内存中的命令写入节点中)
            3.如果RDB时间超过了60s ,从节点会认为失败,可以适当配置这个参数   (对于千兆网卡的机器，一般每秒传输 100MB，6G 文件，很可能超过 60s)
            4.如果在复制期间，内存缓冲区持续消耗超过 64MB，或者一次性超过 256MB，那么停止复制，复制失败。 (client-output-buffer-limit slave 256MB 64MB 60)
            5.如果从节点开启了 AOF，那么会立即执行 BGREWRITEAOF，重写 AOF。
            
        Redis主从复制的断点续传:
            1.如果主从复制过程中，网络连接断掉了，那么可以接着上次复制的地方，继续复制下去，而不是从头开始复制一份。
            2.主节点会在内存中维护一个 backlog，主 和 从都会保存一个 replica offset 还有一个 master run id，offset 就是保存在 backlog 中的。
            如果 master 和 slave 网络连接断掉了，slave 会让 master 从上次 replica offset 开始继续复制，如果没有找到对应的 offset，那么就会执行一次 全量复制。
        Redis无磁盘化复制:
            1.主节点 在内存中直接创建 RDB ，然后发送给 从节点, 不会在自己本地落地磁盘了。只需要在配置文件中开启 repl-diskless-sync yes 即可。
               Redis过期 key 处理:
            1.从节点不会过期,只会等待主节点过期或被LRU淘汰,那么会模拟一条del命令发送给从节点.
    
        复制的完成流程:
            1.从节点启动,在本地保存主节点的host和ip
            2.从节点内部有定时任务,每秒会确认是否有主节点要链接,如果有就跟主节点建立socket网络来链接
            3.如果主节点配置了requirepass,那么从节点会发送masterauth口令过去认证
            4.主节点启动全量复制,将所有数据都发送给从节点,实现数据同步
            5.每次主节点接受到新的数据都异步发送给从节点
    
        Redis主从心跳检测:
            主节点默认每隔 10秒 发送一次 heartbeat，从节点每隔 1秒 发送一个 heartbeat。


#Redis 哨兵集群实现高可用:   
- 参考： [https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/redis-sentinel.md]()
            
            1.集群监控: 负责主从节点的进程是否正常工作
            2.消息通知: 如果某个实例有故障,哨兵会发送消息作为警报给管理员
            3.故障转移: 如果主节点挂了,会自动转移到从节点上
            4.配置中心: 如果故障转移发生了,会通知客户端新的主节点地址
            补充: 判断一个主节点是否宕机,需要大部分哨兵集群同意才行 
        Redis哨兵的核心知识:
            1.至少需要3个实例,来保证自己的健壮性
            2.主从模式➕哨兵,是不保证数据零丢失的 -->  (主从切换) , 只能保证Redis集群的高可用性 , 并且需要进行充足的测试和演练
            3. (同意人数)quorum = 1。用来配置主节点宕机时,哨兵集群来判断是否宕机, 如果等于1,如果有一个哨兵确定了宕机,就可以进行切换从节点作为主节点
            经典的 3 节点哨兵集群是这样的：
                   +----+
                   | M1 |
                   | S1 |
                   +----+
                      |
            +----+    |    +----+
            | R2 |----+----| R3 |
            | S2 |         | S3 |
            +----+         +----+
        
        Redis 哨兵主备切换的数据丢失问题:
            1.异步复制的数据丢失,(在复制到从节点途中,主节点挂了,数据丢失)
            
            2.脑裂导致的数据丢失( 某个 master 所在机器突然脱离了正常的网络，跟其他 slave 机器不能连接，但是实际上 master 还运行着。
                此时哨兵可能就会认为 master 宕机了，然后开启选举，将其他 slave 切换成了 master。这个时候，集群里就会有两个 master ，也就是所谓的脑裂。)
            此时虽然某个 slave 被切换成了 master，但是可能 client 还没来得及切换到新的 master，还继续向旧 master 写数据。
            因此旧 master 再次恢复的时候，会被作为一个 slave 挂到新的 master 上去，自己的数据会清空，重新从新的 master 复制数据。
            而新的 master 并没有后来 client 写入的数据，因此，这部分数据也就丢失了。
        如何避免数据丢失:
            配置: 至少有一个从节点数据复制和同步的延迟不超过10秒,只要全部节点都超过了10秒,主节点就不能再接收任何请求了
                    min-slaves-to-write 1
                    min-slaves-max-lag 10
            异步复制和脑裂导致数据丢失,只能尽可能的保证丢失数据的减少,但不能保证完全不丢失  (最多只丢失10秒)
        Redis的主观宕机和客观宕机转换机制:
            1.主观宕机 : 一个哨兵认为主节点宕机了,就是主观宕机
            2.客观宕机: 如果 (同意人数)quorum 达到了,那么就是多个哨兵认为宕机了,就是客观宕机
            哨兵判断宕机,通过配置 is-master-down-after-milliseconds 如果ping一个主节点超过了配置的毫秒数,就认为是宕机
            如果一个哨兵在指定时间内，收到了 quorum 数量的其它哨兵也认为那个 主节点 是 主观宕机的，那么就认为是 客观宕机了。
        Redis哨兵集群的自动发现机制:
            1.通过redis自身的pub/sub发布订阅实现的 ,达到了哨兵间的通信 
            2.每隔两秒钟，每个哨兵都会往自己监控的某个 master+slaves 对应的 __sentinel__:hello channel 里发送一个消息，内容是自己的 host、ip 和 runid 还有对这个 master 的监控配置。
            3.每个哨兵也会去监听自己监控的每个 master+slaves 对应的 __sentinel__:hello channel，然后去感知到同样在监听这个 master+slaves 的其他哨兵的存在。
            4.每个哨兵还会跟其他哨兵交换对 master 的监控配置，互相进行监控配置的同步。
        Redis主从切换如何选举一个最适合的从节点?
            1..和主节点的断开连接的时长     如断开时间超过了 down-after-milliseconds 的10倍➕主节点的宕机时长 就不会被选举
            2. 按照 从节点 优先级进行排序，从节点 越低，优先级就越高。
            3. 如果优先级相同 看offset, offset越靠后,说明复制了越多,会优先选择
            4. 上面都相同看run id,哪个小选哪个
        quorum 和 majority (同意人数 和 大多数)
            主从切换,必须得到大多数哨兵的同意才可以,
            如果 quorum < majority，比如 5 个哨兵，majority 就是 3，quorum 设置为 2，那么就 3 个哨兵授权就可以执行切换。
            但是如果 quorum >= majority，那么必须 quorum 数量的哨兵都授权，比如 5 个哨兵，quorum 是 5，那么必须 5 个哨兵都同意授权，才能执行切换。
            切换后会得到一个新的version号,如果哨兵切换失败,超过了 failover-timeout的时间,其他哨兵会接替执行切换
