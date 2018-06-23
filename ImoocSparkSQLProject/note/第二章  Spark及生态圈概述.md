[TOC]

# 第二章 Spark及生态圈概述

- Spark产生背景
- Spark概述及特点
- Spark发展历史
- Spark Survey
- Spark对比Hadoop
- Spark和Hadoop的协作性
- Spark开发语言
- Spark运行模式



## Spark概述及特点

- Speed
- Ease of use
- Generality（通用性）
- Runs Everywhere





## Spark产生背景

**MapReduce的局限性：**
1）代码繁琐；
2）只能够支持map和reduce方法；
3）执行效率低下；
4）不适合迭代多次、交互式、流式的处理；



**框架多样化：**
1）批处理（离线）：MapReduce、Hive、Pig
2）流式处理（实时）： Storm、JStorm()
3）交互式计算：Impala

备注：

**JStorm** 是一个分布式实时计算引擎，类似Hadoop MapReduce的系统， 用户按照规定的编程规范实现一个任务，然后将这个任务递交给JStorm系统，Jstorm将这个任务跑起来，并且按**7 \* 24小时**运行起来，一旦中间一个worker 发生意外故障， 调度器立即分配一个新的worker替换这个失效的worker。因此，**从应用的角度，JStorm 应用是一种遵守某种编程规范的分布式应用。从系统角度，JStorm一套类似MapReduce的调度系统。从数据的角度，是一套基于流水线的消息处理机制。**实时计算现在是大数据领域中最火爆的一个方向，因为人们对数据的要求越来越高，实时性要求也越来越快，传统的 Hadoop Map Reduce，逐渐满足不了需求，因此在这个领域需求不断。

**Impala**是Apache Hadoop的开源，本地分析数据库。 它由Cloudera，MapR，Oracle和Amazon等供应商提供。 



学习、运维成本无形中都提高了很多

===> Spark 





## Spark发展历史



![Spark发展历史](https://upload-images.jianshu.io/upload_images/5959612-f591fd87aa6ab51f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## Spark Survey



**参与者**![参与者](https://upload-images.jianshu.io/upload_images/5959612-777b1273764e9620.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**使用Spark的行业**
![使用Spark的行业](https://upload-images.jianshu.io/upload_images/5959612-6650f0e9aea0bc92.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

使用场景

![使用场景](https://upload-images.jianshu.io/upload_images/5959612-d9d019b3b229c986.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



关注点

![关注点](https://upload-images.jianshu.io/upload_images/5959612-7b4b67e2de157251.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



![关注点](https://upload-images.jianshu.io/upload_images/5959612-261f3183fd1fafcd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![环境部署](https://upload-images.jianshu.io/upload_images/5959612-6ca5467867c93e0e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/5959612-ab424714f2f49145.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/5959612-c39974d59549a6d7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/5959612-e0a23ec3ad271789.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## Spark对比Hadoop

**Hadoop生态系统**

![Hadoop生态系统](https://upload-images.jianshu.io/upload_images/5959612-0ad2af4ca036578c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



**Sparl生态系统**

![Sparl生态系统](https://upload-images.jianshu.io/upload_images/5959612-433a494b1f02af03.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



BDAS:Berkeley Data Analytics Stack

**Spark和Hadoop生态圈对比：**

![Spark和Hadoop对比](https://upload-images.jianshu.io/upload_images/5959612-d5d50cc9c44c3647.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**Hadoop对比Spark：**

![Hadoop对比Spark](https://upload-images.jianshu.io/upload_images/5959612-ead5e9708a63ecd9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



**MapReduce对比Spark：**

![MapReduce对比Spark](https://upload-images.jianshu.io/upload_images/5959612-ac53cd3678fef586.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)





## Spark和Hadoop的协作性

- **Hadoop的优势**

![Hadoop的优势](https://upload-images.jianshu.io/upload_images/5959612-4378a0063928c399.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



- **Spark的优势**

![Spark的优势](https://upload-images.jianshu.io/upload_images/5959612-2806fa6d0e39ca53.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



- **Hadoop+Spark**

![Hadoop+Spark](https://upload-images.jianshu.io/upload_images/5959612-d871f35be8789abb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- **在工作当中**

![在工作当中](https://upload-images.jianshu.io/upload_images/5959612-09d6e0a7bcf0f3c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)









