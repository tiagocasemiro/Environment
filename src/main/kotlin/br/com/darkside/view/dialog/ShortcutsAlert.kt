package br.com.darkside.view.dialog

import br.com.darkside.defaultPadding
import tornadofx.*

class ShortcutsAlert: Fragment("Shortcuts") {

    override val root = vbox {
        val labelPadding = 5.0
        setPrefSize(500.0, 300.0)
        label("CTRL + N - New variable") {
            paddingAll = labelPadding
        }
        label("CTRL + O - Open path") {
            paddingAll = labelPadding
        }
        label("CTRL + B - Select other bash file") {
            paddingAll = labelPadding
        }
        label("CTRL + A - About this application") {
            paddingAll = labelPadding
        }
        spacer()
        hbox {
            paddingAll = defaultPadding
            spacer()
            button("Close").action {
                close()
            }
            spacer()
        }
    }
}
