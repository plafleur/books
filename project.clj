(defproject books "0.1.0-SNAPSHOT"
  :description "Web app for users to track books read"
  :url "https://github.com/plafleur/books"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                [org.clojure/java.jdbc "0.6.1"]
                [org.postgresql/postgresql "9.4-1201-jdbc41"]
		            [ring/ring-jetty-adapter "1.4.0"]
                [compojure "1.4.0"]
		            [hiccup "1.0.5"]
		            [hiccup-table "0.2.0"]
		            [buddy/buddy-hashers "1.3.0"]]
  :main ^:skip-aot books.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
