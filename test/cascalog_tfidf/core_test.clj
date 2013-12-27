(ns cascalog-tfidf.core-test
  (:use cascalog-tfidf.core
        clojure.test
        cascalog.api
        midje.sweet
        midje.cascalog))

(deftest scrub-text-test
  (fact
   (scrub-text "FoO BAR  ") => "foo bar"))

(deftest etl-docs-gen-test
  (let [rain [["doc1" "a b c"]]
        stop [["b"]]]
    (fact
     (etl-docs-gen rain stop) => (produces [["doc1" "a"]
                                            ["doc1" "c"]]))))
