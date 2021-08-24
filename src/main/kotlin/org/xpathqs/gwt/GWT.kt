/*
 * Copyright (c) 2021 Xpath-Qs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.xpathqs.gwt

import org.xpathqs.gwt.GIVEN.Companion.gwtAssert
import org.xpathqs.gwt.GIVEN.Companion.log
import org.xpathqs.log.BaseLogger
import org.xpathqs.log.message.IMessage
import org.xpathqs.log.message.tag
import org.xpathqs.log.style.StyledString

open class GIVEN<G> {
    val given: G

    constructor(msg: String, f: () -> G): this(StyledString(msg), f)
    constructor(msg: StyledString, f: () -> G)  {
        given = if(msg.toString().isNotEmpty()) {
            log.action(msg, GIVEN, f)
        } else {
            f()
        }
    }
    constructor(f: () -> G): this("", f)

    fun<W> WHEN(msg: String, f: GIVEN<G>.()->W) = WHEN(StyledString(msg), f)
    fun<W> WHEN(msg: StyledString, f: GIVEN<G>.()->W) : When<G, W> {
        return When(
            given,
            log.action(msg, WHEN) {
                f()
            }
        )
    }
    fun<W> WHEN(f: GIVEN<G>.()->W) = WHEN("", f)

    companion object {
        var log: BaseLogger = BaseLogger()
        var gwtAssert: IGwtAssert = GwtAssertImpl()

        const val GIVEN = "GIVEN"
        const val WHEN = "WHEN"
        const val THEN = "THEN"
    }
}

open class When<G, W>(val given: G, val actual: W) {

}
fun<W> WHEN(f: GIVEN<String>.()->W) = WHEN("", f)
fun<W> WHEN(msg: String, f: GIVEN<String>.()->W) = WHEN(StyledString(msg), f)
fun<W> WHEN(msg: StyledString, f: GIVEN<String>.()->W): When<String, W> {
    val given = GIVEN { "" }
    return When(
        given.given,
        log.action(msg, GIVEN.WHEN) {
            given.f()
        }
    )
}

val IMessage.isThen: Boolean
    get() = this.tag == GIVEN.THEN

val IMessage.isWhen: Boolean
    get() = this.tag == GIVEN.WHEN

val IMessage.isGiven: Boolean
    get() = this.tag == GIVEN.GIVEN