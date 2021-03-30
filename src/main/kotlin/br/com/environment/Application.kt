package br.com.environment

import br.com.environment.view.MainScreen
import tornadofx.App

class Application: App(MainScreen::class)

const val defaultPadding = 10.0
const val defaultWidth = 1000.0
const val defaultHeight = 600.0
var currentBashFile: String? = null
