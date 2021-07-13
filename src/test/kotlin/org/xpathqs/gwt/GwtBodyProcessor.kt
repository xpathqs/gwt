package org.xpathqs.gwt

import org.xpathqs.log.abstracts.IBodyProcessor
import org.xpathqs.log.message.IMessage
import org.xpathqs.log.message.tag
import org.xpathqs.log.printers.body.BodyProcessorDecorator

open class GwtBodyProcessor (  origin: IBodyProcessor
) : BodyProcessorDecorator(origin) {
    override fun selfProcess(msg: IMessage, body: String): String {
        return "${msg.tag} $body"
    }
}