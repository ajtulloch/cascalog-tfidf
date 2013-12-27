(defproject cascalog-tfidf "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cascalog "2.0.0"]
                 [cascalog-more-taps "0.3.0"]
                 [jackknife "0.1.6"]
                 [cascalog-checkpoint "0.1.1"]
                 [org.apache.hadoop/hadoop-core "1.1.2"]]
  :main cascalog-tfidf.core
  :jvm-opts ["-Xms768m" "-Xmx768m"]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies []}})
