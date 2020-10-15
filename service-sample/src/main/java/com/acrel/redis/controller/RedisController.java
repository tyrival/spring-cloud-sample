package com.acrel.redis.controller;

import com.acrel.api.ControllerName;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Redis示例
 * @Author: ZhouChenyu
 */
@RestController
@RequestMapping(ControllerName.SAMPLE_REDIS)
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redisson;

    /**
     * Redis并发锁示例
     *
     * 高并发场景下，如果多个请求同时操作同一个key的值，并且进行的操作非原子性时，可能会导致数据一致性问题。
     * 例如，当秒杀活动时，每个请求进来，需要进行如下步骤：
     * 1.读取redis中的库存余量；
     * 2.扣减库存；
     * 3.将扣减后的库存数量存入redis。
     *
     * 当2个请求A和B同时进来，
     * 1.A在读取到库存余量，假设为10
     * 2.B也读取到库存余量为10；
     * 3.A将库存-1，然后存入redis；
     * 4.B将库存-1，然后存入redis；
     * 此时redis中库存余量为9，却成立了2个订单，产生超卖问题。
     * 以上就是并发场景下的公用资源争抢，导致的数据一致性问题。
     *
     * 归根结底，并发的问题是由于我们CPU执行单个线程的代码，并不是连续执行的，而是以时间片为单位执行，
     * 所以出现了A和B两个线程的代码，可能出现交替执行的情况，这就导致B线程读取到脏数据。
     *
     * 实际上，我们希望A线程将“读取-扣减-存入”的过程变为原子操作，我们通常以加锁的方式来达到这一目的。
     * 以前常见的加锁方式是使用 synchronized，对整个过程进行加锁，这样如果A线程获得锁，B线程就只能在synchronized的位置等待；
     * 直到A线程执行完毕，B线程才开始执行synchronized之内的代码。
     * 但是，synchronized只能对当前应用的公共资源加锁，在分布式场景下，如果我们部署了2个服务端程序，
     * 请求A和B被分发到不同的服务器上，是没办法跨服务器加锁的。
     *
     * 分布式场景下，使用redis解决这一问题的方式通常有两种：
     * 1.lua脚本
     * 将“读取-扣减-存入”的过程编写到一个lua脚本中，而redis执行单个lua脚本的过程是原子的，
     * 而redis为单线程的，A线程的lua执行完毕，才开始执行B线程的lua，也就不会出现B线程脏读的问题。
     * 2.redisson
     * redisson可以进行分布式场景下的加锁。
     *
     * 注：以上所说的分布式，指的是服务端程序分布式部署在多台服务器上，而产生的问题，并不是指redis。
     *
     * 当redis为集群模式时，加锁过程如下
     * 1.A线程申请加锁；
     * 2.redis某个master节点进行响应线程A——加锁成功；
     * 3.master节点将锁同步到它下面的slave节点。
     * 由于redis集群是AP集群，当master节点在步骤3执行之前挂了，它下面的slave通过选举成为新的master，
     * 但是slave节点上没有锁，就可能出现锁失效的问题，仍旧会产生超卖的情况。
     *
     * 如果追求数据的强一致性，可以采取CP集群加锁的方式。
     * 例如使用zookeeper集群进行加锁，zookeeper为CP集群，它的加锁过程如下：
     * 1.A线程申请加锁；
     * 2.zookeeper某个节点进行加锁；
     * 3.这个节点将锁同步集群中所有其他节点；
     * 4.同步完成后，响应线程A——加锁成功。
     * 这就避免了数据一致性问题，但是性能会下降。这就需要根据具体的业务场景判断，是否允许这种小概率情况的发生。
     * 当然，Redis也有CP锁——Redlock红锁，不过从社区的反馈来看，redlock还有较大争议，不建议使用。
     *
     * 分布式系统的CAP定理：
     * 一个分布式系统，不可能同时满足一致性（Consistency）、可用性（Availability）、分区容忍性（Partition tolerance）这三点。
     * 1.一致性（Consistency)：同一个数据在集群中的所有节点，同一时刻是否都是同样的值。
     * 2.可用性（Availability）：集群中一部分节点故障后，集群整体是否还能处理客户端的更新请求。
     * 3.分区容忍性（Partition tolerance）：是否允许数据的分区，分区的意思是指是否允许集群中的节点之间无法通信。
     * 作为一个分布式系统，P（可分区）是必须满足的，那么剩下的问题就是实现C还是A。
     * 通常来说，C追求数据一致性，性能就会降低；A牺牲数据一致性，追求高性能。
     *
     */
    @RequestMapping("/redis_lock")
    public String redisLock() {

        // redis中需要操作的key
        String prodKey = "product_001";
        // 生成一个key，用于加锁，与redis中已经存在的key不可重复
        String lockKey = prodKey + "_lock";
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            // 加锁，并实现锁续命功能，默认30s超时时间，并且10s续命一次
            redissonLock.lock();
            // 从redis中读取库存数量，减1后存入，这个过程不是原子操作，所以需要进行加锁
            int stock = Integer.parseInt(redisTemplate.opsForValue().get(prodKey).toString());
            if (stock > 0) {
                int realStock = stock - 1;
                redisTemplate.opsForValue().set(prodKey, realStock + "");
                System.out.println("扣减成功，剩余库存:" + realStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            redissonLock.unlock();
        }
        return "end";
    }

}
