package br.com.environment.view

import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.defaultWidth
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import tornadofx.*
import tornadofx.Stylesheet.Companion.imageView
import tornadofx.Stylesheet.Companion.line
import javax.swing.Icon

class PathScreen : View("Path") {
    override val root = BorderPane()
    private val controller = EnvironmentControllerGraphic()

    init {
        with(root) {
            top {
                paddingAll = 20.0
                label(controller.path.value) {
                    font = Font.font(20.0)
                    maxHeight = 200.0
                    textAlignment = TextAlignment.LEFT
                    isWrapText = true
                }
            }
            center {
                vbox {
                    padding = Insets(30.0,0.0,0.0,0.0)
                    flowpane {
                        controller.path.path.forEach {
                            hbox {
                                paddingAll = 10.0
                                hbox {
                                    alignment = Pos.CENTER_LEFT
                                    padding = Insets(1.0, 10.0, 1.0, 10.0)
                                    label(":$it") {
                                        paddingAll = 5.0
                                        font = Font.font(15.0)
                                    }
                                    style {
                                        backgroundColor += Color(0.0, 0.0, 0.0, 0.10)
                                        backgroundRadius = multi(box(50.px))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            bottom {
                hbox {
                    paddingAll = 10.0
                    spacer()
                    button("Back") {
                        action {
                            replaceWith<MainScreen>()
                        }
                    }
                }
            }
        }
    }
}