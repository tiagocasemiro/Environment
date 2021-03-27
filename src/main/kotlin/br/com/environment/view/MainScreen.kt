package br.com.environment.view

import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.model.entity.Variable
import br.com.environment.view.dialog.AboutAlert
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import javafx.scene.text.Font
import javafx.stage.StageStyle
import tornadofx.*

class MainScreen : View("Derkside") {
    override val root = BorderPane()
    private val controller = EnvironmentControllerGraphic()

    var variablesTable : TableView<Variable> by singleAssign()
    class SelectedVariable(val variable: Variable?) : FXEvent()
    class CleanForm : FXEvent()
    class AddVariableToPath(val isVisible: Boolean) : FXEvent()

    private var variables = retrieveVariables().observable()

    private fun retrieveVariables(): List<Variable> {
        return controller.list().map {
             it.value
        }.toList()
    }

    private val inputValue = SimpleStringProperty()
    private val inputPathComplemente = SimpleStringProperty()

    var prevSelection: Variable? = null

    init {
        with(root) {
            setPrefSize(1000.0, 600.0)
            setMinSize(600.0, 400.0)
            minHeight = 400.0
            minWidth = 600.0
            top {
                menubar {
                    menu("File") {
                        item("Create").action {
                            replaceWith<CreateScreen>()
                        }
                        item("Open path").action {
                            replaceWith<PathScreen>()
                        }
                        item("Select file").action {

                        }
                    }
                    menu("Help"){
                        item("About").action {
                            AboutAlert().openModal(stageStyle = StageStyle.DECORATED)
                        }
                        item("Shortcuts").action {

                        }
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
                    form {
                        subscribe<CleanForm> {
                            clear()
                        }
                        subscribe<SelectedVariable> { event->
                            fire(CleanForm())
                            event.variable?.let { variable ->
                                inputPathComplemente.value = variable.additionalPath
                                inputValue.value = variable.value
                                fieldset("Edit variable") {
                                    label(variable.name.toUpperCase()) {
                                        font = Font.font(20.0)
                                    }
                                    spacing = 10.0
                                    textarea(inputValue)
                                    checkbox("Add variable to PATH") {
                                        isSelected = variable.isOnPath
                                        subscribe<AddVariableToPath> { event ->
                                            isSelected = event.isVisible
                                        }
                                        action {
                                            variable.isOnPath = isSelected
                                            fire(AddVariableToPath(isSelected))
                                        }
                                    }
                                    vbox {
                                        subscribe<AddVariableToPath> { event ->
                                            isVisible = event.isVisible
                                        }
                                        isVisible = variable.isOnPath
                                        spacing = 20.0
                                        label("Complement of the variable in the path")
                                        textfield(inputPathComplemente)
                                    }
                                    spacing = 20.0

                                    button("Save").action {
                                        variable.value = inputValue.value
                                        variable.additionalPath = inputPathComplemente.value

                                        save(variable)
                                    }
                                }
                            }
                        }
                    }
                ) {
                    setDividerPositions(0.35, 0.65)
                }
            }

        }
    }

    private fun save(variable: Variable) {
        controller.update(variable)
        information("Variable changed with success!")
    }
}
