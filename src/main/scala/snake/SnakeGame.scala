package dev.sharek.examples.scalafx
package snake

import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.scene.Scene
import scalafx.scene.input.KeyCode
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.concurrent.Future
import scala.util.Random

/**
 * Snake Game implemented with ScalaFX GUI
 */
object SnakeGame extends JFXApp3 {

  import scala.concurrent.ExecutionContext.Implicits.global

  def gameLoop(update: () => Unit): Unit =
    Future {
      update()
      Thread.sleep(1000 / 25 * 2)
    }.flatMap(_ => Future(gameLoop(update)))
  
  /**
   * Start the main window
   */
  override def start(): Unit = {

    val state = ObjectProperty(State(StateFactory.initialSnake, StateFactory.randomFood(), score = 0))
    val gameMap = SnakeMap(600, 600, state.get())

    val frame = IntegerProperty(0)

    frame.onChange {
      state.update(state.get().newState())
    }

    stage = new JFXApp3.PrimaryStage {
      title = "Snake"
      width = 600
      height = 600
      scene = new Scene {
        fill = Color.web("#253745")
        content = gameMap.render

        onKeyPressed = key => {
          state.update(state.get().newState(key.getCode.getName))
        }

        frame.onChange {
          Platform.runLater {
            content = gameMap.copy(state = state.get()).render
          }
        }

      }
    }

    gameLoop(() => {
      frame.update(frame.get() + 1)
    })
  }

}





