(ns octolook.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn test-response [] "Test subject 2")

(defroutes app-routes
  (GET "/" [] "Hello world")
  (GET "/test" [] (test-response))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)))
