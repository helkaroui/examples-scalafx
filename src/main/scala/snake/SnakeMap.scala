package dev.sharek.examples.scalafx
package snake

import scalafx.geometry.{Insets, Point2D}
import scalafx.scene.Node
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle, Shape}
import scalafx.scene.text.Text

case class SnakeMap(wHeight: Int, wWidth: Int, state: State) {

  def scoreCounter = new Text {
    x = 50
    y = 50
    text = s"Your score: ${state.score.toString}"
    fill = Color.White
    style = "-fx-font: normal 10pt sans-serif"
  }

  def bestScoreCounter = state.bestScore.map {
    s =>
      new Text {
        x = 50
        y = 70
        text = s"Best score: $s"
        fill = Color.White
        style = "-fx-font: normal 10pt sans-serif"
      }
  }
    .toList

  def rectangle(coordinate: Coordinate, color: Color): Rectangle = new Rectangle {
    x = coordinate._1
    y = coordinate._2
    width = 25
    height = 25
    fill = color
  }

  def renderSnake = state.snake.map(
    rectangle(_, Color.web("#4e7796"))
  )

  def renderFood: Rectangle = rectangle(state.food, Color.web("#df4636"))

  def background: List[Shape] = {
    val rowCount = 600 / 25
    val colCount = 600 / 25

    val lines = (0 to rowCount)
    val cols = (0 to colCount)

    val centers = for {
      l <- lines
      r <- cols
    } yield (25 / 2.0 + 25.0 * l, 25 / 2.0 + 25.0 * r)

    centers.map {
      case (x, y) => new Circle {
        centerX = x
        centerY = y
        radius = 1
        fill = Color.web("#2d4354")
      }
    }
      .toList
  }

  def render: List[Shape] = {
    val screenEntities = renderSnake :+ renderFood
    val screenText = scoreCounter :: bestScoreCounter

    background ++ screenEntities ++ screenText
  }

}
