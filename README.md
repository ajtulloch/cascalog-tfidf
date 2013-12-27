# cascalog-tfidf

Simple implementation of [TF-IDF][] in Cascalog, following the
examples in [Enterprise Data Workflows in Cascading][].

## Installation

    $ git clone https://github.com/ajtulloch/cascalog-tfidf
    $ lein deps
    $ lein test
    $ lein uberjar

## Usage

    $ rm -rf output
    $ hadoop jar target/cascalog-tfidf-0.1.0-SNAPSHOT-standalone.jar \
    data/rain.txt output/wc data/en.stop output/tfidf

In `output/tfidf/part-0000`, we have

    doc02	0.22314355131420976	area
    doc01	0.44628710262841953	area
    doc03	0.22314355131420976	area
    doc05	0.9162907318741551	australia
    doc05	0.9162907318741551	broken
    doc04	0.9162907318741551	california's
    doc04	0.9162907318741551	cause
    doc02	0.9162907318741551	cloudcover
    ...
    doc02	0.9162907318741551	sinking
    doc04	0.9162907318741551	such
    doc04	0.9162907318741551	valley
    doc05	0.9162907318741551	women

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[Enterprise Data Workflows in Cascading]: http://shop.oreilly.com/product/0636920028536.do
[TF-IDF]: http://en.wikipedia.org/wiki/Tf%E2%80%93idf
