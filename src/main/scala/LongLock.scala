import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator

object LongLock extends App {
  for (i <- 1 to 100) {
    new Thread(() => println(NumAndCurrentDateProvider.next)).start()
  }
}

object NumAndCurrentDateProvider {
  private[this] val lastNumber = new AtomicReference[BigInt](BigInt(0))

  def next: (BigInt, LocalDateTime) =  {
    val nextNumber = lastNumber.updateAndGet(new UnaryOperator[BigInt] {
      override def apply(t: BigInt): BigInt = t + 1
    })
    (nextNumber, currentDateSoHeavy)
  }

  def currentDateSoHeavy: LocalDateTime = {
    Thread.sleep(100)
    LocalDateTime.now()
  }
}
