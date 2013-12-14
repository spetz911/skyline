package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
// you need this import to have combinators
import play.api.libs.functional.syntax._


import models.Task

object Application extends Controller {

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


  implicit val rds = {
    (__ \ 'name).read[String] and
      (__ \ 'age).read[Long]
  }.tupled


  def sayHello = Action(parse.json) { request =>
    request.body.validate[(String, Long)].map{
      case (name, age) => Ok("Hello " + name + ", you're "+age + "\n")
    }.recoverTotal{
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e) + "\n")
    }
  }


}
