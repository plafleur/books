(defproject books "0.1.0-SNAPSHOT"
  :description "Web app for users to track books read"
  :url "https://github.com/plafleur/books"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot books.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
