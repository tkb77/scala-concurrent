import java.util.concurrent.{ForkJoinPool, RecursiveTask}

import scala.annotation.tailrec

object ForkJoinFactorialSumStudy extends App {

  val length = 5000
  val list = (for (i <- 1 to length) yield BigInt(i)).toList

  val pool = new ForkJoinPool()

  class AggregateTask(list: List[BigInt]) extends RecursiveTask[BigInt] {

    override def compute(): BigInt = {
      val n = list.length / 2
      if (n == 0) {
        list match {
          case List() => 0
          case List(n) => factorial(n, BigInt(1))
        }
      } else {
        val (left, right) = list.splitAt(n)
        val leftTask = new AggregateTask(left)
        val rightTask = new AggregateTask(right)
        leftTask.fork()
        rightTask.fork()
        leftTask.join() + rightTask.join()
      }
    }

    @tailrec
    private[this] def factorial(i: BigInt, acc: BigInt): BigInt = if (i == 0) acc else factorial(i - 1, i * acc)

  }

  val start = System.currentTimeMillis()
  val factorialSum = pool.invoke(new AggregateTask(list))
  val time = System.currentTimeMillis() - start

  println(factorialSum)
  println(s"time: ${time} msec")
}