(ns octolook.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [octolook.drawbridge :as drawbridge]))

(defn test-response [] "Test subject")

(defroutes app-routes
  (GET "/test" [] (test-response))
  ;(ANY "/repl" request (nrepl-handler request))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (drawbridge/wrap-drawbridge)))

