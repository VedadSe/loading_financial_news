package FinancialNewsLoading

import java.util.Date

class NewsColumns(id: Int, symbol: String, title: String, publicationDate: Date, providerUrl: String) {

  def getId = id
  def getSymbol = symbol
  def getTitle = title
  def getPublicationDate = publicationDate
  def getProviderUrl = providerUrl

}
