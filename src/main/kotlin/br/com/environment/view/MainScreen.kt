package br.com.environment.view

import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.model.entity.Variable
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class MainScreen : View("Derkside") {
    override val root = BorderPane()
    private val controller = EnvironmentControllerGraphic()

/*    var nameField : TextField by singleAssign()
    var labelField : Label by singleAssign()
    var titleField : TextField by singleAssign()*/
    var personTable : TableView<VariableUi> by singleAssign()
    class SelectedVariable(val variable: Variable?) : FXEvent()

    private var persons = retrieveVariables().observable()

    private fun retrieveVariables(): List<VariableUi> {
        return controller.list().map {
            VariableUi(it.key, it.value)
        }.toList()
    }


    var prevSelection: VariableUi? = null

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
                    menu("About").action {

                    }

                }
            }

            center {
                splitpane(orientation = Orientation.HORIZONTAL,
                    tableview(persons) {
                        personTable = this
                        column("Variable", VariableUi::nameProperty)
                        selectionModel.selectedItemProperty().onChange {
                            prevSelection = it
                            selectedItem?.value?.let { variable ->
                                fire(SelectedVariable(variable))
                            }
                        }
                        contextmenu {
                            item("Delete").action {
                                selectedItem?.let {
                                    controller.delete(it.value)
                                    persons.setAll(retrieveVariables().observable())
                                }
                            }
                        }
                    },
                    form {


                        subscribe<SelectedVariable> { event->
                            clear()
                            event.variable?.let {
                                fieldset("Edit variable") {
                                    label(it.name.toUpperCase()) {
                                        font = Font.font(20.0)
                                    }
                                    field("Name") {
                                        textfield() {
                                           // nameField = this
                                        }
                                    }
                                    field("Title") {
                                        textfield() {
                                           // titleField = this
                                        }
                                    }
                                    button("Save").action {
                                        save()
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

    private fun save() {
        // Extract the selected person from the tableView
        val person = personTable.selectedItem!!

        // A real application would persist the person here
        println("Saving ${person.name}")
    }
}

class VariableUi(name: String? = null, val value: Variable? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty
}
