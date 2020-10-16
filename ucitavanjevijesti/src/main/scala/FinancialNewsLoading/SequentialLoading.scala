package FinancialNewsLoading

import java.text.SimpleDateFormat
import java.util.Date

import scala.io.Source

object SequentialLoading {

  case class FinancialNews(id: Int, symbol: String, title: String, publicationDate: Date, providerUrl: String)

    def loadFile(fileName: String): Int = {
      val source = Source.fromFile(fileName)
      val newsSequence: Seq[String] = source.getLines().toSeq.tail

      var fileContent: List[String] = List()
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

      val news: Seq[FinancialNews] = newsSequence.tail.map { seqStr =>
        val parts: Seq[String] = seqStr.split("\\|")

          FinancialNews(parts(0).toInt, parts(1).trim, parts(2).trim, dateFormat.parse(parts(3)), parts(4).trim)
        }

      for (pieceOfNews <- news) {
        fileContent = fileContent :+ (pieceOfNews.id + "\t" + pieceOfNews.symbol + "\t" + pieceOfNews.title + "\t" + pieceOfNews.publicationDate + "\t" + pieceOfNews.providerUrl)
      }
      fileContent.size
    }

    def main(args: Array[String]): Unit = {
      val starter = System.currentTimeMillis()
      var i = 1
        while(i < 11) {
          println("Loaded news number -> " + loadFile(s"project\\Zadaca2File${i}.txt") + s" (Zadaca2File${i}.txt)")
          i += 1
        }
      println(s"Loaded files number: ${i - 1}. Total time duration: ${(System.currentTimeMillis() - starter)} milliseconds.")
    }

}
