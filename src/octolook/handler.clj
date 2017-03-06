(ns octolook.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [morse.handlers :as h :refer [defhandler command-fn]]
            [octolook.telegram :as t]
            [taoensso.timbre :as log]))

(defhandler handler
  (command-fn "start" t/start)
  (command-fn "help" t/help)
  (command-fn "repo" t/repo)
  (h/inline-fn t/inline)

  (h/message-fn
    (fn [message] (log/info "Unhandled message:" message))))

(defroutes app-routes
  (GET "/" [] "Hello world")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)))
