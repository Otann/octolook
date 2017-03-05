(ns octolook.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn test-response [] "Test subject")

(defroutes app-routes
  (GET "/" [] "Hello world 2")
  (GET "/test" [] (test-response))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)))
