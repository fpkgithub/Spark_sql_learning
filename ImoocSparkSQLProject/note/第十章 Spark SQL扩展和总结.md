# 第十章 Spark SQL扩展和总结

[TOC]



## 1 课程目录

Spark SQL务必要掌握的N件事情

- Spark SQL Use Cases  使用场景
- Loading Data  加载数据
- DataFrame Functions vs SQL 
- Schemas
- Loading & Saving Resultschagn
- SQL Function Coverage 覆盖面
- Working with JSON
- External Data sources

## 2 Spark SQL使用场景

Spark SQL Use Cases

- Ad-hoc querying of data in files 数据文件的即席查询（用户在使用时根据自己的需要进行查询）
- Live SQL analytics over streaming data 实时流式处理
- ETL capabilities alongside familiar SQL   ETL数据处理
- Interaction with external Databases   交互式的外部数据操作
- Scalable query performance with larger clusters  (更大的群集可扩展的查询性能)

即席查询
普通查询



## 3 Spark SQL加载数据

- Load data directly into a DataFrame（DataFrame和DataSet提供了强有力的API以及底层更好的优化）
- Load data into an RDD and transform it
- Can load data from local or Cloud



Load Data
1) RDD    DataFrame/Dataset
2) Local   Cloud(HDFS/S3)

**将数据加载成RDD**

```scala
val masterLog = sc.textFile("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/logs/spark-hadoop-org.apache.spark.deploy.master.Master-1-hadoop001.out")
val workerLog = sc.textFile("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/logs/spark-hadoop-org.apache.spark.deploy.worker.Worker-1-hadoop001.out")
val allLog = sc.textFile("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/logs/*out*")
```

```shell
//查询count
asterLog.count
workerLog.count
allLog.count
```



**存在的问题：使用使用SQL进行查询呢？**

```scala
import org.apache.spark.sql.Row
val masterRDD = masterLog.map(x => Row(x))
import org.apache.spark.sql.types._
//定义一个字段line
val schemaString = "line"

val fields = schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, nullable = true))
val schema = StructType(fields)

val masterDF = spark.createDataFrame(masterRDD, schema)
masterDF.show
//通过df的api进行操作

//另外一种方式，创建表进行查询
masterDF.createOrReplaceTempView("master_logs")
spark.sql("select * from master_logs limit 10").show(false)
```



**JSON/Parquet**

```scala
val usersDF = spark.read.format("parquet").load("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet")
scala> usersDF.printSchema
root
 |-- name: string (nullable = true)
 |-- favorite_color: string (nullable = true)
 |-- favorite_numbers: array (nullable = true)
 |    |-- element: integer (containsNull = true)


scala> usersDF.show
+------+--------------+----------------+
|  name|favorite_color|favorite_numbers|
+------+--------------+----------------+
|Alyssa|          null|  [3, 9, 15, 20]|
|   Ben|           red|              []|
+------+--------------+----------------+

scala> usersDF.createOrReplaceTempView("users_info")

scala> spark.sql("select * from users_info where name='Ben'").show
+----+--------------+----------------+
|name|favorite_color|favorite_numbers|
+----+--------------+----------------+
| Ben|           red|              []|
+----+--------------+----------------+



###################################################################################
//另外一种方式
spark.sql("select * from  parquet.`file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet`").show
```



**Drill 大数据处理框架**

从Cloud读取数据: HDFS/S3

```shell
val hdfsRDD = sc.textFile("hdfs://path/file")
val s3RDD = sc.textFile("s3a://bucket/object")
	s3a/s3n
```

spark外部数据源/SparkSQL

```shell
spark.read.format("text").load("hdfs://path/file")
spark.read.format("text").load("s3a://bucket/object")
```



## 4 DataFeame Functions vs SQL

- DataFrame = RDD(数据集) + Schema(表结构：字段的名称、类型)
- DataFrame is just a type alias(别名) for Dataset of Row -- Databricks  (是对DataSet的别名，是Row类型)
- DataFrame over RDD:Catalyst optimization$schemas （地秤使用Catalyst优化，有shemas信息）
- DataFrame can handle : Text、JSON、Parquet and more （能处理外部数据源）
- Both SQL and API Functions in DF still Catalyst optimized (使用SQL和API，底层都使用的是Catalyst进行优化)



## 5 Schema

- inferred (隐式的)
- explicit（显式的）

反射

编程的方式

推导Schema字节的信息



## 6 SaveMode

Loading & Saving Results

