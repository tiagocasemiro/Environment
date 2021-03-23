import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.model.entity.Variable


class VariableUi(name: String? = null, value: Variable? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty
}

class PersonEditor : View("Person Editor") {
    val controller = EnvironmentControllerGraphic()

    override val root = BorderPane()
    var nameField : TextField by singleAssign()
    var titleField : TextField by singleAssign()
    var personTable : TableView<VariableUi> by singleAssign()
    // Some fake data for our table

    val persons = toList().observable()

    fun toList(): List<VariableUi> {
        return controller.list().map {
            VariableUi(it.key)
        }.toList()
    }


    var prevSelection: VariableUi? = null

    init {
        with(root) {
            splitpane {
                add(pane {
                    tableview(persons) {
                        personTable = this
                        column("Variable", VariableUi::nameProperty)
                        selectionModel.selectedItemProperty().onChange {
                            editPerson(it)
                            prevSelection = it
                        }
                    }
                })
                add(pane {
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
                })
                setDividerPositions(0.6, 0.4)
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



class MyApp: App(PersonEditor::class)


fun main(args: Array<String>) {
    launch<MyApp>(args)
}



