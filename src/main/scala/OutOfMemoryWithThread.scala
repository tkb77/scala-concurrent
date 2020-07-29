import java.util.concurrent.atomic.AtomicInteger

object OutOfMemoryWithThread extends App {

  val counter = new AtomicInteger(0)

  while (true) {
    new Thread(() => {
      println(counter.incrementAndGet())
      Thread.sleep(100000)
    }).start()
  }

}
