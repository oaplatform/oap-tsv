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

import oap.io.Files;
import oap.tsv.Tsv;
import oap.util.Arrays;
import oap.util.Strings;
import org.assertj.core.api.AbstractAssert;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TsvAssertion extends AbstractAssert<TsvAssertion, Tsv> {

    protected TsvAssertion( String value ) {
        super( Tsv.tsv.fromString( value ).withHeaders().toTsv(), TsvAssertion.class );
    }

    public static TsvAssertion assertTsv( String tsv ) {
        return new TsvAssertion( tsv );
    }

    public static TsvAssertion assertTsv( Path path ) {
        return new TsvAssertion( Files.readString( path ) );
    }

    public static TsvAssertion assertTsv( File file ) {
        return new TsvAssertion( Files.readString( file.toPath() ) );
    }

    public static TsvAssertion assertTsv( InputStream is ) {
        return new TsvAssertion( Strings.readString( is ) );
    }

    public TsvAssertion hasHeaders( String... headers ) {
        assertThat( actual.headers ).contains( headers );
        return this;
    }

    public TsvAssertion hasHeaders( Iterable<String> headers ) {
        assertThat( actual.headers ).containsAll( headers );
        return this;
    }

    public TsvAssertion containOnlyHeaders( String... headers ) {
        assertThat( actual.headers ).containsOnly( headers );
        return this;
    }

    public TsvAssertion contains( List<String> headers, String... entries ) {
        hasHeaders( headers );
        assertThat( entries.length % headers.size() )
            .withFailMessage( "entries length doesnt match headers" )
            .isEqualTo( 0 );
        assertThat( actual.stream()
            .select( headers )
            .stripHeaders()
            .toTsv()
            .data )
            .contains( Arrays.map( List.class, List::of, Arrays.splitBy( String.class, headers.size(), entries ) ) );

        return this;
    }

    public TsvAssertion isNotEmpty() {
        assertThat( actual.data ).isNotEmpty();
        return this;
    }
}
