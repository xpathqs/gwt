package org.xpathqs.gwt.equals

import org.xpathqs.gwt.GIVEN
import org.xpathqs.gwt.When
import org.xpathqs.log.style.StyledString

fun <G,W> When<G,W>.THEN(f: When<G, W>.()->W) = THEN("", f)
fun <G,W> When<G,W>.THEN(msg: String, f: When<G, W>.()->W) = THEN(StyledString(msg), f)
fun <G,W> When<G,W>.THEN(msg: StyledString, f: When<G, W>.()->W): When<G, W> {
    GIVEN.log.action(msg, GIVEN.THEN) {
        GIVEN.gwtAssert.equals(actual, f())
    }
    return this
}

fun <G,W> When<G,W>.AFTER(f: When<G, W>.()->W): When<G, W> {
    this.f()
    return this
}