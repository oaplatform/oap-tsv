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

package oap.tsv;

import org.testng.annotations.Test;

import java.util.List;

import static oap.testng.Asserts.assertString;
import static org.assertj.core.api.Assertions.assertThat;

public class TsvStreamTest {
    @Test
    public void csv() {
        String csv = """
            "1","2","3"
            "1","2","3"
            """;
        assertString( Tsv.csv.fromString( csv )
            .toCsvString() )
            .isEqualTo( csv );
    }

    @Test
    public void toList() {
        assertThat( Tsv.tsv.fromString( "1\t2\t3\n1\t2\t3" )
            .toList() )
            .containsExactly(
                List.of( "1", "2", "3" ),
                List.of( "1", "2", "3" ) );
    }

    @Test
    public void toStrng() {
        assertString( Tsv.tsv.fromString( "1\t2\t3\n1\t2\t3" )
            .toTsvString() )
            .isEqualTo( "1\t2\t3\n1\t2\t3\n" );
    }

    @Test
    public void withHeaders() {
        assertThat( Tsv.tsv.fromString( "a\tb\tc\n1\t2\t3\n1\t2\t3" )
            .withHeaders()
            .withHeaders()
            .withHeaders()
            .withHeaders()
            .headers() )
            .containsExactly( "a", "b", "c" );
        assertThat( Tsv.tsv.fromString( "a\tb\tc\n1\t2\t3\n1\t2\t3" )
            .withHeaders()
            .toList() )
            .containsExactly(
                List.of( "a", "b", "c" ),
                List.of( "1", "2", "3" ),
                List.of( "1", "2", "3" ) );
    }

    @Test
    public void select() {
        assertThat( Tsv.tsv.fromString( "a\tb\tc\n1\t2\t3\n1\t2\t3" )
            .withHeaders()
            .select( 0, 2 )
            .toList() )
            .containsExactly(
                List.of( "a", "c" ),
                List.of( "1", "3" ),
                List.of( "1", "3" ) );
    }

    @Test
    public void selectByHeaders() {
        assertThat( Tsv.tsv.fromString( "a\tb\tc\n1\t2\t3\n1\t2\t3" )
            .select( "a", "c" )
            .toList() )
            .containsExactly(
                List.of( "a", "c" ),
                List.of( "1", "3" ),
                List.of( "1", "3" ) );
        assertThat( Tsv.tsv.fromString( "a\tb\tc\n1\t2\t3\n1\t2\t3" )
            .withHeaders()
            .select( "a", "c" )
            .toList() )
            .containsExactly(
                List.of( "a", "c" ),
                List.of( "1", "3" ),
                List.of( "1", "3" ) );
    }

    @Test
    public void stripHeaders() {
        assertThat( Tsv.tsv.fromString( "a\tb\tc\n1\t2\t3\n1\t2\t3" )
            .stripHeaders()
            .toList() )
            .containsExactly(
                List.of( "1", "2", "3" ),
                List.of( "1", "2", "3" ) );
    }

    @Test
    public void filter() {
        assertThat( Tsv.tsv.fromString( "\n\na\tb\tc\n\n1\t2\t3\n1\t2\t3" )
            .filter( line -> line.size() == 3 )
            .withHeaders()
            .select( 0, 2 )
            .toList() )
            .containsExactly(
                List.of( "a", "c" ),
                List.of( "1", "3" ),
                List.of( "1", "3" ) );
    }
}
