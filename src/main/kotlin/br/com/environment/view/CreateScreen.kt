package br.com.environment.view

import br.com.environment.controller.EnvironmentControllerGraphic
import javafx.scene.layout.BorderPane
import tornadofx.View

class CreateScreen : View("Create") {
    override val root = BorderPane()
    private val controller = EnvironmentControllerGraphic()

    init {
        with(root) {

        }
    }
}