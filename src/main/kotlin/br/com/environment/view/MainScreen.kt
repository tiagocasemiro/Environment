package br.com.environment.view

import br.com.environment.MainMenu
import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.model.entity.Variable
import br.com.environment.view.dialog.AboutAlert
import br.com.environment.view.dialog.ShortcutsAlert
import javafx.geometry.Orientation
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser.*
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

class MainScreen : View("Derkside") {
    override val root = BorderPane()
    private var controller = EnvironmentControllerGraphic(currentBashFile)
    private var variablesTable : TableView<Variable> by singleAssign()
    private class SelectedVariable(val variable: Variable) : FXEvent()
    class CleanForm : FXEvent()
    private lateinit var tableview: TableView<Variable>
    private var variables = retrieveVariables().observable()
    private fun retrieveVariables(): List<Variable> {
        return controller.list().map {
            it.value.oldAdditionalPath = it.value.additionalPath
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
                                            refreshTableView()
                                            fire(CleanForm())
                                        }
                                    })
                                }
                            }
                        }
                    }.apply {
                        tableview = this
                    },
                    vbox {
                        subscribe<CleanForm> {
                            clear()
                        }
                        subscribe<SelectedVariable> { event->
                            clear()
                            formEdit(event.variable, onUpdate = {
                                updatePath(it)
                                it.oldAdditionalPath = it.additionalPath
                                controller.update(it)
                                information("Variable changed with success!")
                            }, onCreate = {
                                updatePath(it)
                                it.oldAdditionalPath = it.additionalPath
                                controller.create(it)
                                refreshTableView()
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

    private fun updatePath(variable: Variable) {
        val list = mutableListOf<String>()
        list.addAll(controller.path.path)
        val oldAdditionalPath = "\$${variable.name}/${variable.oldAdditionalPath}"
        val newAdditionalPath = "\$${variable.name}/${variable.additionalPath}"

        if(variable.isOnPath) {
            if(variable.oldAdditionalPath.isNullOrEmpty()) {
                list.add(newAdditionalPath)
            } else {
                list.replaceAll {
                    if(it == oldAdditionalPath) {
                        newAdditionalPath
                    } else {
                        it
                    }
                }
            }
        } else {
            if(!variable.oldAdditionalPath.isNullOrEmpty()) {
                list.removeIf {
                    it == oldAdditionalPath
                }
            }
        }
        val newValue = StringBuilder()
        list.forEachIndexed { index, item ->
            if(index == 0) {
                newValue.append(item)
            } else {
                newValue.append(":$item")
            }
        }
        val newPath =  controller.path
        newPath.value =  newValue.toString()

        controller.savePath(newPath)
    }

    private fun selectFile() {
        val files = chooseFile(title = "Select file", filters = arrayOf(ExtensionFilter("Environment variable file", listOf(".*")))) {
            initialDirectory = File(System.getProperty("user.home"))
        }
        if(files.isNotEmpty()) {
            currentBashFile = files.first().absoluteFile.toString()
            controller = EnvironmentControllerGraphic(currentBashFile)
            refreshTableView()
        }
    }

    private fun refreshTableView() {
        variables.clear()
        variables.addAll(retrieveVariables().observable())
        tableview.refresh()
    }

    private fun shortcuts() {
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
