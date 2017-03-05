(ns octolook.core
  (:gen-class)
  (:require [clojure.tools.nrepl.server :refer [start-server stop-server]]
            [ring.adapter.jetty :refer [run-jetty]]
            [octolook.handler :refer [app]]
            [omniconf.core :as cfg]
            [morse.polling :as p]
            [octolook.telegram :as t]))

(cfg/define {:port           {:description "HTTP port"
                              :type        :number
                              :default     8080}
             :repl           {:description "REPL port"
                              :type        :number
                              :default     7888}
             :telegram-token {:description "Token for Telegram Bot API"
                              :type        :string
                              :required    true
                              :secret      true}})

(defonce nrepl-server (atom nil))


(defn -main [& args]
  (cfg/populate-from-env)
  (cfg/verify :quit-on-error true)
  (reset! nrepl-server (start-server :port (cfg/get :repl)
                                     :bind "0.0.0.0"))
  (p/start (cfg/get :telegram-token) t/handler)
  (run-jetty app {:port  (cfg/get :port)
                  :join? false}))
