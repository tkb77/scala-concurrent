object Interruption extends App {

  val t = new Thread(() => {
    try {
      while (true) {
        println("Sleeping...")
        Thread.sleep(1000)
      }
    } catch {
      case _: InterruptedException =>
    }
  })

  t.start()
  t.interrupt()

}