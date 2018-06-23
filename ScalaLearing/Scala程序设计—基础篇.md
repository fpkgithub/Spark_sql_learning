# Scala程序设计—基础篇




[TOC]







地址：[Scala程序设计—基础篇](https://www.imooc.com/learn/613)

## 1章 函数式编程思想

本章讲述了函数式编程的概念、术语

### 1-1 课程介绍

**Scala特性：**

Scalable编程语言

纯正的面向对象语言

函数式语言

无缝的Java互操作

![Scala特性](https://upload-images.jianshu.io/upload_images/5959612-5bf9cd895111f562.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### 1-2 scala函数式编程思想

**什么是函数式编程？**

在计算机科学领域，函数式编程是一种编程范式，它是一种构建计算机程序结构的方法和风格，它把程序当做数学函数的求值过程并且避免了改变状态和可变的数据。 

**函数式编程的重要概念**

纯函数：不具有副作用，即不改变函数作用域之外变量的值。 

副作用：是状态的变化（mutation） 例子：修改全局变量、抛出异常、IO读写、t调用有副作用的函数



引用透明（Referential Transparency）

对于相同的输入，总是得到相同的输出

如果f(x)的参数x和函数体都是引用透明的，那么函数f是纯函数



不变性（Immutability）为了获得引用透明性，任何值都不能变化



函数是一等公民（First-class Function）

> 一切都是计算，函数式编程中只有表达式，变量，函数都是表达式

高阶函数（Higher order Function）

闭包（Closure）



表达式求值策略：严格求值 和 非严格求值

Call By Value vs Call By Name

惰性求值（Lazy Evaluation）



递归函数（Recursive Function）

递归实现循环

尾递归（Tail Recursion）



函数式编程的关键词

![函数式编程的关键词](https://upload-images.jianshu.io/upload_images/5959612-f784e68cd79c2e07.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



函数值编程的优点：

生产效率高、同样功能的程序，Lisp代码的长度可能是C代码的1/7~~~1/10

易于推理（Reasoning）

并行编程，多核计算、云计算

http://www.slideshare.net/Ordersky/oscon-keynote-working-hard-to-keep-it-simple 



## 2章 Scala开发环境

本章介绍了Scala的开发环境-REPL和IDE

### 2-1 scala环境搭建

按照Jdk：

下载安装Scala：https://www.scala-lang.org/download/



下载安装SBT（Simple Build Tool ）：https://www.scala-sbt.org/download.html?_ga=2.212214996.1146994929.1526990837-1074418497.1526990748

> SBT是SCALA 平台上标准的项目构建工具，当然你要用它来构建其他语言的项目也是可以的。SBT 没有Maven那么多概念和条条框框，但又比IVY要更加灵活，可以认为是一个精简版的Maven吧。 

REPL：Read Evaluate Print Loop 交互式程序运行环境，非常轻量级，用于做实验的   （可以理解为shell交互环境）

启动REPL：

1 sbt console

2 scala







## 3章 Scala的语言基础

本章讲解了Scala的变量和表达式

### 3-1 scala基础语法之变量

val 常量  var 变量  lazy val 惰性求值常量：该变量可能不会被用到 ，只有当第一次使用时 使用 lazy val 

![三种变量修饰符](https://upload-images.jianshu.io/upload_images/5959612-bf0b167056b15038.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)





### 3-2 Scala数据类型

Scala类型体系：

**根类：**Any

**子类：**

AnyVal：值类型  

AnyRef：引用类型的父类

![Scala类型体系](https://upload-images.jianshu.io/upload_images/5959612-f0091362883c1578.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



和java一样，低精度可以向高精度自动转化，高精度不可以想低精度转换

函数返回nothing表明程序异常，

**String**

![String](https://upload-images.jianshu.io/upload_images/5959612-568358c3206b585d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 3-3 Scala函数与代码块

代码块

![代码块](https://upload-images.jianshu.io/upload_images/5959612-9476c769c584669e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

函数：

![函数](https://upload-images.jianshu.io/upload_images/5959612-f05ec80a4bc19902.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```scala
def hello(name: String): String = {
  s"Hello,${name}"
}
hello("boy")

def hello2(name:String)={
  s"Hello,${name}"
}
hello2("123")

def add(x:Int,y:Int) ={x+y
}
def add2(x:Int,y:Int) =x+y
add(1,7)
```



### 3-4 Scala基础 if与for

if是表达式，不是语句

![if是表达式](https://upload-images.jianshu.io/upload_images/5959612-4983eed314736389.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

for comprehension 循环

![for循环](https://upload-images.jianshu.io/upload_images/5959612-b68ea1c9220fa43b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```scala
val list = List("alice", "bob", "cathy");

for (
  s <- list //generator
) println(s)


for (
  s <- list
  if (s.length > 3) //filter
) println(s)

val result_for = for {
  s <- list
  s1 = s.toUpperCase() //variable binding
  if(s1 != "")
} yield(s1)  //generate new collection
```



### 3-5 Scala基础 try与match表达式

**try表达式**

![try表达式](https://upload-images.jianshu.io/upload_images/5959612-db26f041e2ebbeb0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`_`：通配符

```scala
val result_try = try {
  Integer.parseInt("boy")
} catch {
  case _ => 0
} finally {
  println("always be printed")
}

输出：
always be printed
result_try: Int = 0
```

**match表达式**

![match表达式](https://upload-images.jianshu.io/upload_images/5959612-a4cf289de47180e8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```scala
val code = 5
val result_match = code match {
  case 1 => "one"
  case 2 => "two"
  case _ => "others"
}

输出：
code: Int = 5
result_match: String = others
```

## 4章 求值策略

### 4-1 求值策略

本章讲解了Call By Value和Call By Name的区别

**求值策略（Evaluation Strategy）有两种：**

> Call By Value - 对函数实参求值，且仅求值一次
>
> Call By Name - 函数实参每次在函数体内被用到时都会求值

Scala通常使用Call By Value

如果函数形参类型以 => 开头，那么会使用Call By Name

```scala
def foo(x: Int) = x  //call by  value

def foo(x: => Int ) = x //call By Name
```

**求值策略的例子**

```scala
def test1(x: Int, y: Int): Int = x*x
def test2(x: => Int, y: => Int): Int = x*x
```

![image.png](https://upload-images.jianshu.io/upload_images/5959612-dbb8b613cc8733a8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```scala
def bar(x: Int, y: => Int): Int = 1

def loop(): Int = loop

bar(1, loop)     //输出：res2: Int = 1

bar(loop, 1)     //死循环
```



## 5章 高阶函数

本章讲解了Scala高阶函数的概念和语法

### 5-1 Scala 函数与匿名函数

**函数是一等公民**

![函数是一等公民](https://upload-images.jianshu.io/upload_images/5959612-11da6858dcc3ff0d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**函数类型**

在Scala语言中，函数类型的格式为A => B，表示一个接受类型A的参数，并返回类型B的函数

例子：Int => String 是把整型映射为字符串的函数类型

**高阶函数**

用函数作为形参或者返回值的函数，称为高阶函数

```scala
//函数operate，f是一个函数，接受两个int的参数并且返回一个int的值，在函数operate体内调用f函数  
def operate(f: (Int, Int) => Int) ={
    f(4,4)
}
//返回值是一个函数
def greeting() = (name: String) => {"hello" + " " + name}
```



**匿名函数**

匿名函数（Anonymous Function）,就是函数常量，也称为函数文字量（Function Literal）

在Scala里，匿名函数的定义格式为： (形参列表) => {函数体}

匿名函数的例子：

```scala
例子1: (x: Int) => x*x
例子2: (x: Int,y: Int) => x+y
例子3:
var add = (x: Int, y: Int) => x + y //add是一个具有函数类型的变量
add(1, 2) //返回值：Int=3

def greeting() = (name: String) => {
  s"Hello $name"
}
greeting()("World")

def greeting(age: Int) = (name: String) => {
  s"Hello $name,your age is $age"
}
greeting(23)("Flygar")
```



### 5-2 Scala 柯里化

柯里化函数（Curried Function）把具有多个参数的函数转换为一条函数链，每个节点上是单一参数。

例子：

以下两个add函数定义是等价的

```scala
def add(x: Int, y: Int) = x + y
def add(x: Int)(y: Int) = x + y  //Scala里柯里化的语法
```

柯里化的例子

```scala
def curriedAdd(a: Int)(b: Int) = a + b
curriedAdd(2)(2)  //4

val addOne = curriedAdd(1)_  //Int => Int  下换线匹配的是b
addOne(2)    //3
```



### 5-3 Scala 递归与尾递归

递归函数（Recursive Function）在函数式编程中是实现循环的一种技术。

例子：计算你n!

```scala
def factorial(n: Int): Int = 
	if(n <= 0) 1
	else n*factorial(n - 1)
```

为了防止递归时栈溢出，进行优化使用尾递归函数

**尾递归函数**

尾递归函数（Tail Recursive Function）中所有递归形式的调用都出现在函数的末尾。

当编译器检测到一个函数调用是尾递归的时候，它就覆盖当前的活动记录而不是在栈中去创建一个新的

尾递归的例子

```scala
//5!
@annotation.tailrec
def factorial(n: Int, m: Int): Int = 
	if(n <= 0) m
    else factorial(n-1, m*n)
factorial(5,1)
```



### 5-4 例子

综合性例子：求$\sum_{i=a}^b$ f(x)

![求$\sum_{i=a}^b$ f(x)](https://upload-images.jianshu.io/upload_images/5959612-250b8700d781be22.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```scala
//三个参数：f a b
def sum(f: Int => Int)(a: Int)(b: Int): Int = {
  //a到b的循环，对f(x)求解，然后累加f(x)
  @annotation.tailrec
  def loop(n: Int, acc: Int): Int = {
    if (n > b) {
      println(s"n=${n}, acc=${acc}")
      acc
    } else {
      println(s"n=${n}, acc=${acc}")
      loop(n + 1, acc + f(n))
    }
  }

  loop(a, 0)
}

sum(x => x)(1)(5)

sum(x => x * x)(1)(5)

sum(x => x * x * x)(1)(5)

val sumSquare = sum(x => x * x)_
sumSquare(1)(5)
```





## 6章 Scala Immutable Collection

本章讲解了Scala Immutable Collection的List、Range、Stream和Map

### 6-1 Scala Collections-list基本使用

scala.collection.immutable

![scala.collection.immutable](https://upload-images.jianshu.io/upload_images/5959612-2f8c6a241388edc1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

List[T]

```scala
scala> val a = List(1,2,3,4)
a: List[Int] = List(1, 2, 3, 4)

//::  连接操作符 左边是成员 右边是list
scala> val b =0 :: a
b: List[Int] = List(0, 1, 2, 3, 4)

scala> val c = "x" :: "y" :: "z" :: Nil
c: List[String] = List(x, y, z)

scala> "z" :: Nil
res0: List[String] = List(z)

scala> "y" :: res0
res1: List[String] = List(y, z)

scala> "x" :: res1
res2: List[String] = List(x, y, z)

scala> val d = a ::: c
d: List[Any] = List(1, 2, 3, 4, x, y, z)

//a.head返回list第一个元素 a.tail返回除了第一个元素外的其他元素（尾列表） a.isEmpty判断是否为空
```



### 6-2 Scala list高级使用

**a.filter过滤函数**

```scala
//a.filter过滤函数
scala> val a = List(1,2,3,4)
scala> a.filter(x => x%2 ==1 )
res10: List[Int] = List(1, 3)
```

**"".toList.filter**

```scala
scala>  "99 Red Balloons".toList.filter(x => Character.isDigit(x))
res11: List[Char] = List(9, 9)
```

**"".toList.takeWhile**

```scala
scala> "99 Red Ballons".toList.takeWhile(x => x != 'B')
res12: List[Char] = List(9, 9,  , R, e, d,  )
```



### 6-3 Scala list-map

map映射：用来将一个列表中的每个元素进行映射的函数

```scala
//转乱成大写
scala> c.map(x => x.toUpperCase)
res13: List[String] = List(X, Y, Z)

//通配符
scala> c.map(_.toUpperCase)
res14: List[String] = List(X, Y, Z)

//通配符
scala> c.map(_.toUpperCase)
res17: List[String] = List(X, Y, Z)

//通配符
scala> a.filter(_%2 == 1)
res18: List[Int] = List(1, 3)

//通配符过滤后每个元素加10
scala> a.filter(_%2 == 1).map(_ + 10)
res19: List[Int] = List(11, 13)

//取q中的偶数
scala> val q = List(a, List(4,5,6))
q: List[List[Int]] = List(List(1, 2, 3, 4), List(4, 5, 6))

scala> q.map(x => x.filter( _%2 == 0))
res20: List[List[Int]] = List(List(2, 4), List(4, 6))

scala>  q.map(_.filter(_%2 == 0))
res21: List[List[Int]] = List(List(2, 4), List(4, 6))

//flatMap
scala>  q.flatMap(_.filter(_%2 == 0))
res23: List[Int] = List(2, 4, 4, 6)
```





### 6-4 Scala reduceLeft与flodLeft

**reduceLeft 规约操作**

![reduceLeft 规约操作](https://upload-images.jianshu.io/upload_images/5959612-9816b554ae820bec.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```scala
scala> a
res25: List[Int] = List(1, 2, 3, 4)

scala> a.reduceLeft((x, y) => x+y)
res26: Int = 10

scala> a.reduce( _ + _)
res27: Int = 10
```

**foldLeft规约操作**

![foldLeft规约操作](https://upload-images.jianshu.io/upload_images/5959612-56e26279c31226a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



```scala
scala> a.foldLeft(0)( _+_ )
res28: Int = 10

scala> a.foldLeft(1)( _*_ )
res29: Int = 24
```



### 6-5 Scala Rang与Stream

**Rang**

```scala
//to是闭区间 until是开区间
scala> 1 to 10
res34: scala.collection.immutable.Range.Inclusive = Range 1 to 10

scala> 1 to 10 by 2
res35: scala.collection.immutable.Range = inexact Range 1 to 10 by 2

scala> (1 to 10).toList
res36: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

scala> 1 until 10
res37: scala.collection.immutable.Range = Range 1 until 10

scala> (1 until 10).toList
res38: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
```

**Stream**

惰性求值列表，只求第一个值

```scala
scala> 1 #:: 2 #:: 3 #:: Stream.empty
res39: scala.collection.immutable.Stream[Int] = Stream(1, ?)

scala> val stream = (1 to 1000000).toStream
stream: scala.collection.immutable.Stream[Int] = Stream(1, ?)

scala> stream.head
res40: Int = 1

scala> stream.tail
res41: scala.collection.immutable.Stream[Int] = Stream(2, ?)
```



### 6-6 Scala tuple与map

**tuple**：元组，类似于数据库中的记录

```scala
//如果有两个值则称为对pair
scala> (1,2)
res44: (Int, Int) = (1,2)
//简写
scala> 1 -> 2
res45: (Int, Int) = (1,2)
//类似于数据库中的记录
scala> (1, "Alice", "Math", 95.5)
res46: (Int, String, String, Double) = (1,Alice,Math,95.5)

scala> val t = (1, "Alice", "Math", 95.5)
t: (Int, String, String, Double) = (1,Alice,Math,95.5)
//取值
scala> t._1
res47: Int = 1

scala> t._2
res48: String = Alice

scala> t._3
res49: String = Math

scala> t._4
res50: Double = 95.5

//求list的长度，元素之和，元素平方之和
scala> a
res52: List[Int] = List(1, 2, 3, 4)
//输入list  输出：tuple(Int,Int,Int)  foldLeft规约操作，t是元组(0,0,0,0),list中的每个值作为输出参数v 然后返回一个tuple 
scala> def sumSq(in : List[Int]):(Int,Int,Int) =
     | in.foldLeft((0,0,0))((t,v) => (t._1+1, t._2+v, t._3+v*v))
sumSq: (in: List[Int])(Int, Int, Int)

scala> sumSq(a)
res51: (Int, Int, Int) = (4,10,30)

```

**Map[K,V]**

```scala
//新建
scala> val p = Map(1 -> "David", 9 -> "Elwood")
p: scala.collection.immutable.Map[Int,String] = Map(1 -> David, 9 -> Elwood)
//取值
scala> p(1)
res55: String = David

scala> p(9)
res56: String = Elwood

scala> p.contains(1)
res57: Boolean = true

scala> p.contains(2)
res58: Boolean = false
//取key
scala> p.keys
res59: Iterable[Int] = Set(1, 9)
//取values
scala> p.values
res60: Iterable[String] = MapLike.DefaultValuesIterable(David, Elwood)
//添加键值对
scala> p + (8 -> "Archer")
res61: scala.collection.immutable.Map[Int,String] = Map(1 -> David, 9 -> Elwood, 8 -> Archer)
//删除键值对
scala> p - 1
res62: scala.collection.immutable.Map[Int,String] = Map(9 -> Elwood)
//添加多个值到map
scala> p ++ List(2 -> "Alice", 5 -> "Bob")
res69: scala.collection.immutable.Map[Int,String] = Map(1 -> David, 9 -> Elwood, 2 -> Alice, 5 -> Bob)
//删除多个值
scala> p -- List(1,9,2)
res70: scala.collection.immutable.Map[Int,String] = Map()
//同时操作
scala> p ++ List(2 -> "Alice", 5 -> "Bob") -- List(1,9,2)
res72: scala.collection.immutable.Map[Int,String] = Map(5 -> Bob)
```

### 6-7 Scala 快速排序案例

![Scala 快速排序案例](https://upload-images.jianshu.io/upload_images/5959612-230c41da29527901.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

启动`:paste`大规模编辑模式

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)

def qSort(a: List[Int]): List[Int] =
if(a.length < 2) a
else
qSort(a.filter(_<a.head)) ++ a.filter( _==a.head) ++ qSort(a.filter( _>a.head))

// Exiting paste mode, now interpreting.

qSort: (a: List[Int])List[Int]

scala> qSort(List(3,1,2))
res73: List[Int] = List(1, 2, 3)

scala> qSort(List(9,5,6,3,5,7,8,23))
res74: List[Int] = List(3, 5, 5, 6, 7, 8, 9, 23)
```





