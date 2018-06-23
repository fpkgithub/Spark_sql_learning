# 第五章 从Hive平滑过渡到Spark SQL

[TOC]

- SQLContext(1.X)/HiveContext(1.X)/SparkSession(2.X)使用
- spark-shell/spark-sql的使用
- thriftserver/beeline的使用
- jdbc方式编程访问



## SQLContext/HiveContext/SparkSession使用

- Spark1.X中Spark SQL的入口点：SQLContext

```scala
val sc: SparkContext   // an existing SparkContext.
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
```



### 1 SQLContext的使用

**1) 本地local测试，配置args[0]**

```scala
package com.boy.spark
import java.io.File
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * SQLContext的使用
  * 注意：IDEA是在本地，而测试数据是在服务器上 ，能不能在本地进行开发测试的？
  */
object SQLContextApp {

  def main(args: Array[String]): Unit = {

    //设置Edit Configurations program arguments "src\main\data\people.json"
    val path = args(0)

    //1) 创建相应的Context
    val sparkConf = new SparkConf()
    //在测试或者生产中，AppBName和Master我们是通过脚本进行指定
    //sparkConf.setAppName("SQLContextApp").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)

    //2) 相关的处理：josn
    val people = sqlContext.read.format("json").load(path)
    people.printSchema()
    people.show()

    //3) 关于资源
    sc.stop()
  }
}
```

maven编译打包，上传jar包到集群里面，提交作业

```shell
$ mvn clean package -DiskipTests
```

**2) 提交Spark Application到环境中运行**

```shell
//一般情况下，将提交的命令放到shell脚本中执行
$spark-submit --name SQLContextApp --class com.boy.spark.SQLContextApp --master local[2] /home/hadoop/lib/mylib/sql-1.0.jar /home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json
```



### 2 HiveContext的使用

Spark1.x中Spark SQL的入口点：HiveContext

```scala
// sc is an existing SparkContext.
val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
```

看spark-sql官网文档：http://spark.apache.org/docs/1.6.1/sql-programming-guide.html

注意：
1）To use a HiveContext, you do not need to have an existing Hive setup
2）hive-site.xml

```scala
package com.boy.spark
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
/**
  * HiveContext的使用
  */
object HiveContextApp {

  def main(args: Array[String]): Unit = {
    //设置Edit Configurations program arguments "src\main\data\people.json"
    val path = args(0)

    //1) 创建相应的Context
    val sparkConf = new SparkConf()
    //在测试或者生产中，AppBName和Master我们是通过脚本进行指定
    //sparkConf.setAppName("HiveContextApp").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)
    val hiveContext = new HiveContext(sc)
    //2) 相关的处理：josn
    hiveContext.table("emp").show
    //3) 关于资源
    sc.stop()
  }
}

```

maven编译打包，上传jar包到集群里面，提交作业

```shell
$ mvn clean package -DiskipTests
//脚本hivecontext.sh
$ spark-submit --name HiveContextApp --class com.boy.spark.HiveContextApp --master local[2] /home/hadoop/lib/mylib/sql-1.0.jar

//带jdbc驱动包
$ spark-submit --name HiveContextApp --class com.boy.spark.HiveContextApp --master local[2] --jars /home/hadoop/software/mysql-connector-java-5.1.27-bin.jar /home/hadoop/lib/mylib/sql-1.0.jar
```



### 3 SparkSession的使用

Spark2.x中Spark SQL的入口点：SparkSession

```scala
val  spark = SparkSession
			.builder()
			.appName("Spark SQL basic example")
			.config("spark.some.config.option","some-value")
			.getOrCreate()
```

```scala
package com.boy.spark

import org.apache.spark.sql.SparkSession

/**
  * SparkSession的使用
  */
object SparkSessionApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkSessionApp").master("local[2]").getOrCreate()
    val people = spark.read.json("src/main/data/people.json")
    people.show()
    spark.close()
  }
}
```

## spark-shell/spark-sql的使用

- hive-site.xml配置文件
- --jars传递mysql驱动包

1） spark-shell

```shell
$ spark-shell --master local[2]
scala> spark.sql("select * from emp").show
scala> spark.sql("show tables").show
```

备注：

- jars包
- hive-site.xml文件移到spark/conf/中，且添加

```shell
<property>
  	<name>hive.metastore.schema.verification</name>
  	<value>false</value>
</property>
  
```

  

2）spark-sql

```shell
$ spark-sql --master local[2]
spark-sql> select * from emp;
spark-sql> select * from emp e join dept d on e.deptno=d.deptno;
```



创建一个表

```scala
create table t(key string, value string);

//执行计划
explain extended select a.key*(2+3), b.value from  t a join t b on a.key = b.key and a.key > 3;
```

a: Parsed Logical Plan

b: Analyzed Logical Plan

