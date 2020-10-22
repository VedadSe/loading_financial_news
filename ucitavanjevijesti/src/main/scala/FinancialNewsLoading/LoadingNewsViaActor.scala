package FinancialNewsLoading

import java.text.SimpleDateFormat
import java.util.Date

import FinancialNewsLoading.LoadingNewsViaActor.LoadNews
import akka.actor.Actor

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

object LoadingNewsViaActor {

  case class LoadNews()
}

class LoadingNewsViaActor(id: Int, symbol: String, title: String, publicationDate: Date, providerUrl: String) extends Actor {
  var result: Int = 0

  override def receive: Receive = {
    case LoadNews() => {
      sender ! result
    }
  }

  def loadFile(fileName: String): Future[Int] = Future {
    val source = Source.fromFile(fileName)
    val newsSequence: Seq[String] = source.getLines().toSeq

    var fileContent: List[String] = List()
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val news: Seq[LoadingNewsViaActor] = newsSequence.takeRight(newsSequence.size - 2).map { seqStr =>
      val parts: Seq[String] = seqStr.split("\\|")

      new LoadingNewsViaActor(parts(0).toInt, parts(1).trim, parts(2).trim, dateFormat.parse(parts(3)), parts(4).trim)
    }

    for (pieceOfNews <- news) {
      fileContent = fileContent :+ (pieceOfNews.id + "\t" + pieceOfNews.symbol + "\t" + pieceOfNews.title + "\t" + pieceOfNews.publicationDate + "\t" + pieceOfNews.providerUrl)
    }
    fileContent.size
  }

}