object CheckThenAct extends App {
  for (i <- 1 to 100) {
    new Thread(() => println(SingletonProvider.get)).start()
  }
}

object SingletonProvider {
  private[this] var singleton: BigObject = null

  def get: BigObject = this.synchronized {
    if (singleton == null) {
      singleton = new BigObject()
    }
    singleton
  }
}

class BigObject() {
  Thread.sleep(1000)
}