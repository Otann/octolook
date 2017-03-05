(defproject octolook "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [com.taoensso/timbre "4.1.4"]
                 ;[environ "1.1.0"]
                 [com.grammarly/omniconf "0.2.2"]
                 [compojure "1.5.1"]
                 [clj-http "3.4.1"]
                 [cheshire "5.7.0"]]

  :plugins [[lein-ring "0.9.7"]
            [lein-environ "1.1.0"]]

  :ring {:handler octolook.handler/app}

  :profiles {:production {:env {:production true}}
             :uberjar {:aot :all
                       :main octolook.core
                       :uberjar-name "octolook.jar"}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}})
