(ns octolook.github
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.string :as str]))

(def gh-base "https://api.github.com")

(defn repo [name]
  (let [url  (str gh-base "/repos/" name)
        resp (http/get url {:throw-exceptions false})
        {:keys [status body]} resp]
    (when (< status 300)
      (json/parse-string body true))))

(defn user-repos [user prefix]
  (let [url  (str gh-base "/users/" user "/repos")
        resp (http/get url {:throw-exceptions false
                            :query-params     {:type "owner"}})
        {:keys [status body]} resp]
    (when (< status 300)
      (->> (json/parse-string body true)
           (filter #(-> % :name (str/starts-with? prefix)))))))

(defn search-owner [name]
  (let [url  (str gh-base "/search/users")
        resp (http/get url {:throw-exceptions false
                            :query-params     {:q name}})
        {:keys [status body]} resp]
    (when (< status 300)
      (-> (json/parse-string body true)
          :items))))

(defn search-repo [name]
  (let [url (str gh-base "/search/repositories")
        resp (http/get url {:throw-exceptions false
                            :query-params {:q name}})
        {:keys [status body]} resp]
    (when (< status 300)
      (-> (json/parse-string body true)
          :items))))

