object ThreadStudy extends App {
  println(Thread.currentThread().getName)

  val thread1 = new Thread(() => {
    Thread.sleep(1000)
    println(Thread.currentThread().getName)
  })

  val thread2 = new Thread(() => {
    Thread.sleep(500)
    println(Thread.currentThread().getName)
  })


  thread1.start()
  thread2.start()

  println("main thread finished.")
}
