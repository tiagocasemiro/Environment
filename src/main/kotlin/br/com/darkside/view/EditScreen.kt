package br.com.darkside.view

import br.com.darkside.entity.Variable
import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.Font
import tornadofx.*

private class AddVariableToPath(val isVisible: Boolean) : FXEvent()

fun View.formEdit(variable: Variable, onUpdate: (Variable) -> Unit = {}, onCreate: (Variable) -> Unit = {},): Form {
    val inputValue = SimpleStringProperty()
    val inputPathComplemente = SimpleStringProperty()
    val inputName = SimpleStringProperty()
    val isCreate = variable.name.isNullOrEmpty()
    val titleForm = if (isCreate) "New variable" else "Edit variable"

    return form {
        inputPathComplemente.value = variable.additionalPath
        inputValue.value = variable.value
        fieldset(titleForm) {
            if(variable.name.isNullOrEmpty()) {
                textfield(inputName)
            } else {
                label(variable.name.toUpperCase()) {
                    font = Font.font(20.0)
                }
            }
            spacing = 10.0
            textarea(inputValue)
            checkbox("Add variable to PATH") {
                isSelected = variable.isOnPath
                action {
                    variable.isOnPath = isSelected
                    if(!isSelected) {
                        inputPathComplemente.value = ""
                        variable.additionalPath = null
                    }
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
            spacer()
            button("Save").action {
                variable.value = inputValue.value
                if(variable.isOnPath) {
                    variable.additionalPath = inputPathComplemente.value
                } else {
                    variable.additionalPath = null
                }
                if(isCreate) {
                    variable.name = inputName.value
                    onCreate(variable)
                } else {
                    onUpdate(variable)
                }
            }
        }
    }
}
