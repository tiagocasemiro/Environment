package br.com.environment.view.dialog

import br.com.environment.defaultPadding
import tornadofx.*

class AboutAlert: Fragment("About") {
    override val root = borderpane {
        center {
            vbox {
                setPrefSize(500.0, 300.0)
                spacer()
                label(message()) {
                    paddingAll = defaultPadding
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
    }

    private fun message() = "Developer:\nTiago paiva Casemiro\n\nRepo:\nhttps://github.com/tiagocasemiro/Environment"
}
