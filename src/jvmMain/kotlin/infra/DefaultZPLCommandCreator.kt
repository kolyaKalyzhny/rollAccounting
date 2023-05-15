package infra

import domain.interfaces.printer.ZPLCommandCreator

class DefaultZPLCommandCreator : ZPLCommandCreator {
    override fun createZPLCommand(data: String): String {
        return "^XA^FO80,80^BY2^BCN,80,Y,N,N^FD$data^FS^XZ"
    }
}