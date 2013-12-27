(ns cascalog-tfidf.core
  (:require [cascalog.logic.def :as def]
            [cascalog.logic.ops :as c]
            [cascalog.logic.vars :as v]
            [clojure.string :as s])
  (:use [cascalog.checkpoint]
        [cascalog.api]
        [cascalog.more-taps :only (hfs-delimited)] 
        [cascalog.playground])
  (:gen-class))

(def/defmapcatfn tokenise [line]
  (s/split line #"[\[\]\\\(\),.)\s]+"))

(defn scrub-text [s]
  (-> s
      (s/trim)
      (s/lower-case)))

(defn etl-docs-gen [rain stop]
  (<- [?doc-id ?word]
      (rain ?doc-id ?line)
      (tokenise ?line :> ?word-dirty)
      (stop ?word :> false)
      (scrub-text ?word-dirty :> ?word)))

(defn word-count [src]
  (<- [?word ?count]
      (src _ ?word)
      (c/count ?count)))

(defn D [src]
  (let [src (select-fields src ["?doc-id"])]
    (<- [?n-docs]
        (src ?doc-id)
        (c/distinct-count ?doc-id :> ?n-docs))))

(defn DF [src]
  (<- [?df-word ?df-count]
      (src ?doc-id ?df-word)
      (c/distinct-count ?doc-id ?df-word :> ?df-count)))

(defn TF [src]
  (<- [?doc-id ?tf-word ?tf-count]
      (src ?doc-id ?tf-word)
      (c/count ?tf-count)))

(defn- tf-idf-formula [tf-count df-count n-docs]
  (->> (+ 1.0 df-count)
       (div n-docs)
       (Math/log)
       (* tf-count)))

(defn TF-IDF [src]
  (let [n-doc (first (flatten (??- (D src))))]
    (<- [?doc-id ?tf-idf ?tf-word]
        ((TF src) ?doc-id ?tf-word ?tf-count)
        ((DF src) ?tf-word ?df-count)
        (tf-idf-formula ?tf-count ?df-count n-doc :> ?tf-idf))))

(defn -main [in out stop tfidf & args]
  (workflow
   ["tmp/checkpoint"]
   etl-step ([:tmp-dirs etl-stage]
               (let [rain (hfs-delimited in :skip-header? true) stop (hfs-delimited stop :skip-header? true)]
                 (?- (hfs-delimited etl-stage)
                     (etl-docs-gen rain stop))))
   tf-step ([:deps etl-step]
              (let [src (name-vars (hfs-delimited etl-stage :skip-header? true)
                                   ["?doc-id" "?word"])]
                (?- (hfs-delimited tfidf)
                    (TF-IDF src))))
   wrd-step ([:deps etl-step]
               (?- (hfs-delimited out)
                   (word-count (hfs-delimited etl-stage))))))
