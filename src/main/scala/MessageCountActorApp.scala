import akka.actor.{Actor, ActorSystem, Props}

class MessageCount extends Actor {
  var count = 0
  def receive = {
    case _ => {
      count = count + 1
      println(count)
    }
  }
}

object MessageCountActorApp extends App {
  val system = ActorSystem("actorStudy")

  val myActor = system.actorOf(Props[MessageCount], "MessageCount")

  for(i <- 1 to 10000) myActor ! "test"

  Thread.currentThread().join()
}