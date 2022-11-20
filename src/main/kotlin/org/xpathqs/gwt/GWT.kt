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

import org.xpathqs.gwt.GIVEN.Companion.log
import org.xpathqs.gwt.GIVEN.Companion.logEvaluator
import org.xpathqs.log.BaseLogger
import org.xpathqs.log.message.IMessage
import org.xpathqs.log.message.tag
import org.xpathqs.log.style.StyledString

open class GIVEN<G: Any> {
    lateinit var given: G

    constructor(msg: String, f: G): this(msg, {f})
    constructor(msg: String, f: () -> G): this(StyledString(msg), f)
    constructor(msg: StyledString, f: () -> G)  {
        logEvaluator.GIVEN(msg, this, f)
    }
    constructor(f: () -> G): this("", f)


    fun<W> WHEN(msg: String, f: GIVEN<G>.()->W) = WHEN(StyledString(msg), f)
    fun<W> WHEN(msg: StyledString, f: GIVEN<G>.()->W) : When<G, W> {
        return When(
            given,
            logEvaluator.WHEN(msg, this, f)
        )

    }
    fun<W> WHEN(f: GIVEN<G>.()->W) = WHEN("", f)

    companion object {
        var logEvaluator: ILogEvaluate = LogEvaluateImpl()
        var log: BaseLogger = BaseLogger()
        var gwtAssert: IGwtAssert = GwtAssertImpl()

        const val GIVEN = "GIVEN"
        const val WHEN = "WHEN"
        const val THEN = "THEN"
    }
}

open class When<G: Any, W>(val given: G, val actual: W) {

    open fun<W:Any> THEN(expected: W) = THEN("", expected)
    open fun<W:Any> THEN(msg: String, expected: W) = THEN(StyledString(msg), expected)
    open fun<W:Any> THEN(msg: StyledString, expected: W): When<G, W> {
        log.action(msg, GIVEN.THEN) {
            GIVEN.gwtAssert.equals(actual, expected)
        }
        return this as When<G, W>
    }

    open fun THEN(f: When<G, W>.()->Unit) = THEN("", f)
    open fun THEN(msg: String, f: When<G, W>.()->Unit) = THEN(StyledString(msg), f)
    open fun THEN(msg: StyledString, f: When<G, W>.()->Unit): When<G, W> {
        logEvaluator.THEN(msg, this, f)
        return this
    }
}

fun<G: Any, W> When<G,*>.AND(msg: String, f: GIVEN<G>.()->W): When<G,W> {
    val given = GIVEN("", this.given)

    return When(
        given.given,
        logEvaluator.WHEN(StyledString(msg), given, f)
    )
}

fun<W> WHEN(f: GIVEN<String>.()->W) = WHEN("", f)
fun<W> WHEN(msg: String, f: GIVEN<String>.()->W) = WHEN(StyledString(msg), f)
fun<W> WHEN(msg: StyledString, f: GIVEN<String>.()->W): When<String, W> {
    val given = GIVEN { "" }
    return When(
        given.given,
        logEvaluator.WHEN(msg, given, f)
    )
}

val IMessage.isThen: Boolean
    get() = this.tag == GIVEN.THEN

val IMessage.isWhen: Boolean
    get() = this.tag == GIVEN.WHEN

val IMessage.isGiven: Boolean
    get() = this.tag == GIVEN.GIVEN