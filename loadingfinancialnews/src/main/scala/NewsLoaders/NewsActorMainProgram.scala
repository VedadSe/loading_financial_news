package NewsLoaders

import NewsLoaders.NewsActor.LoadNews
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object NewsActorMainProgram {

  def main(args: Array[String]): Unit = {
    val as = ActorSystem("news-loader")
    implicit val timeout = new Timeout(8.seconds)

    val news = as.actorOf(Props(new NewsActor()))

    val response: Future[Seq[Int]] = (news ? LoadNews()).map(_.asInstanceOf[Seq[Int]])

    val starter = System.currentTimeMillis()

    response.onComplete {
      case Success(r) => println(s"Loaded files number: ${r.size}. Loaded news number: ${r.sum}. Total time duration: ${System.currentTimeMillis() - starter} milliseconds.")
      case Failure(ex) => println(s"Greska: ${ex.getMessage}")
    }
  }

}
