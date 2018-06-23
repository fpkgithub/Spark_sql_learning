# 第四章 Spark SQL概述

[TOC]

- Spark SQL前世今生
- SQL on Hadoop常用框架
- Spark SQL概述
- Spark SQL愿景
- Spark SQL架构



# Spark SQL前世今生

**为什么需要SQL?**

- 事实上的标准
- 易学易用
- 受众面大

**比如：**

文本文件进行统计分析：
id, name, age, city
1001,zhangsan,45,beijing
1002,lisi,35,shanghai
1003,wangwu,29,tianjin
.......

table定义：person
column定义：
	id：int
	name：string
	age： int
	city：string
hive：load data

sql: query....



**Shark**

1）Hive On Spark

Hive: 类似于sql的Hive QL语言， sql==>mapreduce

- 特点：mapreduce
- 改进：hive on tez、hive on spark、hive on mapreduce

2）Shark特性和缺点

Spark: hive on spark ==> shark(hive on spark)

- shark推出：欢迎， 基于spark、基于内存的列式存储、与hive能够兼容
- 缺点：hive ql的解析、逻辑执行计划生成、执行计划的优化是依赖于hive的，仅仅只是把物理执行计划从mr作业替换成spark作业

Shark终止以后，产生了2个分支：
1）hive on spark
	Hive社区，源码是在Hive中
2）Spark SQL
	Spark社区，源码是在Spark中
	支持多种数据源，多种优化技术，扩展性好很多

![Shark终止以后](https://upload-images.jianshu.io/upload_images/5959612-08172e36a54ad6c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## SQL on Hadoop常用框架

**SQL on Hadoop**
1）Hive 
	sql ==> mapreduce
	metastore ： 元数据 
	sql：database、table、view
	facebook

2）impala
	cloudera ： cdh（建议大家在生产上使用的hadoop系列版本）、cm（提供web界面安装hadoop）
	sql：自己的守护进程执行的，非mr
	metastore

3）presto
	facebook
	京东
	sql

4）drill
	支持sql
	访问：hdfs、rdbms、json、hbase、mongodb、s3（亚马逊的数据系统）、hive

5）Spark SQL
	sql
	dataframe/dataset api
	metastore
	访问：hdfs、rdbms、json、hbase、mongodb、s3、hive  ==> 外部数据源



## Spark SQL概述

- Spark sql是Spark的核心组件，在1.0时发布。
- Runs SQL/Hive QL queries including UDFs UDAFs and SerDes
- Connect existing BI  tool   to  Spark throughth JDBC
- Binding in Python,Scala,Java and R
- The not-so-secret truth ... is about more than SQL.



> Spark SQL is Apache Spark's module for working with structured data. 
>
> Spark SQL是Apache Spark用于处理结构化数据的模块。 



**有见到SQL字样吗？**
Spark SQL它不仅仅有访问或者操作SQL的功能，还提供了其他的非常丰富的操作：外部数据源、优化

**Spark SQL概述小结：**
1）Spark SQL的应用并不局限于SQL；
2）访问hive、json、parquet等文件的数据；
3）SQL只是Spark SQL的一个功能而已；
===> Spark SQL这个名字起的并不恰当
4）Spark SQL提供了SQL的api、DataFrame和Dataset的API；

## Spark SQL愿景

- Write less code
- Read less data
- Let the optimizer do the hard work（把优化工作交给底层优化器去执行）



## Spark SQL架构

![Spark SQL架构](https://upload-images.jianshu.io/upload_images/5959612-159b2293e80cc968.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



------

Boy-20180531





