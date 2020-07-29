object MemoryVisibility extends App {
  @volatile var number = 0
  @volatile var ready = false

  new Thread(() => {
    while (!ready) {
      Thread.`yield`()
    }
    println(number)
  }).start()

  number = 2525
  ready = true
}
