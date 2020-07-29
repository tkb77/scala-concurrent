import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

import scala.concurrent.Await
import scala.concurrent.duration._

class MyActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "test" => log.info("received test")
    case _ => log.info("received unknown message")
  }
}

object ActorStudy extends App {
  val system = ActorSystem("actorStudy")

  val myActor = system.actorOf(Props[MyActor], "myActor")

  myActor ! "test"

  myActor ! "hoge"

  Thread.currentThread().join()
}