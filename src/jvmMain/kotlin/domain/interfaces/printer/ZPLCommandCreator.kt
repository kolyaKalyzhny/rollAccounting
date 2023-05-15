package domain.interfaces.printer

interface ZPLCommandCreator {
    fun createZPLCommand(data: String): String
}