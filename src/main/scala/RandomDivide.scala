import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{Actor, ActorInitializationException, ActorKilledException, ActorSystem, DeathPactException, Inbox, OneForOneStrategy, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Random, Success, Try}

case class DivideRandomMessage(numerator: Int)
case class AnswerMessage(num: Int)
case class ListDivideRandomMessage(numeratorList: Seq[Int])

class RandomDivider extends Actor {
  val random = new Random()
  val denominator = random.nextInt(3) // 0, 1, 2のどれかで割る。 0 で割るアクターは壊れている

  def receive = {
    case m@DivideRandomMessage(numerator) =>
      val answer = Try {
        AnswerMessage(numerator / denominator)
      } match {
        case Success(a) => a
        case Failure(e) =>
          self.forward(m)
          throw e
      }
      println(s" ${numerator} / ${denominator} is ${answer}")
      sender() ! answer
  }

}

class ListRandomDivider extends Actor {
  var listDivideMessageSender = Actor.noSender
  var sum = 0
  var answerCount = 0
  var totalAnswerCount = 0

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, 10.seconds) {
      case _: ArithmeticException => {
        println("Restart by ArithmeticException")
        Restart
      }
      case _: ActorInitializationException => Stop
      case _: ActorKilledException => Stop
      case _: DeathPactException => Stop
      case _: Exception => Restart
    }

  val router = {
    val routees = Vector.fill(4) {
      ActorRefRoutee(context.actorOf(Props[RandomDivider]))
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case ListDivideRandomMessage(numeratorList) => {
      listDivideMessageSender = sender()
      totalAnswerCount = numeratorList.size
      numeratorList.foreach(n => router.route(DivideRandomMessage(n), self))
    }
    case AnswerMessage(num) => {
      sum += num
      answerCount += 1
      if (answerCount == totalAnswerCount) listDivideMessageSender ! sum
    }
  }
}

object RandomDivide extends App {
  val system = ActorSystem("randomDivide")
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val listRandomDivider = system.actorOf(Props[ListRandomDivider], "listRandomDivider")
  listRandomDivider ! ListDivideRandomMessage(Seq(1, 2, 3, 4))
  val result = inbox.receive(10.seconds)
  println(s"Result: ${result}")

  Await.ready(system.terminate(), Duration.Inf)
}
