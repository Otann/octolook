(ns octolook.config
  (:require [omniconf.core :as cfg]))

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