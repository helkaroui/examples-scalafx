package dev.sharek.examples.scalafx
package snake

import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.util.Random

type Coordinate = (Double, Double)
type Snake = List[Coordinate]

case class State(snake: Snake, food: Coordinate, score: Int = 0, bestScore: Option[Int] = None, dir: String = "Right") {

  def newState(newDir: String = dir): State = {

    val directedSnake = {
      if (List(("Up", "Down"), ("Down", "Up"), ("Left", "Right"), ("Right", "Left")).contains((newDir, dir)))
        snake.reverse
      else
        snake
    }

    val (x, y) = directedSnake.head

    val (newX, newY) = newDir match {
      case "Up" => (x, y - 25)
      case "Down" => (x, y + 25)
      case "Left" => (x - 25, y)
      case "Right" => (x + 25, y)
      case _ => (x, y)
    }

    val (newSnake, newFood, newScore, newBestScore) = {
      if (newX < 0 || newX >= 600 || newY < 0 || newY >= 600 || directedSnake.drop(2).contains((newX, newY)))
        val s = bestScore match {
          case None if score != 0 => Some(score)
          case Some(s) if s < score => Some(score)
          case _ => bestScore
        }
        (StateFactory.initialSnake, food, 0, s)
      else if (food == (newX, newY))
        (food :: directedSnake, StateFactory.randomFood(), score + 1, bestScore)
      else
        ((newX, newY) :: directedSnake.init, food, score, bestScore)
    }

    State(newSnake, newFood, newScore, newBestScore, newDir)
  }


}


object StateFactory {

  def apply(snake: Snake, food: Coordinate, score: Int = 0, dir: String = "Right"): State =
    State(snake, food, score, None, dir)

  def initialSnake: Snake = List(
    (250.0, 200.0),
    (225.0, 200.0),
    (200.0, 200.0)
  )

  def randomFood(): Coordinate =
    (Random.nextInt(600 / 25 - 1) * 25.0, Random.nextInt(600 / 25 - 1) * 25.0)

}