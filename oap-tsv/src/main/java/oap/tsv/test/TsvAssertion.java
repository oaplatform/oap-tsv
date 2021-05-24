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
import oap.io.content.ContentReader;
import oap.tsv.Tsv;
import oap.util.Arrays;
import org.assertj.core.api.AbstractAssert;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static oap.io.content.ContentReader.ofString;
import static org.assertj.core.api.Assertions.assertThat;

public class TsvAssertion extends AbstractAssert<TsvAssertion, Tsv> {
    protected TsvAssertion( String value ) {
        this( value, true );
    }

    protected TsvAssertion( String value, boolean withHeaders ) {
        this( withHeaders ? Tsv.tsv.fromString( value ).withHeaders().toTsv()
            : Tsv.tsv.fromString( value ).toTsv() );
    }

    protected TsvAssertion( Tsv value ) {
        super( value, TsvAssertion.class );
    }

    public static TsvAssertion assertTsv( String tsv ) {
        return assertTsv( tsv, true );
    }

    public static TsvAssertion assertTsv( String tsv, boolean withHeaders ) {
        return new TsvAssertion( tsv, withHeaders );
    }

    public static TsvAssertion assertTsv( Tsv tsv ) {
        return new TsvAssertion( tsv );
    }

    public static TsvAssertion assertTsv( Path path ) {
        return assertTsv( path, true );
    }

    public static TsvAssertion assertTsv( Path path, boolean withHeaders ) {
        return assertTsv( Files.read( path, ofString() ), withHeaders );
    }

    public static TsvAssertion assertTsv( File file ) {
        return assertTsv( file, true );
    }

    public static TsvAssertion assertTsv( File file, boolean withHeaders ) {
        return assertTsv( Files.read( file.toPath(), ofString() ), withHeaders );
    }

    public static TsvAssertion assertTsv( InputStream is ) {
        return assertTsv( is, true );
    }

    public static TsvAssertion assertTsv( InputStream is, boolean withHeaders ) {
        return assertTsv( ContentReader.read( is, ofString() ), withHeaders );
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
            .containsExactlyInAnyOrderElementsOf(
                List.of(
                    Arrays.map( List.class, List::of,
                        Arrays.splitBy( headers.size(), entries ) ) ) );
        return this;
    }

    public TsvAssertion doesNotContain( String... entries ) {
        assertThat( actual.headers )
            .withFailMessage( "tsv must contain headers" )
            .isNotEmpty();
        assertThat( entries.length % actual.headers.size() )
            .withFailMessage( "entries length doesnt match headers" )
            .isEqualTo( 0 );
        assertThat( actual.data )
            .doesNotContainAnyElementsOf(
                List.of(
                    Arrays.map( List.class, List::of,
                        Arrays.splitBy( actual.headers.size(), entries ) ) ) );
        return this;
    }

    public TsvAssertion isNotEmpty() {
        assertThat( actual.data ).isNotEmpty();
        return this;
    }

    public TsvAssertion isEqualToTsv( String tsv ) {
        Tsv expected = Tsv.tsv.fromString( tsv ).withHeaders().toTsv();
        hasHeaders( expected.headers );
        assertThat( this.actual.data ).containsExactlyInAnyOrderElementsOf( expected.data );
        return this;
    }

    public TsvAssertion isEqualToTsv( Path tsv ) {
        return isEqualToTsv( Files.read( tsv, ofString() ) );
    }
}
