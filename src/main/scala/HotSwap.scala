import akka.actor.{Actor, ActorSystem, Inbox, Props}

import scala.concurrent.Await
import scala.concurrent.duration._

class HotSwapActor extends Actor {
  import context._
  def angry: Receive = {
    case "foo" => sender() ! "I am already angry?"
    case "bar" => become(happy)
  }

  def happy: Receive = {
    case "bar" => sender() ! "I am already happy :-)"
    case "foo" => become(angry)
  }

  def receive = {
    case "foo" => become(angry)
    case "bar" => become(happy)
  }
}

object HotSwap extends App {
  val system = ActorSystem("hotSwap")
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val hotSwapActor = system.actorOf(Props[HotSwapActor], "hotSwapActor")

  hotSwapActor ! "foo"
  hotSwapActor ! "foo"
  println("foo: " + inbox.receive(5.seconds))
  hotSwapActor ! "bar"
  hotSwapActor ! "bar"
  println("bar: " + inbox.receive(5.seconds))

  Await.ready(system.terminate(), Duration.Inf)
}
