package org.xpathqs.gwt

import org.xpathqs.log.style.StyledString

interface ILogEvaluate {
    fun<G: Any> GIVEN(msg: StyledString, obj: GIVEN<G>, f: () -> G)
    fun<G: Any,W> WHEN(msg: StyledString, obj: GIVEN<G>, f: GIVEN<G>.()->W) : W
    fun<G: Any,W> THEN(msg: StyledString, obj: When<G, W>, f: When<G, W>.()->Unit)
}

internal class LogEvaluateImpl : ILogEvaluate {
    override fun <G: Any> GIVEN(msg: StyledString, obj: GIVEN<G>, f: () -> G) {
        obj.given = if(msg.toString().isNotEmpty()) {
            GIVEN.log.action(msg, GIVEN.GIVEN, f)
        } else {
            f()
        }
    }

    override fun <G: Any, W> WHEN(msg: StyledString, obj: GIVEN<G>, f: GIVEN<G>.()->W) : W {
        return GIVEN.log.action(msg, GIVEN.WHEN) {
            obj.f()
        }
    }

    override fun<G: Any,W> THEN(msg: StyledString, obj: When<G, W>, f: When<G, W>.()->Unit) {
        GIVEN.log.action(msg, GIVEN.THEN) {
            obj.f()
        }
    }
}