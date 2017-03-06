(ns octolook.core
  (:gen-class)
  (:require [clojure.tools.nrepl.server :refer [start-server stop-server]]
            [ring.adapter.jetty :refer [run-jetty]]
            [octolook.handler :refer [app handler]]
            [omniconf.core :as cfg]
            [morse.polling :as p]
            [octolook.handler :as h]
            [octolook.config]))

(defonce nrepl-server (atom nil))

(defonce telegram-poller (atom nil))

(defn -main [& args]
  (cfg/populate-from-env)
  (cfg/verify :quit-on-error true)

  (reset! nrepl-server
          (start-server :port (cfg/get :repl)
                        :bind "0.0.0.0"))

  (reset! telegram-poller
          (p/start (cfg/get :telegram-token) h/handler))

  (run-jetty app {:port  (cfg/get :port)
                  :join? false}))
