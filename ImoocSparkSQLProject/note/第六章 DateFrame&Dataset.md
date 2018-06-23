# 第六章 DateFrame&Dataset

[TOC]

- DataFrame产生背景
- DataFrame概述
- DataFrame对比RDD
- DataFrame基本API常用操作
- DataFrame API操作案例实战
- DataFrame与RDD互操作之一
- DataFrame与RDD互操作之二
- Dataset概述

## 1、DataFrame产生背景

- Spark RDD API vs MapReduce API
- R/Randas

DataFrame它不是Spark SQL提出的，而是早起在R、Pandas语言就已经有了的。

## 2、DataFrame概述

- A distributed collection of rows organized into named columns(RDD with schema)

schema一个表的结构信息

- It is conceptually equivalent to a table in a relational database or a data frame in R/Python, but with richer optimizations under the hood.（关系型数据库中的表并做了优化）
- An  abstraction for selecting, filtering, aggregation and plotting structured data
- Inspired by R and Pandas single machine small data processing experiences applied to distributed big data, i.e.
- Previously SchemaRDD(cf.Spark < 1.3)

![数据处理规模](https://upload-images.jianshu.io/upload_images/5959612-5fce3fdb2d54327e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



**Dataset ：**A Dataset is a distributed collection of data：分布式的数据集

**DataFrame ：**A DataFrame is a Dataset organized into named columns. 
以**列**（列名、列的类型、列值）的形式构成的分布式数据集，按照列赋予不同的名称

> student
>
> id:int
>
> ame:string
>
> city:string



It is conceptually equivalent to a table in a relational database or a data frame in R/Python

它在概念上等于一个关系型数据库中的表，或者是R/Python的数据框架





## 3、DataFrame对比RDD

![DataFrame对比RDD](https://upload-images.jianshu.io/upload_images/5959612-105d2f0c1b054f35.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**RDD：** 
	java/scala  ==> jvm
	python ==> python runtime

**DataFrame:**
	java/scala/python ==> Logic Plan



## 4、DataFrame基本API常用操作

- Create DataFrame
- printSchema
- show
- select
- filter

```scala
package com.boy.spark
import org.apache.spark.sql.SparkSession
/**
  * DataFrame API基本操作
  */
object DataFrameApp {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    //将json文件加载成一个dataframe
    val peopleDF = spark.read.format("json").load("src/main/data/people.json")

    //输出dataframe对应的schema信息
    peopleDF.printSchema()

    //输出数据集的前20条
    peopleDF.show()

    //查询某列所有的数据： select name from table
    peopleDF.select("name").show()

    //查询某几列所有的数据，并对列进行计算： select name,age+10 as age2 from table
    peopleDF.select(peopleDF.col("name"), (peopleDF.col("age") + 10).as("age2")).show()

    //根据某一列的值进行过滤： select * from table where age>19
    peopleDF.filter(peopleDF.col("age") > 19).show()

    //根据某一行进行分组，然后再进行聚合操作： select age,count(1) from table group by age
    peopleDF.groupBy("age").count().show()

    spark.close()

  }
}

```

## 5、DataFrame与RDD互操作之一：反射方式

- 使用反射来推断包含了特定数据类型的RDD的元数据（case class）
- 使用FDataFrame API或者sql方式编程

备注： 首先定义一个case class，用于获取字段属性，然后使用toDF()



## 6、DataFrame与RDD互操作方式二

DataFrame和RDD互操作的两种方式：
1）反射：case class   前提：事先需要知道你的字段、字段类型    
2）编程：Row             如果第一种情况不能满足你的要求（事先不知道列）
3)   选型：优先考虑第一种

## 7、DataFram API操作案例实战

- 学生信息统计案例

数据：student.data

```tex
1|Burke|1-300-746-8446|ullamcorper.velit.in@ametnullaDonec.co.uk
2|Kamal|1-668-571-5046|pede.Suspendisse@interdumenim.edu
3|Olga|1-956-311-1686|Aenean.eget.metus@dictumcursusNunc.edu
4|Belle|1-246-894-6340|vitae.aliquet.nec@neque.co.uk
5|Trevor|1-300-527-4967|dapibus.id@acturpisegestas.net
6|Laurel|1-691-379-9921|adipiscing@consectetueripsum.edu
7|Sara|1-608-140-1995|Donec.nibh@enimEtiamimperdiet.edu
8|Kaseem|1-881-586-2689|cursus.et.magna@euismod.org
9|Lev|1-916-367-5608|Vivamus.nisi@ipsumdolor.com
10|Maya|1-271-683-2698|accumsan.convallis@ornarelectusjusto.edu
11|Emi|1-467-270-1337|est@nunc.com
12|Caleb|1-683-212-0896|Suspendisse@Quisque.edu
13|Florence|1-603-575-2444|sit.amet.dapibus@lacusAliquamrutrum.ca
14|Anika|1-856-828-7883|euismod@ligulaelit.co.uk
15|Tarik|1-398-171-2268|turpis@felisorci.com
16|Amena|1-878-250-3129|lorem.luctus.ut@scelerisque.com
17|Blossom|1-154-406-9596|Nunc.commodo.auctor@eratSed.co.uk
18|Guy|1-869-521-3230|senectus.et.netus@lectusrutrum.com
19|Malachi|1-608-637-2772|Proin.mi.Aliquam@estarcu.net
20|Edward|1-711-710-6552|lectus@aliquetlibero.co.uk
21||1-711-710-6552|lectus@aliquetlibero.co.uk
22||1-711-710-6552|lectus@aliquetlibero.co.uk
23|NULL|1-711-710-6552|lectus@aliquetlibero.co.uk
```



```scala
package com.boy.spark

import org.apache.spark.sql.SparkSession

/**
  * DataFrame中的操作
  */
object DataFrameCase {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameCase").master("local[2]").getOrCreate()

    val rdd = spark.sparkContext.textFile("src/main/data/student.data")

    import spark.implicits._
    val studentDF = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()

    studentDF.show(30, false)
    //前10条
    studentDF.take(10).foreach(println)
    //第一条记录
    studentDF.first()
    //前三条
    studentDF.head(3)
    //指定的列
    studentDF.select("name", "email").show(30, false)
    //过滤name=''
    studentDF.filter("name='' OR name='NULL'").show()

    //name以M开头的人
    studentDF.filter("SUBSTR(name,0,1)='M'").show()
    //查询内置函数
    spark.sql("show functions").show(1000)

    // //name以M开头的人
    studentDF.filter("substring(name,0,1)='M'").show()

    //排序
    studentDF.sort(studentDF("name")).show()
    studentDF.sort(studentDF("name").desc).show()
    studentDF.sort("name", "id").show()
    studentDF.sort(studentDF("name").asc, studentDF("id").desc).show()

    //重命名
    studentDF.select(studentDF("name").as("student_name")).show()

    //join操作
    val studentDF2 = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()
    studentDF.join(studentDF2, studentDF.col("id") === studentDF2.col("id")).show()

    spark.stop()
  }

  case class Student(id: Int, name: String, phone: String, email: String)

}

```



## 8、Dateset概述

官网：https://spark.apache.org/docs/latest/sql-programming-guide.html#datasets-and-dataframes

> A Dataset is a distributed collection of data. Dataset is a new interface added in Spark 1.6 that provides the benefits of RDDs (strong typing, ability to use powerful lambda functions) with the benefits of Spark SQL’s optimized execution engine. 

> Python does not have the support for the Dataset API. 



- 静态类型（Static-typing）和运行时类型安全（runtime type-safety）

![image.png](https://upload-images.jianshu.io/upload_images/5959612-2470981d5b11cf04.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



DataFrame = Dataset[Row]
**Dataset**：强类型  typed  case class
**DataFrame**：弱类型   Row

**SQL**: 编译不进行语法检查和分析检错
	seletc name from person;  compile  ok, result no

**DF**：编译只进行语法检查，但无法进行分析检错
	df.select("name")  compile no
	df.select("nname")  compile ok  

**DS**：编译时就会进行语法和分析检查
	ds.map(line => line.itemid)  compile no

------

Boy-20180601













