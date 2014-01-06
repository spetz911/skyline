package models

import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14/12/13
 * Time: 14:47
 */

case class Book (
    title: String,
    creator: String,
    date: Date,
    url: String
)
