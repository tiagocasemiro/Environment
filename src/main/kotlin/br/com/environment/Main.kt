package br.com.environment

import br.com.environment.view.MainView
import tornadofx.*

class AppView: App(MainView::class)

fun main(args: Array<String>) {
    launch<AppView>(args)
}
