# 第八章 SparkSQL愿景

[TOC]

**SparkSQL愿景之一更少的代码**

## 1 代码和可读性

MapRduce代码量大，逻辑混乱

使用RDD编程代码精简了，但是不容易看明白

使用DataFrame或者SQL方式代码精简且逻辑清晰，可读性强



## 2 统一访问操作接口

写更少的输入和输出

- Unified interface to reading/writing data in a variety of formats

![image.png](https://upload-images.jianshu.io/upload_images/5959612-dc26f9ceeadeb30d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/5959612-6d86494175e485f0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![image.png](https://upload-images.jianshu.io/upload_images/5959612-2c0616491de89c2a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



![image.png](https://upload-images.jianshu.io/upload_images/5959612-5e2ece4db388401c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- ETL Using Custom Data Sources

![自定义数据源](https://upload-images.jianshu.io/upload_images/5959612-71ea2903fd2fcd5b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## 3 强有力的API支持

- Solve common problems concisely using DataFrame functions:

  a . Selecting required columns

  b. Joining different data sources

  c. Aggregation(count,sum,average,etc)

  d.Filtering

  e. ......

看源码



## 4 Schema推导

- Schema inference:

  A. Big data tends to be dirty

  B. Infer schema from semi-structured data(i.e.JSON)

  C. Merge different but compatible versions of shema(i.e.JSON,Parquet)

举例

![Schema推导](https://upload-images.jianshu.io/upload_images/5959612-ff810b4525cba7da.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



代码实例

```scala
package com.boy.spark

import org.apache.spark.sql.SparkSession

object SchemaInferApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("").master("local[2]").getOrCreate()
    val df = spark.read.format("json").load("src/main/data/json_schema_infer.json")
    df.printSchema()
    df.show()
    spark.close()
  }
}
```



## 5 Schema Merge

> 官网：https://spark.apache.org/docs/latest/sql-programming-guide.html#schema-merging
>
> Like ProtocolBuffer, Avro, and Thrift, Parquet also supports schema evolution. Users can start with a simple schema, and gradually add more columns to the schema as needed. In this way, users may end up with multiple Parquet files with different but mutually compatible schemas. The Parquet data source is now able to automatically detect this case and merge schemas of all these files. 

Since schema merging is **a relatively expensive operation,** and is not a necessity in most cases, we turned it off by default starting from 1.5.0. You may enable it by

1. setting data source option `mergeSchema` to `true` when reading Parquet files (as shown in the examples below), or
2. setting the global SQL option `spark.sql.parquet.mergeSchema` to `true`.



## 6 Partition Discovery

分区探测

A. Discover Hive style partitioned table directory layout

B. Infer partition column types and values from partition directory paths

官网：https://spark.apache.org/docs/latest/sql-programming-guide.html#partition-discovery



## 7 执行速度更快

![image.png](https://upload-images.jianshu.io/upload_images/5959612-499dda51786d456f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

DataFrame里面封装了一段小巧的逻辑执行计划，python端把计划发送给Jvm，计算部分还是在Jvm里面执行 

如果使用RDD Python时，python的Vm需要和JVM跨进程数据交换，开销性能大，所以它的执行速度慢

RDD Scala：RDD方式是函数式的，而且强调的是一个不可变的，那么在大部分的场景下，它都是在创建一个对象而不是修改一个已经存在的老对象，这样就使得spark程序在运行时可能会创建非常多的临时对象，那么GC的压力就会很大，所以性能会差一点 

DataFrame/SparkSQL：这个框架的原则就是尽可能的使用已有的对象，虽然打破了不可变的特性，但是在将数据返回给用户的时候，还是会将这些数据转换成不可变的，所以是由DataFrame API开发时，是可以利用Spark内部的这些优化特性，从而使得我们的业务程序不单代码写得少，而且执行速度更快。	 



## 8 Spark愿景之二-读更少的数据

- The faster way to process big data **is** to ignore it.

- Spark SQL can help you need read less data automatically

  A. Using columnar formats (i.e. parquet,ORC列式存储) **只读需要的行**

  B. Using partitioning prunig(i.e., /year=2014/month=02/...)  **使用分区裁剪**

  C. Skipping data using min/max statistics   **使用最小/最大统计数据跳过数据**

  D. Pushing predicates into storage systems (i.e., JDBC)  谓词下压，将条件下压到数据源中，当我们查询的时候，就可以过滤到不符合条件数据，然后把我们需要的数据拿出来

  E. ......

**面试的优化点：**

**忽略和过滤不需要的数据，分区裁剪，跳过数据，谓词下压条件到数据源中等**

- Columnar Storage 列式存储

![image.png](https://upload-images.jianshu.io/upload_images/5959612-c881ddf706b4757c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

行式存储和列式存储相比，列式存储在读取指定列的数据时速度更快

- Saves space 节省空间

  A. Type specific encoding  可以指定列编码格式

  B. Columner layout compresses better  可以对列进行压缩

- Enable better scans 更好的查询

  A. Loads only the columns that need to be accessed; 仅加载需要访问的列

  B. Limits IO to data actually needed 对于IO进行了限制，减少了IO操作

### 为什么还要用行式存储呢？

参考：

[OLAP和OLTP的区别(基础知识)](https://www.cnblogs.com/beyondstorm/archive/2006/08/12/475011.html)

[一图理解行式数据库和列式数据库的区别](https://www.jianshu.com/p/ad2533e5cfaa)

**行存储的数据库更适合OLTP** ：on-line transaction processing 在线事物处理

**列存储的数据库更适合OLAP：**On-Line Analytical Processing  在线分析处理

**行式数据库**是按照行存储的，行式数据库擅长随机读操作不适合用于大数据。像SQL server,Oracle，mysql等传统的是属于行式数据库范畴。

**列式数据库**从一开始就是面向大数据环境下数据仓库的**数据分析**而产生。

### 列式数据库的优缺点：

**优点：**

- 极高的装载速度 （最高可以等于所有硬盘IO 的总和，基本是极限了）
- 适合大量的数据而不是小数据
- 实时加载数据仅限于增加（删除和更新需要解压缩Block 然后计算然后重新压缩储存）
- 高效的压缩率，不仅节省储存空间也节省计算内存和CPU。
- 非常适合做聚合操作。

**缺点：**

- 不适合扫描小量数据
- 不适合随机的更新
- 批量更新情况各异，有的优化的比较好的列式数据库（比如Vertica）表现比较好，有些没有针对更新的数据库表现比较差。
- 不适合做含有删除和更新的实时操作

 

## 9 Spark愿景之三让查询优化器帮助我们优化执行效率

**Let the optimizer do the hard work**

优化例子1：先用where条件进行过滤和裁剪，然后进行查询

![优化例子1](https://upload-images.jianshu.io/upload_images/5959612-ee823420d93810f2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



优化例子2：再join表时，先进行条件的过滤后再进行join连接

![优化例子2](https://upload-images.jianshu.io/upload_images/5959612-75e6ea0f1e761946.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



- DataFrame Internals  DataFrame内部

  A. Represented internally as a "logical plan"; 实际就是一个逻辑计划

  B. Execution is lazy, allowing it to be optimized by Catalyst; 是懒加载，可以被Catalyst处理优化

![DataFrame内部](https://upload-images.jianshu.io/upload_images/5959612-c328f4f1c33ff26f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

DataFrame和SQL使用的是同一套执行和优化的操作



备注：

Catalyst：Spark Sql优化器  [ 认识SparkSQL的Catalyst ](https://blog.csdn.net/lw_ghy/article/details/60778157)

> 任何一个优化器工作原理都大同小异：SQL语句首先通过Parser模块被解析为语法树，此棵树称为Unresolved Logical Plan；Unresolved Logical Plan通过Analyzer模块借助于数据元数据解析为Logical Plan；此时再通过各种基于规则的优化策略进行深入优化，得到Optimized Logical Plan；优化后的逻辑执行计划依然是逻辑的，并不能被Spark系统理解，此时需要将此逻辑执行计划转换为Physical Plan； 





## 10 Spark SQL愿景总结

Create and Run Spark Programs Faster:

A. Write less code

B. Read less data

C. Let the optimizer do the hard work





------

Boy-20180603








