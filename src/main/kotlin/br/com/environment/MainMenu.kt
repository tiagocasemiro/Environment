package br.com.environment

import br.com.environment.view.dialog.AboutAlert
import br.com.environment.view.dialog.ShortcutsAlert
import javafx.scene.layout.Pane
import javafx.stage.StageStyle
import tornadofx.action
import tornadofx.item
import tornadofx.menu
import tornadofx.menubar


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