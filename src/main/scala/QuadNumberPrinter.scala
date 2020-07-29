object QuadNumberPrinter extends App {
  private var counter = 0

  for (i <- 1 to 4) {
    new Thread(() => for(j <- 1 to 100000)  println(Thread.currentThread().getName + ": " + j)).start()
  }
}
