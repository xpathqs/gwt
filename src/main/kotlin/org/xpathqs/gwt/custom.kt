package org.xpathqs.gwt

import org.xpathqs.log.style.StyledString

fun<G,W> When<G,W>.THEN(expected: W) = THEN("", expected)
fun<G,W> When<G,W>.THEN(msg: String, expected: W) = THEN(StyledString(msg), expected)
fun <G,W> When<G,W>.THEN(msg: StyledString, expected: W): When<G, W> {
    GIVEN.log.action(msg, GIVEN.THEN) {
        GIVEN.gwtAssert.equals(actual, expected)
    }
    return this
}

fun <G,W> When<G,W>.THEN(f: When<G, W>.()->Unit) = THEN("", f)
fun <G,W> When<G,W>.THEN(msg: String, f: When<G, W>.()->Unit) = THEN(StyledString(msg), f)
fun <G,W> When<G,W>.THEN(msg: StyledString, f: When<G, W>.()->Unit): When<G, W> {
    GIVEN.log.action(msg, GIVEN.THEN) {
        f()
    }
    return this
}

fun <G,W> When<G,W>.AFTER(f: When<G, W>.()->Unit): When<G, W> {
    this.f()
    return this
}