c: Optimized Logical Plan

d: Physical Plan

```shell
== Parsed Logical Plan ==
'Project [unresolvedalias(('a.key * (2 + 3)), None), 'b.value]
+- 'Join Inner, (('a.key = 'b.key) && ('a.key > 3))
   :- 'UnresolvedRelation `t`, a
   +- 'UnresolvedRelation `t`, b

== Analyzed Logical Plan ==
(CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE)): double, value: string
Project [(cast(key#49 as double) * cast((2 + 3) as double)) AS (CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE))#53, value#52]
+- Join Inner, ((key#49 = key#51) && (cast(key#49 as double) > cast(3 as double)))
   :- SubqueryAlias a
   :  +- MetastoreRelation default, t
   +- SubqueryAlias b
      +- MetastoreRelation default, t

== Optimized Logical Plan ==
Project [(cast(key#49 as double) * 5.0) AS (CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE))#53, value#52]
+- Join Inner, (key#49 = key#51)
   :- Project [key#49]
   :  +- Filter (isnotnull(key#49) && (cast(key#49 as double) > 3.0))
   :     +- MetastoreRelation default, t
   +- Filter ((cast(key#51 as double) > 3.0) && isnotnull(key#51))
      +- MetastoreRelation default, t

== Physical Plan ==
*Project [(cast(key#49 as double) * 5.0) AS (CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE))#53, value#52]
+- *SortMergeJoin [key#49], [key#51], Inner
   :- *Sort [key#49 ASC NULLS FIRST], false, 0
   :  +- Exchange hashpartitioning(key#49, 200)
   :     +- *Filter (isnotnull(key#49) && (cast(key#49 as double) > 3.0))
   :        +- HiveTableScan [key#49], MetastoreRelation default, t
   +- *Sort [key#51 ASC NULLS FIRST], false, 0
      +- Exchange hashpartitioning(key#51, 200)
         +- *Filter ((cast(key#51 as double) > 3.0) && isnotnull(key#51))
            +- HiveTableScan [key#51, value#52], MetastoreRelation default, t

```



## thriftserver/beeline的使用

- **启动thriftserver**
- **通过beeline连接到thriftserver**



1)  启动thriftserver: 默认端口是10000 ，可以修改

```shell
$ ./start-thriftserver.sh --master local[2]
```

2)  启动beeline

```shell	
$ beeline -u jdbc:hive2://localhost:10000 -n hadoop

//web查看作业运行的状况
http://192.168.95.128:4040/jobs/
//启动两个以上的beeline
http://192.168.95.128:4041/jobs/
```

备注：如果同时启动两个以上的beeline，则web页面的端口是自增1

![WebUI界面](https://upload-images.jianshu.io/upload_images/5959612-81914f3a8ce06af9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



**修改thriftserver启动占用的默认端口号：**

```shell
$ ./start-thriftserver.sh  \
--master local[2] \
--jars ~/software/mysql-connector-java-5.1.27-bin.jar  \
--hiveconf hive.server2.thrift.port=14000 

$ ./beeline -u jdbc:hive2://localhost:14000 -n hadoop
```



**thriftserver和普通的spark-shell/spark-sql有什么区别？**
1）spark-shell、spark-sql都是一个spark  application；
2）thriftserver， 不管你启动多少个客户端(beeline/code)，永远都是一个spark application
      解决了一个数据共享的问题，多个客户端可以共享数据；



## jdbc方式编程访问

- maven添加依赖：org.spark-project.hive#hive-jdbc
- 开发代码访问thriftserver

**注意事项：在使用jdbc开发时，一定要先启动thriftserver**
Exception in thread "main" java.sql.SQLException: 
Could not open client transport with JDBC Uri: jdbc:hive2://hadoop001:14000: 
java.net.ConnectException: Connection refused

```scala
<dependency>
        <groupId>org.spark-project.hive</groupId>
        <artifactId>hive-jdbc</artifactId>
        <version>1.2.1.spark2</version>
</dependency>

package com.boy.spark
import java.sql.DriverManager
/**
  * 通过JDBC的方式访问
  *
  */
object SparkSQLThriftServerApp {

  def main(args: Array[String]): Unit = {

    Class.forName("org.apache.hive.jdbc.HiveDriver")
    val conn = DriverManager.getConnection("jdbc:hive2://192.168.95.128:14000", "hadoop", "")
    val pstmt = conn.prepareStatement("select empno, ename, sal from emp")
    val rs = pstmt.executeQuery()
    while (rs.next()) {
      println("empno:" + rs.getInt("empno") +
        ", ename:" + rs.getString("ename") +
        ", sal:" + rs.getDouble("sal")
      )
    }

    rs.close()
    pstmt.close()
    conn.close()

  }
}
```



------

Boy-20180601
