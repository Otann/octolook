(ns octolook.core
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [octolook.handler :refer [app]]
            [omniconf.core :as cfg]))

(cfg/define {:port {:description "HTTP port"
                    :type :number
                    :default 8080}})

(defn -main [& args]
  (cfg/populate-from-env)
  (cfg/verify :quit-on-error true)
  (run-jetty app {:port (cfg/get :port)
                  :join? false}))