package br.com.darkside.view.menu

import javafx.scene.layout.Pane
import tornadofx.*

class MainMenu: Pane() {
    var onCreateMenu: () -> Unit = {}
    var onOpenPathMenu: () -> Unit = {}
    var onSelectFileMenu: () -> Unit = {}
    var onAboutMenu: () -> Unit = {}
    var onShortcutsMenu: () -> Unit = {}

    init {
        menubar {
            menu("File") {
                item("New").action {
                    onCreateMenu()
                }
                item("Open path").action {
                    onOpenPathMenu()
                }
                item("Bash file").action {
                    onSelectFileMenu()
                }
            }
            menu("Help"){
                item("Shortcuts").action {
                    onShortcutsMenu()
                }
                item("About").action {
                    onAboutMenu()
                }
            }
        }
    }
}