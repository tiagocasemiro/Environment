package br.com.environment

import br.com.environment.view.MainView
import br.com.environment.view.dialog.WarningAlert
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle
import tornadofx.*

class AppView: App(MainView2::class)

fun main(args: Array<String>) {
    launch<AppView>(args)
}

val defautPadding = 10.0


class MainView2() : View("Derkside") {
    override val root = BorderPane()
    init {
        with(root) {
            center {
                setPrefSize(1000.0, 600.0)
                button("click").action {
                    WarningAlert("Attetion, this variable will are deleted", {
                       // replaceWith<MainView>()
                      //  replaceWith(MainView::class, ViewTransition.Fade(0.8.seconds))

                    }).openModal(stageStyle = StageStyle.DECORATED)
                }
            }
        }
    }
}

