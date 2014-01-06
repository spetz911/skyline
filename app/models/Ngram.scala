package models

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14/12/13
 * Time: 14:47
 */

case class Ngram(ean: Long,
                 name: String,
                 description: String)

object Ngram {

  var products = Set(
    Ngram(5010255079763L, "Paperclips Large",
      "Large Plain Pack of 1000"),
    Ngram(5018206244666L, "Giant Paperclips",
      "Giant Plain 51mm 100 pack"),
    Ngram(5018306332812L, "Paperclip Giant Plain",
      "Giant Plain Pack of 10000"),
    Ngram(5018306312913L, "No Tear Paper Clip",
      "No Tear Extra Large Pack of 1000"),
    Ngram(5018206244611L, "Zebra Paperclips",
      "Zebra Length 28mm Assorted 150 Pack")
  )

  def findAll = this.products.toList.sortBy(_.ean)

  def findByEan(ean: Long) = this.products.find(_.ean == ean)

  def save(product: Ngram) = {
    findByEan(product.ean).map( oldProduct =>
      this.products = this.products - oldProduct + product
      ).getOrElse(
      throw new IllegalArgumentException("Product not found")
      )
    }
  }
