#Mysql
   ###sql调优
        1.排除缓存干扰
            加上SQL NoCache去跑sql
        2.explain计划:
            1.id
                包含一组数字，表示查询中执行select子句或操作表的顺序
                
                id值越大优先级越高，越先被执行,id相同，执行顺序由上至下
            2.select_type
                示查询中每个select子句的类型（简单OR复杂）
                
                a. SIMPLE：查询中不包含子查询或者UNION
                b. 查询中若包含任何复杂的子部分，最外层查询则被标记为：PRIMARY
                c. 在SELECT或WHERE列表中包含了子查询，该子查询被标记为：SUBQUERY
                d. 在FROM列表中包含的子查询被标记为：DERIVED（衍生）用来表示包含在from子句中的子查询的select，mysql会递归执行并将结果放到一个临时表中。
                    服务器内部称为"派生表"，因为该临时表是从子查询中派生出来的
                e. 若第二个SELECT出现在UNION之后，则被标记为UNION；若UNION包含在FROM子句的子查询中，外层SELECT将被标记为：DERIVED
                f. 从UNION表获取结果的SELECT被标记为：UNION RESULT
                
                SUBQUERY和UNION还可以被标记为DEPENDENT和UNCACHEABLE。
                DEPENDENT意味着select依赖于外层查询中发现的数据。
                UNCACHEABLE意味着select中的某些 特性阻止结果被缓存于一个item_cache中。
                
            3.type :  
                从左到右，性能从最差到最好
                ALL：Full Table Scan， MySQL将遍历全表以找到匹配的行
                index:  Full Index Scan，index与ALL区别为index类型只遍历索引树
                range: 索引范围扫描，对索引的扫描开始于某一点，返回匹配值域的行。显而易见的索引范围扫描是带有between或者where子句里带有<, >查询。
                        当mysql使用索引去查找一系列值时，例如IN()和OR列表，也会显示range（范围扫描）,当然性能上面是有差异的。
                ref:    使用非唯一索引扫描或者唯一索引的前缀扫描，返回匹配某个单独值的记录行
                eq_ref: 类似ref，区别就在使用的索引是唯一索引，对于每个索引键值，表中只有一条记录匹配，简单来说，
                        就是多表连接中使用primary key或者 unique key作为关联条件
                const:  当MySQL对查询某部分进行优化，并转换为一个常量时，使用这些类型访问。如将主键置于where列表中，MySQL就能将该查询转换为一个常量
                system: system是const类型的特例，当查询的表只有一行的情况下，使用system
                NULL:   MySQL在优化过程中分解语句，执行时甚至不用访问表或索引，例如从一个索引列里选取最小值可以通过单独索引查找完成。
           
            4.possible_keys
                指出MySQL能使用哪个索引在表中找到记录，查询涉及到的字段上若存在索引，则该索引将被列出，但不一定被查询使用
            5.key
                显示MySQL在查询中实际使用的索引，若没有使用索引，显示为NULL
            6.key_len
                表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度（key_len显示的值为索引字段的最大可能长度，并非实际使用长度，即key_len是根据表定义计算而得，不是通过表内检索出的）
            7.ref
                表示上述表的连接匹配条件，即哪些列或常量被用于查找索引列上的值
            8.rows
                表示MySQL根据表统计信息及索引选用情况，估算的找到所需的记录所需要读取的行数
            9. Extra
                包含不适合在其他列中显示但十分重要的额外信息
                
        3. 覆盖索引
            在自己的索引上就查到自己想要的，不要去主键索引查了。
        4.联合索引
            例: 查询商品名字和库存,名称和库存的联合索引，这样名称查出来就可以看到库存了，不需要查出id之后去回表再查询库存了
        5.最左匹配原则
            例: 模糊查询 name like '敖丙%',左边不加%号会走索引
        6.索引下推
            select * from table where name like '敖%' and size=22 and age = 20;
            改成
            select * from (select id,name from table where size=22 and age = 20) a where a.name like '熬%';
        7.前缀索引
            可以定义字符串的一部分作为索引
        8.条件字段函数操作
             select * from tradelog where id + 1 = 10000 就走不上索引
             select * from tradelog where id = 9999就可以。
        
            
            
            
   ###隔离级别
        1.读未提交
        2.读已提交
        3.可重复读
        4.串行化
        
        脏读: 其他事务还没有提交的事务被读了出来
        不可重复读: 两次查询,中间有事务进行了修改操作,读出来的不一样
        幻读: 两次查询,中间有事务进行了增加操作,读出来的数据变多了
   
   ###Mysql主从
        主从复制是指将主数据库的DDL和DML操作通过二进制日志传到从数据库上，然后在从数据库上对这些日志进行重新执行，
            从而使从数据库和主数据库的数据保持一致。
        
   ###主从复制的原理     
        1.MySql主库在事务提交时会把数据变更作为事件记录在二进制日志Binlog中；
        2.主库推送二进制日志文件Binlog中的事件到从库的中继日志Relay Log中，之后从库根据中继日志重做数据变更操作，
            通过逻辑复制来达到主库和从库的数据一致性；
        3.MySql通过三个线程来完成主从库间的数据复制，其中Binlog Dump线程跑在主库上，I/O线程和SQL线程跑着从库上；
        4.当在从库上启动复制时，首先创建I/O线程连接主库，主库随后创建Binlog Dump线程读取数据库事件并发送给I/O线程，
            I/O线程获取到事件数据后更新到从库的中继日志Relay Log中去，之后从库上的SQL线程读取中继日志Relay Log中更新的数据库事件并应用，如下图所示。
            
  ![](document/resource/mysql主从复制原理.png)
  
  ###MySQL log
        undo log 
            回滚和多版本控制(MVCC)
            insert一条数据了，那undo log会记录的一条对应的delete日志。
            update一条记录时，它会记录一条对应相反的update记录。
            
        binlog : 
            1.记录了数据库表结构和表数据变更,比如增删改等
            2.复制和恢复数据
        
        redo log (innoDB引擎才有):
            mysql修改行,会先写在内存中,然后在写到磁盘中,如果写磁盘的过程中数据库挂了,就用到了redo log, redo log是记录修改的内容的
            并且用到了NIO,恢复速度快.
            