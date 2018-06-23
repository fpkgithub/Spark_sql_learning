# 第七章 External Data Source

[TOC]

- 产生背景
- 概述
- 目标
- 操作Parquet文件数据
- 操作Hive表数据
- 操作Mysql表数据
- 综合使用

## 1：产生背景

> Every Spark application starts with loading data and ends with saving data
>
> Loading and saving Data is not easy
>
> Parse raw data:text/json/parquet
>
> Convert data format transformation
>
> Datasets stored in various Formats/Systems 

![Datasets stored in various Formats/Systems](https://upload-images.jianshu.io/upload_images/5959612-f73fadb60be7fcb8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**用户：**

- 方便快速从不同的数据源（json、parquet、rdbms），经过混合处理（json join parquet），
- 再将处理结果以特定的格式（json、parquet）写回到指定的系统（HDFS、S3）上去

> Parquet仅仅是一种存储格式，它是语言、平台无关的，并且不需要和任何一种数据处理框架绑定，目前能够和Parquet适配的组件包括下面这些，可以看出基本上通常使用的查询引擎和计算框架都已适配，并且可以很方便的将其它序列化工具生成的数据转换成Parquet格式。
>
> - 查询引擎: Hive, Impala, Pig, Presto, Drill, Tajo, HAWQ, IBM Big SQL
> - 计算框架: MapReduce, Spark, Cascading, Crunch, Scalding, Kite
> - 数据模型: Avro, Thrift, Protocol Buffers, POJOs

Spark SQL 1.2 ==> 外部数据源API

## 2：概述

- An extension way to integreate a various of external data sources into Spark SQL
- Can read and write DataFrames using a variety of formats and storage systems
- Data Sources API can automatically prune columns(列的裁剪) and push filters to the source（下压数据源）:Parquet/JDBC
- New API introduces in 1.2

1、一种扩展的方式，这种扩展的方式可以集成各种外部数据源，把这些外部数据源集成到SparkSQL里面来，既然已经集成到Spark SQL里面，那么就可以使用DataFrame的API或者SQL的API进行操作，这就很方便了。

2、它能够读和写DataFrame使用各种各样的format格式和存储系统，这怎么理解呢？就是说你可以load进来各种存储系统中的数据，这些数据可以是不同的格式，然后把这些数据读取成一个DataFrame，那么同样的也可以把DataFrame指定格式指定输出路径，写到外面去也是可以的。

3、外部数据源的API可以自动做一些列的裁剪，什么叫列的裁剪，你如一个表厘有A B C D E五个列，在做select的时候你只需要A B C这几个列，那么D E这两个列就相当于被裁减了，还可以把一些条件下压到数据源端去。

4、外部数据源是在1.2版本才提出来的

再来看一张图。

![img](https://pic2.zhimg.com/80/v2-bbd7ccb9901c59fabb76ebab27b2d168_hd.jpg)

有了Data Source API以后，你下面各个地方的各式各样的格式的数据都能够非常方便的读取和操作了，标准的写法就是xxx.read.dormat("json/txt/csv...........").load(path)



## 3：目标

1、Developer：build libraries for various data sources

对于开发人员来说我们只需要构建一个library，构建库，这个库是专门用来对于外部的数据源的，比如说你现在想通过Spark来访问Hbase，访问微博的一些数据，那么你必须开发相应的实现类才行，这是对于开发人员来说的

2、对于使用人员来说就非常容易就可以加载或者保存为DataFrame了，一般用法就是：
**读：**spark.read.format(format)

format:

build-in: json parquet jdbc csv(2.x)

packages: 外部的， 并不是spark内置的 这有一个网站 ：[Spark Packages]( https://spark-packages.org/)

**写：**spark.write.format("parquet").save(path)



## 4：操作Parquet文件数据

其实很简单只有两步

1、spark.read.format("parquet").load(path)

2、dataframe.format("parquet").save(path)

还是在IDEA里面开发一下

```scala
package com.boy.spark
import org.apache.spark.sql.SparkSession
/**
  * Parquet文件操作
  */
object ParquetAPP {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ParquetAPP").master("local[2]").getOrCreate()

    /**
      * spark.read.format("parquet").load这是标准写法
      */
    val userDF = spark.read.format("parquet").load("src/main/data/users.parquet")

    userDF.printSchema()
    userDF.show()

    userDF.select("name","favorite_color").show()
    userDF.select("name","favorite_color").write.format("json").save("src/main/data/jsonout")

    //也会显示
    spark.read.format("parquet").load("src/main/data/users.parquet").show()

    //会报错，因为sparksql默认处理的format就是parquet
    spark.read.format("parquet").load("src/main/data/people.json").show()

    spark.read.format("parquet").option("path","src/main/data/users.parquet").load().show()

    spark.stop()
  }
}
```

处理parquet数据

```scala
RuntimeException: file:/home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json is not a Parquet file

val DEFAULT_DATA_SOURCE_NAME = SQLConfigBuilder("spark.sql.sources.default")
    .doc("The default data source to use in input/output.")
    .stringConf
    .createWithDefault("parquet")
```

**注意USING的用法**

CREATE TEMPORARY VIEW parquetTable
USING org.apache.spark.sql.parquet
OPTIONS (
  path "/home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet"
)

## 5：操作Hive表数据

这个其实也简单

1、spark.table(tableName)

2、dataframe.write.saveAsTable(tableName)

我们用spark shell来操作，那么我们依次要打开mysql.server和你的Hadoop

好了我们拿emp这张表格开始讲，首先按照部门编号来统计员工数量



SELECT * FROM parquetTable



**错误**

```shell
spark.sql("select deptno,count(1) from emp where group by deptno").filter("deptno is not null").write.saveAsTable("hive_table2")

//错误 这是什么情况？看看错误说count(1)这个属性名称包含了一些无效的参数，
//其实他已经告诉我们了，请使用一个别名重新命名它，那好办，我们改一下代码。
org.apache.spark.sql.AnalysisException: Attribute name "count(1)" contains invalid character(s) among " ,;{}()\n\t=". Please use alias to rename it.;

//添加count别名
spark.sql("select deptno,count(1) as mount from emp where group by deptno").filter("deptno is not null").write.saveAsTable("hive_table2")
```

执行成功，但是我们看到上面有个200，这个是什么呢？这个配置的是你分区的数量当你数据join或者聚合进行shuffling的时候，这个参数是可以调整的，我们看官网 



官网说了默认是200，那么我们可以这样设置它。 

```shell
spark.sqlContext.setConf("spark.sql.shuffle.partitions","10")
```

在生产环境中一定要注意设置spark.sql.shuffle.partitions，默认是200



## 6：**操作MySQL表数据**

操作MySQL的数据官网：https://spark.apache.org/docs/latest/sql-programming-guide.html#datasets-and-dataframes

**方法一：**

```scala
val jdbcDF = spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306/hive").option("dbtable", "hive.TBLS").option("user", "root").option("password", "root").option("driver", "com.mysql.jdbc.Driver").load()

jdbcDF.printSchema
jdbcDF.show
jdbcDF.select("TBL_ID","TBL_NAME").show
```

看官网介绍： 


错误：java.sql.SQLException: No suitable driver

添加驱动：option("driver", "com.mysql.jdbc.Driver")



**方法二：**

```scala
import java.util.Properties

val connectionProperties = new Properties()
connectionProperties.put("user", "root")
connectionProperties.put("password", "root")
connectionProperties.put("driver", "com.mysql.jdbc.Driver")
val jdbcDF2 = spark.read.jdbc("jdbc:mysql://localhost:3306", "hive.TBLS", connectionProperties)

jdbcDF.printSchema
jdbcDF.show
jdbcDF.select("TBL_ID","TBL_NAME").show
```

我们再看看用spark SQL怎么操作mysql表数据 

```scala
CREATE TEMPORARY VIEW jdbcTable
USING org.apache.spark.sql.jdbc
OPTIONS (
  url "jdbc:mysql://localhost:3306",
  dbtable "sparksql.TBLS",
  user "root",
  password "root",
  driver "com.mysql.jdbc.Driver"
)
```

## 7：综合使用

**将mysql里面的一张DEPT表和hive里面的一张emp表进行join操作** 

首先在mysql里建一张DEPT的表： 

```shell
create database spark;
use spark;

CREATE TABLE DEPT(
DEPTNO int(2) PRIMARY KEY,
DNAME VARCHAR(14) ,
LOC VARCHAR(13) ) ; 

INSERT INTO DEPT VALUES(10,'ACCOUNTING','NEW YORK');
INSERT INTO DEPT VALUES(20,'RESEARCH','DALLAS');
INSERT INTO DEPT VALUES(30,'SALES','CHICAGO');
INSERT INTO DEPT VALUES(40,'OPERATIONS','BOSTON');

mysql> select * from DEPT;
+--------+------------+----------+
| DEPTNO | DNAME      | LOC      |
+--------+------------+----------+
|     10 | ACCOUNTING | NEW YORK |
|     20 | RESEARCH   | DALLAS   |
|     30 | SALES      | CHICAGO  |
|     40 | OPERATIONS | BOSTON   |
+--------+------------+----------+
```

再hive中创建emp表

```shell
scala> spark.table("emp").show
+-----+------+---------+----+----------+-------+------+------+
|empno| ename|      job| mgr|  hiredate|    sal|  comm|deptno|
+-----+------+---------+----+----------+-------+------+------+
| 7369| SMITH|    CLERK|7902|1980-12-17|  800.0|  null|    20|
| 7499| ALLEN| SALESMAN|7698| 1981-2-20| 1600.0| 300.0|    30|
| 7521|  WARD| SALESMAN|7698| 1981-2-22| 1250.0| 500.0|    30|
| 7566| JONES|  MANAGER|7839|  1981-4-2| 2975.0|  null|    20|
| 7654|MARTIN| SALESMAN|7698| 1981-9-28| 1250.0|1400.0|    30|
| 7698| BLAKE|  MANAGER|7839|  1981-5-1| 2850.0|  null|    30|
| 7782| CLARK|  MANAGER|7839|  1981-6-9| 2450.0|  null|    10|
| 7788| SCOTT|  ANALYST|7566| 1987-4-19| 3000.0|  null|    20|
| 7839|  KING|PRESIDENT|null|1981-11-17| 5000.0|  null|    10|
| 7844|TURNER| SALESMAN|7698|  1981-9-8| 1500.0|   0.0|    30|
| 7876| ADAMS|    CLERK|7788| 1987-5-23| 1100.0|  null|    20|
| 7900| JAMES|    CLERK|7698| 1981-12-3|  950.0|  null|    30|
| 7902|  FORD|  ANALYST|7566| 1981-12-3| 3000.0|  null|    20|
| 7934|MILLER|    CLERK|7782| 1982-1-23| 1300.0|  null|    10|
| 8888|  HIVE|  PROGRAM|7839| 1988-1-23|10300.0|  null|  null|
+-----+------+---------+----+----------+-------+------+------+
```

编码：

```shell
package com.boy.spark

import org.apache.spark.sql.SparkSession

/**
  * 使用外部数据源综合查询Hive和MySQL的表数据
  */
class HiveMySQLApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("").master("local[2]").getOrCreate()

    //加载Hive表数据
    val hiveDF = spark.table("emp")

    //加载MySQL表数据
    val mysqlDF = spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306").option("dbtable", "spark.DEPT").option("user", "root").option("password", "root").option("driver", "com.mysql.jdbc.Driver").load()

    //JOIN
    val resultDF = hiveDF.join(mysqlDF, hiveDF.col("deptno") === mysqlDF.col("DEPTNO"))
    resultDF.show()
    resultDF.select(hiveDF.col("empno"), hiveDF.col("ename"), mysqlDF.col("deptno"), mysqlDF.col("dname")).show()

    spark.stop()
  }
}
```



------

Boy-20180602




















