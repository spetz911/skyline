package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.templates.Html
import lib.LangModel

// you need this import to have combinators
import play.api.libs.functional.syntax._

import models.Task


import scala.io.Source
import scala.collection.immutable.Map

object Processing extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText
  )


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }


  def home = Action {
    Ok(views.html.home("Your new application is ready."))
  }


  def tasks = Action {
    Ok(views.html.task(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.task(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  def showText = Action {
//    val ss = lib.Counts.text split "\n" map { x => <p>{x}</p>.toString() } mkString "\n"

    val res = data.map { x => if (wordDict.getOrElse(x, 9000) > 9000) x else <u>{x}</u> toString() } mkString " "

    Ok(views.html.text("Your new application is ready.") {
                       Html(res)
  })}


  val wordDict = (for ((line,i)  <- Source.fromFile("/Users/oleg/_lang/en_counts.txt").getLines().zipWithIndex ;
                   a :: b :: _ = line.split(' ').toList )
              yield (a, b.toInt)).toMap

  val data = Source.fromFile("/Users/oleg/To_Catch_an_Heiress.txt").getLines().
              flatMap(_ split ' ').
              map(_ filterNot (".,\"\'!()?-" contains _) toString).
              filterNot(_ isEmpty).
              map(_.toLowerCase).toList


  val model = new LangModel().loadFromFile("/Users/oleg/model.txt")

  def showModel: Action[AnyContent] = Action {
      Ok(views.html.model("Your new application is ready 11."))
    }



}




















