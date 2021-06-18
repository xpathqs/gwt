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

class GIVEN<G> {
    val given: G

    constructor(msg: String, f: () -> G)  {
        given = if(Notifier.useLambdaLog) {
            Notifier.lambdaLog.onGiven(msg, f) as G
        } else {
            Notifier.log.onGiven(msg)
            f()
        }
    }
    constructor(f: () -> G): this("", f)

    fun<W> WHEN(msg: String, f: GIVEN<G>.()->W) : When<G, W> {
        return if(Notifier.useLambdaLog) {
            When(
                this.given,
                Notifier.lambdaLog.onWhen(msg) {
                    f()
                } as W
            )
        } else {
            Notifier.log.onWhen(msg)
            When(this.given, f())
        }
    }
    fun<W> WHEN(f: GIVEN<G>.()->W) = WHEN("", f)
}

class When<G, W>(val given: G, val actual: W) {
    fun THEN(expected: W) = THEN("", expected)
    fun THEN(msg: String, expected: W): When<G, W> {
        if(Notifier.useLambdaLog) {
            Notifier.lambdaLog.onThen(msg) {
                Notifier.assert.equals(actual, expected)
            }
        } else {
            Notifier.log.onThen(msg)
            Notifier.assert.equals(actual, expected)
        }

        return this
    }

    fun THEN(f: When<G, W>.()->Unit) = THEN("", f)
    fun THEN(msg: String, f: When<G, W>.()->Unit): When<G, W> {
        if(Notifier.useLambdaLog) {
            Notifier.assert.assertAll {
                Notifier.lambdaLog.onThen(msg) {
                    f()
                }
            }
        } else {
            Notifier.log.onThen(msg)
            Notifier.assert.assertAll {
                f()
            }
        }

        return this
    }

    fun AFTER(f: When<G, W>.()->Unit): When<G, W> {
        this.f()
        return this
    }
}
fun<W> WHEN(f: GIVEN<String>.()->W) = WHEN("", f)
fun<W> WHEN(msg: String, f: GIVEN<String>.()->W): When<String, W> {
    val given = GIVEN { "" }

    return if(Notifier.useLambdaLog) {
        When(given.given, Notifier.lambdaLog.onWhen(msg) {
            given.f()
        } as W)
    } else {
        Notifier.log.onWhen(msg)
        When(given.given, given.f())
    }

}