(defproject octolook "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-jetty-adapter "1.4.0"]

                 [com.taoensso/timbre "4.1.4"]
                 ;[org.slf4j/slf4j-api "1.7.14"]
                 ;[com.fzakaria/slf4j-timbre "0.3.4"]

                 [com.grammarly/omniconf "0.2.2"]
                 [compojure "1.5.1"]
                 [clj-http "3.4.1"]
                 [cheshire "5.7.0"]
                 [morse "0.2.4"]]

  :plugins [[lein-ring "0.9.7"]]

  :main ^:skip-aot octolook.core
  :ring {:handler octolook.handler/app}

  :profiles {:production {:env {:production true}}
             :uberjar {:aot :all
                       :uberjar-name "octolook.jar"}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}})
