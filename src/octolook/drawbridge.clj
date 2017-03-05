(ns octolook.drawbridge
  (:require [cemerick.drawbridge :as drawbridge]
            [ring.middleware.session :as session]
            [ring.middleware.params :as params]
            [ring.middleware.nested-params :as nested-params]
            [ring.middleware.keyword-params :as keyword-params]))

(def drawbridge-handler
  (-> (drawbridge/ring-handler)
      (keyword-params/wrap-keyword-params)
      (nested-params/wrap-nested-params)
      (params/wrap-params)
      (session/wrap-session)))

(defn wrap-drawbridge [handler]
  (fn [req]
    (if (= "/repl" (:uri req))
      (drawbridge-handler req)
      (handler req))))