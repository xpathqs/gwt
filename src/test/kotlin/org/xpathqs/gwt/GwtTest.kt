/*
 * Copyright (c) 2021 XpathQs
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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GwtTest {

    @Test
    fun gwt_example() {
        GIVEN {
            "a string"
        }.WHEN {
            given.length
        }.THEN(8)
    }

    @Test
    fun gwt_with_log_example() {
        GIVEN("A String of '8' chars") {
            "a string"
        }.WHEN("Calling a length") {
            given.length
        }.THEN("It Should return '8'", 8)
    }

    @Test
    fun gwt_assert_example() {
        GIVEN {
            "a string"
        }.WHEN {
            given.length
        }.THEN {
            assertEquals(actual, 8)
        }
    }

    @Test
    fun noGiven_example() {
        WHEN {
            "str".length
        }.THEN(3)
    }

    @Test
    fun noGiven_example_withCustomAssert() {
        WHEN {
            "str".length
        }.THEN("Check Length") {
            assertEquals(actual, 3)
        }
    }
}