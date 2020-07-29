import scala.collection.mutable.ArrayBuffer

object Escape extends App {
  for (i <- 1 to 100) {
    new Thread(() => {
      println(EscapeVarSeqProvider.next)
      println(EscapeArrayBufferProvider.next)
    }).start()
  }
}

object EscapeVarSeqProvider {
  private[this] var seq: Seq[Int] = Seq() // ESCAPE!
  def next: Seq[Int] = synchronized {
    val nextSeq = seq :+ (seq.size + 1)
    seq = nextSeq
    nextSeq
  }
}

object EscapeArrayBufferProvider {
  private[this] val array: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
  def next: ArrayBuffer[Int] = synchronized {
    array += (array.size + 1)
    array // ESCAPE!
  }
}