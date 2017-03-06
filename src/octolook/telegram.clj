(ns octolook.telegram
  (:require [clojure.string :as str]
            [octolook.github :as gh]
            [morse.api :as t]
            [morse.handlers :as h]
            [taoensso.timbre :as log]
            [omniconf.core :as cfg]))

(defn repo-info [text]
  (let [parts     (str/split text #"\s")
        full-name (second parts)]
    (if-let [repo (gh/repo full-name)]
      (let [{repo-url  :html_url
             forks     :forks_count
             watchers  :watchers_count
             stars     :stargazers_count
             owner     :owner
             repo-name :name} repo
            {owner-url  :html_url
             owner-name :login} owner]
        (str "[" repo-name "](" repo-url ") by _" owner-name "_ — "
             (or stars 0) " stars and "
             (or forks 0) " forks"))
      (str "Repo " full-name " not found"))))

(defn repo-inline [{repo-url :html_url
                    forks    :forks_count
                    watchers :watchers_count
                    stars    :stargazers_count
                    :as      repo}]
  {:type                  "article"
   :id                    (str (:id repo))
   :title                 (:full_name repo)
   :thumb_url             (-> repo :owner :avatar_url)
   :description           (or (:description repo) "")
   :input_message_content {:message_text
                           (str repo-url " "
                                (or forks 0) " forks and "
                                (or stars 0) " stars")}})

(defn start [{{id :id :as chat} :chat}]
  (log/debug "Bot joined new chat: " chat)
  (t/send-text (cfg/get :telegram-token)
               id "Welcome to octolook!"))

(defn help [{{id :id :as chat} :chat}]
  (log/debug "Help was requested in " chat)
  (t/send-text (cfg/get :telegram-token)
               id "Help is on the way"))

(defn repo [{{id :id} :chat text :text}]
  (t/send-text (cfg/get :telegram-token)
               id {:parse_mode "Markdown"}
               (repo-info text)))


(defn inline [{:keys [id query offset] :as data}]
  (let [token (cfg/get :telegram-token)
        parts (str/split (or query "") #"/")]
    (if (> (count parts) 1)

      ; user entered — owner/name
      (let [[user-name repo-prefix] parts
            repos (gh/user-repos user-name repo-prefix)
            total (count repos)]
        (log/debug "Found " total " repos for " user-name "/" repo-prefix)
        (when (> total 0)
          (t/answer-inline token id (map repo-inline repos))))

      ; user entered — repo-name
      (let [repo-name (first parts)
            repos     (gh/search-repo repo-name)
            total     (count repos)]
        (log/debug "Found " total " repos for " repo-name)
        (when (> total 0)
          (t/answer-inline token id (map repo-inline repos))))))
  :done)