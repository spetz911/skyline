package controllers

import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.templates.Html
import lib.LangModel

// you need this import to have combinators

import models.LangEntity


import scala.io.Source
import scala.collection.immutable.Map

import com.redis._
import com.mongodb.casbah.Imports._

object Processing extends Controller {

  def showHome = Action {

    val r = new RedisClient("localhost", 6379)
    r.set("tst", "some value")

    val res = r.get("tst").getOrElse("ololo")


    val mongoClient = MongoClient("localhost", 27017)

    val db = mongoClient("test")

    val res2 : String = db.collectionNames.mkString(" ")


    Ok(views.html.home(Array(res, " xxx ", res2)))
  }


  def showDict = Action {
    //    val ss = lib.Counts.text split "\n" map { x => <p>{x}</p>.toString() } mkString "\n"

    val mm = Map("kokos" -> 15,
                 "milk" -> 10)

    Ok(views.html.dict(mm))
  }


  def showText = Action {
    //    val ss = lib.Counts.text split "\n" map { x => <p>{x}</p>.toString() } mkString "\n"

    val res = data.map { xx => if (wordDict.getOrElse(key = xx, default = 9000) > 9000) xx
    else <u>{xx}</u> toString() } mkString " "

    Ok(views.html.text(Html(res)))
  }




  def showIndex = Action {
    //    val ss = lib.Counts.text split "\n" map { x => <p>{x}</p>.toString() } mkString "\n"

    val res = data.map { x => if (wordDict.getOrElse(x, 9000) > 9000) x
    else <u>{x}</u> toString() } mkString " "

    Ok(views.html.text(Html(res)))
  }


  val wordDict = (for ((line,i)  <- Source.fromFile("/Users/oleg/_lang_vocabulary/en_counts.txt").getLines().zipWithIndex ;
                   a :: b :: _ = line.split(' ').toList )
              yield (a, b.toInt)).toMap


  val data = Source.fromFile("/Users/oleg/To_Catch_an_Heiress.txt").getLines().
              flatMap(_ split ' ').
              map(_ filterNot (".,\"\'!()?-" contains _) toString).
              filterNot(_ isEmpty)
              .map(_.toLowerCase).toList


  val contactForm: Form[LangEntity] = Form(
    // Defines a mapping that will handle Contact values
    mapping(
      "word" -> nonEmptyText,
      "count" -> longNumber
    )(LangEntity.apply)(LangEntity.unapply)
  )


  val model = new LangModel().loadFromFile("/Users/oleg/lang.txt")

  def showModel: Action[AnyContent] = Action {
      Ok(views.html.lang("Your new application is ready 11.")(contactForm.fill(LangEntity("oolo", 13))))
    }

  val filledForm = contactForm.fill(LangEntity("Bob", 18))

  /**
   * Handle form submission.
   */
  def submit = Action { implicit request =>
    contactForm.bindFromRequest.fold(
      errors => BadRequest(views.html.lang("OLOLO")(errors)),
      contact => Ok(views.html.lang("OKAY")(contactForm.fill(LangEntity("oolo", 13))))
    )
  }

}




//val taskForm = Form(
//"label" -> nonEmptyText
//)
//
//
//def home = Action {
//Ok(views.html.home("Your new application is ready."))
//}
//
//
//def home = Action {
//Ok(views.html.home("Your new application is ready."))
//}
//
//
//def tasks = Action {
//Ok(views.html.task(Task.all(), taskForm))
//}
//
//def newTask = Action { implicit request =>
//taskForm.bindFromRequest.fold(
//errors => BadRequest(views.html.task(Task.all(), errors)),
//label => {
//Task.create(label)
//Redirect(routes.Application.tasks)
//}
//)
//}
//
//def deleteTask(id: Long) = Action {
//Task.delete(id)
//Redirect(routes.Application.tasks)
//}

















