import br.com.environment.controller.EnvironmentControllerGraphic
import br.com.environment.model.entity.Variable
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.*


class VariableUi(name: String? = null, value: Variable? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty
}

class Main : View("Derkside") {
    val controller = EnvironmentControllerGraphic()

    override val root = BorderPane()
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
            left {
                listmenu(theme = "blue") {
                    item(graphic = imageview("config.png")) {
                        whenSelected {
                            println("config")
                        }
                    }
                    item(graphic = imageview("config.png")) {
                        whenSelected {
                            val dir = chooseDirectory("Select Target Directory")
                            println(dir?.absoluteFile)
                        }
                    }
                    item(graphic = imageview("config.png")) {
                        whenSelected {
                            val file = chooseFile(title = "Select file", filters = arrayOf(FileChooser.ExtensionFilter("Images", listOf("*.svg", "*.png"))))
                            file.forEach {
                                println(it.absoluteFile)
                            }
                        }
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
                            item("Change").action {
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

class MyApp: App(Main::class)

fun main(args: Array<String>) {
    launch<MyApp>(args)
}


fun image() = "<svg height=\"512pt\" viewBox=\"0 0 512 512\" width=\"512pt\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><linearGradient id=\"a\" gradientUnits=\"userSpaceOnUse\" x1=\"0\" x2=\"512\" y1=\"256\" y2=\"256\"><stop offset=\"0\" stop-color=\"#00f38d\"/><stop offset=\"1\" stop-color=\"#009eff\"/></linearGradient><path d=\"m512 256c0 141.386719-114.613281 256-256 256s-256-114.613281-256-256 114.613281-256 256-256 256 114.613281 256 256zm0 0\" fill=\"url(#a)\"/><g fill=\"#fff\"><path d=\"m417.398438 217.941406h-26.800782c3.453125 12.148438 5.304688 24.96875 5.304688 38.210938 0 34.40625-12.492188 65.941406-33.167969 90.335937-7.8125-32.570312-30.183594-59.539062-59.824219-73.609375 12.03125-12.023437 19.484375-28.628906 19.484375-46.941406 0-36.609375-29.785156-66.390625-66.394531-66.390625s-66.394531 29.78125-66.394531 66.390625c0 18.3125 7.453125 34.917969 19.484375 46.941406-29.640625 14.070313-52.011719 41.035156-59.824219 73.609375-20.675781-24.390625-33.167969-55.929687-33.167969-90.335937 0-77.140625 62.761719-139.898438 139.902344-139.898438 13.480469 0 26.515625 1.921875 38.859375 5.496094v-26.839844c-12.578125-3.007812-25.597656-4.566406-38.859375-4.566406-44.289062 0-85.929688 17.25-117.246094 48.566406s-48.5625 72.953125-48.5625 117.242188c0 44.289062 17.246094 85.925781 48.5625 117.246094 31.316406 31.316406 72.957032 48.5625 117.246094 48.5625s85.925781-17.246094 117.242188-48.5625c31.320312-31.320313 48.566406-72.957032 48.566406-117.246094 0-13.035156-1.503906-25.832032-4.410156-38.210938zm-197.792969 7.996094c0-20.066406 16.328125-36.394531 36.394531-36.394531s36.394531 16.328125 36.394531 36.394531c0 20.070312-16.328125 36.394531-36.394531 36.394531s-36.394531-16.324219-36.394531-36.394531zm-43.335938 145.105469c.566407-43.496094 36.101563-78.710938 79.730469-78.710938 43.625 0 79.164062 35.214844 79.730469 78.710938-22.636719 15.757812-50.121094 25.011719-79.730469 25.011719s-57.09375-9.253907-79.730469-25.011719zm0 0\"/><path d=\"m330.246094 195.09375 34.757812-17.910156 34.757813 17.910156-6.644531-37.980469 28.429687-27.164062-39.261719-5.59375-17.28125-34.316407-17.277344 34.320313-39.261718 5.589844 28.429687 27.164062zm0 0\"/></g></svg>"
