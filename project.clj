(defproject dyscord "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [goog-jar "1.0.0"]]
  :plugins [[lein-cljsbuild "0.2.1"]
            [lein-swank "1.4.4"]]
  :cljsbuild {
              :builds[{
                      ;; The path to the toplevel clojurescript source directory
                      :source-path "src-cljs"
                      :compiler  {
                                  :output-to "war/javascript/dyscord.js"
                                  :optimizations :whitespace
                                  :pretty-print true}}]})