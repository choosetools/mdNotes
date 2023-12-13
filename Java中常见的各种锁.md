//todo：

[不可不说的Java“锁”事 - 美团技术团队 (meituan.com)](https://tech.meituan.com/2018/11/15/java-lock.html)（主要）

[十分钟学会18种Java锁（图文讲解） - 掘金 (juejin.cn)](https://juejin.cn/post/6976622790724026376#heading-0)

[java中的Lock锁_java lock-CSDN博客](https://ashen.blog.csdn.net/article/details/107333451?spm=1001.2101.3001.6650.18&utm_medium=distribute.pc_relevant.none-task-blog-2~default~BlogCommendFromBaidu~Rate-18-107333451-blog-89607758.235^v39^pc_relevant_3m_sort_dl_base4&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~BlogCommendFromBaidu~Rate-18-107333451-blog-89607758.235^v39^pc_relevant_3m_sort_dl_base4&utm_relevant_index=22)

[Java：java学习笔记之volatile关键字的简单理解和使用-CSDN博客](https://blog.csdn.net/JMW1407/article/details/122302803)



![img](.\images\7f749fc8.png)

# 乐观锁vs悲观锁

乐观锁与悲观锁是一种广义上的概念，体现了看待线程同步的不同角度，在Java和数据库中都有此概念对应的实际应用。

先说概念





**什么是原子操作？**

> 原子操作就是: **`不可中断的一个或者一系列操作`**, 这些操作看成一个整体，也就是不会被线程调度机制打断的操作, 运行期间不会有任何的上下文切换(context switch)。

就比如一串代码：i++

这是原子操作吗？感觉好像看上去有些像原子操作，但其实不是

我们来看看字节码文件：

![image-20231213211107146](.\images\image-20231213211107146.png)

我们可以发现，i++代码操作编译成字节码文件后，内部其实执行了四条指令：getstatic表示获取到i变量的值；iconst_1表示将常量1压入栈顶；iadd表示把i与1相加，然后把结果值压入栈顶；putstatic表示把相加后的值赋值给变量i。

我们可以看到，一个i++代码执行会执行四个操作，这也就导致了i++操作并不是原子操作，也就可能会发生线程安全的问题。

假设有多个线程，线程A开始执行，获取到了i的值0，然后此时失去了CPU的执行权，然后线程B获得了执行权，线程B完成了i++的四个操作，此时线程A再去执行，他的i值是++前的值，也就是0，那么两个线程执行完后，发现i只会自增1次，这个时候就发生了线程的安全问题。

但是，如果将i++用一个锁锁起来之后，i++自增中的四个操作就是一个整体了，那么也就是原子操作了。