- Loding and Saving is fairly straight forward  （简单易懂）
- Save your dataframes in your desired format  （按照需求进行格式保存）



```scala
val df=spark.read.format("json").load("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json")

df.show

df.select("name").write.format("parquet").mode("overwrite").save("file:////home/hadoop/data/overwrite")

//查看
spark.read.format("parquet").load("file:///home/hadoop/data/overwrite").show
```



**指定保存的几种格式**

| Scala/Java                        | Any Language                          | Meaning                                                      |
| --------------------------------- | ------------------------------------- | ------------------------------------------------------------ |
| `SaveMode.ErrorIfExists`(default) | `"error" or "errorifexists"`(default) | When saving a DataFrame to a data source, if data already exists, an exception is expected to be thrown. |
| `SaveMode.Append`                 | `"append"`                            | When saving a DataFrame to a data source, if data/table already exists, contents of the DataFrame are expected to be appended to existing data. |
| `SaveMode.Overwrite`              | `"overwrite"`                         | Overwrite mode means that when saving a DataFrame to a data source, if data/table already exists, existing data is expected to be overwritten by the contents of the DataFrame. |
| `SaveMode.Ignore`                 | `"ignore"`                            | Ignore mode means that when saving a DataFrame to a data source, if data already exists, the save operation is expected to not save the contents of the DataFrame and to not change the existing data. This is similar to a `CREATE TABLE IF NOT EXISTS` in SQL. |



## 7 处理复杂的JSON数据

Working with JSON

- JSON data is most easily read-in as line delimited （JSON数据最容易以行分隔的方式读入）
- Schema is inferred upon load (Schema信息支持自动推导)
- If you want to flatten you JSON data,use the explode method (如果你想把json数据打平，可以使用explode方法)
- Access nested-objects with dot sysntax (Josn对象可以内嵌Json对象，如何访问呢？我们可以使用点的语法操作)



explode()方法使用：

```scala
scala> val json = spark.read.format("json").load("file:///home/hadoop/data/test.json")
scala> json.createOrReplaceTempView("josn_table")

scala> spark.sql("select * from josn_table").show
+--------+----------------+
|    name|            nums|
+--------+----------------+
|zhangsan| [1, 2, 3, 4, 5]|
|    lisi|[6, 7, 8, 9, 10]|
+--------+----------------+


scala> spark.sql("select name,nums[1] from josn_table").show
+--------+-------+
|    name|nums[1]|
+--------+-------+
|zhangsan|      2|
|    lisi|      7|
+--------+-------+


scala> spark.sql("select name,explode(nums) from josn_table").show
+--------+---+
|    name|col|
+--------+---+
|zhangsan|  1|
|zhangsan|  2|
|zhangsan|  3|
|zhangsan|  4|
|zhangsan|  5|
|    lisi|  6|
|    lisi|  7|
|    lisi|  8|
|    lisi|  9|
|    lisi| 10|
+--------+---+
```

内嵌的Json对象：使用`.`的语法

```scala
scala> val json = spark.read.format("json").load("file:///home/hadoop/data/test2.json")
json: org.apache.spark.sql.DataFrame = [address: struct<city: string, state: string>, name: string]

scala> json.show
+-----------------+-------+
|          address|   name|
+-----------------+-------+
|  [Columbus,Ohio]|    Yin|
|[null,California]|Michael|
+-----------------+-------+

scala> json.createOrReplaceTempView("json_table2")

scala> spark.sql("select * from json_tables2").show
+-----------------+-------+
|          address|   name|
+-----------------+-------+
|  [Columbus,Ohio]|    Yin|
|[null,California]|Michael|
+-----------------+-------+

scala> spark.sql("select name,address.city,address.state from json_table2").show
+-------+--------+----------+
|   name|    city|     state|
+-------+--------+----------+
|    Yin|Columbus|      Ohio|
|Michael|    null|California|
+-------+--------+----------+

```



## 8 SQL的覆盖程度

SQL Function Coverage

- SQL 2003support  (SQL标准)
- Runs all 99 of TPC-DS benchmark（基准） queries (能运行很多SQL)
- Subquery supports （支持子查询）
- wectorization (支持向量化：对于查询、聚合、过滤、join等等，性能上有极大的提升，一次可以读多行数据和记录)

TPC-DS



## 9 外部数据源

External Data Sources

- rdbms(关系型数据库),need JDBC jars（需要jdbc驱动）
- Parquet、Phoenix、csv、avro、etc 

网站：spark-packages.org  -- https://spark-packages.org



------

Boy-20180608















