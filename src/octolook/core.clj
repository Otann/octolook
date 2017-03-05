(ns octolook.core
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [octolook.handler :refer [app]]
            [omniconf.core :as cfg]
            [clojure.tools.nrepl.server :refer [start-server stop-server]]))

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
  (reset! nrepl-server (start-server :port 7888))
  (run-jetty app {:port  (cfg/get :port)
                  :join? false}))
