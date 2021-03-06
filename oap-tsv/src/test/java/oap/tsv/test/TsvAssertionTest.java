/*
 * The MIT License (MIT)
 *
 * Copyright (c) Open Application Platform Authors
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

package oap.tsv.test;

import oap.tsv.Tsv;
import org.testng.annotations.Test;

import java.util.List;

import static oap.tsv.test.TsvAssertion.assertTsv;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TsvAssertionTest {

    @Test
    public void containsHeaders() {
        assertTsv( "a\tb\tc\n1\t2\t3" ).hasHeaders( "a" );

        assertThatThrownBy( () ->
            assertTsv( "a\tb\tc\n1\t2\t3" ).hasHeaders( "unknown" ) )
            .isInstanceOf( AssertionError.class );
    }

    @Test
    public void contains() {
        String tsv = """
            a\tb\tc
            11\t12\t13
            21\t22\t23
            """;
        assertTsv( tsv )
            .contains(
                List.of( "a", "b" ),
                "21", "22",
                "11", "12"
            );

        assertThatThrownBy( () ->
            assertTsv( tsv )
                .contains(
                    List.of( "a", "b" ),
                    "11", "22" )
                .isInstanceOf( AssertionError.class ) );
    }

    @Test
    public void doesNotContain() {
        String tsv = """
            a\tb\tc
            11\t12\t13
            21\t22\t23
            """;
        assertTsv( tsv )
            .doesNotContain( "11", "12", "14" );

        assertThatThrownBy( () ->
            assertTsv( tsv )
                .doesNotContain( "11", "12", "13" )
                .isInstanceOf( AssertionError.class ) );
    }

    @Test
    public void isEqualTo() {
        String tsv = """
            a\tb\tc
            1\t2\t3
            3\t2\t1
            """;
        assertTsv( tsv ).isEqualTo( Tsv.tsv.fromString( tsv ).withHeaders().toTsv() );
        assertTsv( tsv ).isEqualToTsv( """
            a\tb\tc
            3\t2\t1
            1\t2\t3
            """ );
    }
}
