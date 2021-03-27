package br.com.environment

import br.com.environment.view.MainScreen
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*

class AppView: App(MainScreen::class)

fun main(args: Array<String>) {
    launch<AppView>(args)
}

const val defaultPadding = 10.0


class MainView2() : View("Derkside") {
    override val root = BorderPane()
    init {
        with(root) {
            center {

                setPrefSize(1000.0, 600.0)
                button("click").action {

                    confirmation("Attetion", "this variable will are deleted!", actionFn = {

                    })

                /*    WarningAlert(, {
                       // replaceWith<MainView>()
                      //  replaceWith(MainView::class, ViewTransition.Fade(0.8.seconds))


                        val dir = chooseDirectory("Select Target Directory")
                        println(dir?.absoluteFile)

                        val file = chooseFile(title = "Select file", filters = arrayOf(FileChooser.ExtensionFilter("Images", listOf("*.svg", "*.png"))))
                        file.forEach {
                            println(it.absoluteFile)
                        }

                    }).openModal(stageStyle = StageStyle.DECORATED)*/
                }
            }
        }
    }
}


