# oap-tsv

TSV is a Tab Separated Value format. Unlike Comma Separated Value (CSV) it contains ONLY tabular character to distingish columns.
CSV my  be divided by comma, tab, semi-colon, pipe etc.
It also has some rules to separate columns with data inside them, if it contains special characters (a.k.a. escaping).
TSV alsways wrap dta into quotes if separator is comma.
Like [1..3] becomes '"1","2","3"' (with tabs)
and [1..3] becomes '1  2 3' (with comma)
