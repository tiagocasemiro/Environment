package br.com.environment.view

import br.com.environment.MainMenu
import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.currentBashFile
import br.com.environment.defaultHeight
import br.com.environment.defaultWidth
import br.com.environment.model.entity.Variable
import br.com.environment.view.dialog.AboutAlert
import br.com.environment.view.dialog.ShortcutsAlert
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.MenuBar
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.FileChooser.*
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

class MainScreen : View("Derkside") {
    override val root = BorderPane()
    private var controller = EnvironmentControllerGraphic(currentBashFile)
    var variablesTable : TableView<Variable> by singleAssign()
    class SelectedVariable(val variable: Variable) : FXEvent()
    class CleanForm : FXEvent()

    private var variables = retrieveVariables().observable()
    private fun retrieveVariables(): List<Variable> {
        return controller.list().map {
             it.value
        }.toList()
    }
    var prevSelection: Variable? = null

    init {
        shortcuts()
        with(root) {
            setPrefSize(defaultWidth, defaultHeight)
            top {
                MainMenu().attachTo(this) {
                    onOpenPathMenu = {
                        replaceWith<PathScreen>()
                    }
                    onCreateMenu = {
                        fire(SelectedVariable(Variable()))
                    }
                    onSelectFileMenu = {
                       selectFile()
                    }
                    onAboutMenu = {
                        AboutAlert().openModal(stageStyle = StageStyle.DECORATED)
                    }
                    onShortcutsMenu = {
                        ShortcutsAlert().openModal(stageStyle = StageStyle.DECORATED)
                    }
                }
            }
            center {
                splitpane(orientation = Orientation.HORIZONTAL,
                    tableview(variables) {
                        variablesTable = this
                        column("Variable", Variable::getName)
                        selectionModel.selectedItemProperty().onChange {
                            prevSelection = it
                            selectedItem?.let { variable ->
                                fire(SelectedVariable(variable))
                            }
                        }
                        contextmenu {
                            item("Delete").action {
                                selectedItem?.let { variable ->
                                    confirmation("Attetion", "this variable will are deleted!", actionFn = {
                                        if(it.buttonData.isDefaultButton) {
                                            controller.delete(variable)
                                            variables.setAll(retrieveVariables().observable())
                                            fire(CleanForm())
                                        }
                                    })
                                }
                            }
                        }
                    },
                    vbox {
                        subscribe<CleanForm> {
                            clear()
                        }
                        subscribe<SelectedVariable> { event->
                            clear()
                            formEdit(event.variable, onUpdate = {
                                controller.update(it)
                                information("Variable changed with success!")
                            }, onCreate = {
                                controller.create(it)
                                variables.setAll(retrieveVariables().observable())
                                information("Variable created with success!")
                            }).attachTo(this@vbox)
                        }
                    }
                ) {
                    setDividerPositions(0.35, 0.65)
                }
            }
        }
    }

    fun selectFile() {
        val files = chooseFile(title = "Select file", filters = arrayOf(ExtensionFilter("Environment variable file", listOf(".*")))) {
            initialDirectory = File(System.getProperty("user.home"))
        }
        if(files.isNotEmpty()) {
            currentBashFile = files.first().absoluteFile.toString()
            controller = EnvironmentControllerGraphic(currentBashFile)
            variables.setAll(retrieveVariables().observable())
        }
    }

    fun shortcuts() {
        shortcut("Ctrl+N") {
            fire(SelectedVariable(Variable()))
        }
        shortcut("Ctrl+O") {
            replaceWith<PathScreen>()
        }
        shortcut("Ctrl+B") {
            selectFile()
        }
        shortcut("Ctrl+A") {
            AboutAlert().openModal(stageStyle = StageStyle.DECORATED)
        }
    }
}
