package br.com.environment.view.dialog

import br.com.environment.defautPadding
import tornadofx.*

class WarningAlert (message: String, onYes: () -> Unit, onCancel: () -> Unit = {}, width: Double = 400.0, height: Double = 200.0): Fragment("Warning") {
    override val root = vbox {
        setPrefSize(width, height)
        spacer()
        label(message) {
            paddingAll = defautPadding
        }
        spacer()
        hbox {
            paddingAll = defautPadding
            spacer()
            button("Cancel"){
                spacing = defautPadding
            }.action {
                onCancel()
                close()
            }
            button("Yes").action {
                onYes()
                close()
            }
        }
    }
}
