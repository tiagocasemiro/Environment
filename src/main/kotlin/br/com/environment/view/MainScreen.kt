package br.com.environment.view

import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.model.entity.Variable
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainScreen : View("Derkside") {
    override val root = BorderPane()
    private val controller = EnvironmentControllerGraphic()

    var nameField : TextField by singleAssign()
    var titleField : TextField by singleAssign()
    var personTable : TableView<VariableUi> by singleAssign()


    val persons = toList().observable()

    fun toList(): List<VariableUi> {
        return controller.list().map {
            VariableUi(it.key)
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
                            editPerson(it)
                            prevSelection = it
                        }
                        contextmenu {
                            item("Delete").action {
                                println(selectedItem?.name)
                            }
                        }
                    },
                    form {
                        fieldset("Edit person") {
                            field("Name") {
                                textfield() {
                                    nameField = this
                                }
                            }
                            field("Title") {
                                textfield() {
                                    titleField = this
                                }
                            }
                            button("Save").action {
                                save()
                            }
                        }
                    }
                ) {
                    setDividerPositions(0.35, 0.65)
                }
            }

        }
    }

    private fun editPerson(person: VariableUi?) {
        if (person != null) {
            prevSelection?.apply {
                nameProperty.unbindBidirectional(nameField.textProperty())
            }
            nameField.bind(person.nameProperty)
            prevSelection = person
        }
    }

    private fun save() {
        // Extract the selected person from the tableView
        val person = personTable.selectedItem!!

        // A real application would persist the person here
        println("Saving ${person.name}")
    }
}

class VariableUi(name: String? = null, value: Variable? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty
}
