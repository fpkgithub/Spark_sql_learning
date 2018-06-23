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
  if (s1 != "")
} yield (s1) //generate new collection

val result_try = try {
  Integer.parseInt("boy")
} catch {
  case _ => 0
} finally {
  println("always be printed")
}

val code = 5
val result_match = code match {
  case 1 => "one"
  case 2 => "two"
  case _ => "others"
}

def bar(x: Int, y: => Int): Int = 1

def loop(): Int = loop

//bar(1, loop)

//bar(loop, 1)

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


//尾递归
@annotation.tailrec
def factorial(n: Int, m: Int): Int =
  if (n <= 0) m
  else factorial(n - 1, m * n)
factorial(5, 1)


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

