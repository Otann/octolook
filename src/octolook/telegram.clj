(ns octolook.telegram
  (:require [clojure.string :as str]
            [octolook.github :as gh]
            [morse.api :as t]
            [morse.handlers :as h]
            [taoensso.timbre :as log]
            [omniconf.core :as cfg]))

(defn repo-info [text]
  (let [parts     (str/split text #"\s")
        repo-name (second parts)]
    (if-let [repo (gh/repo repo-name)]
      (let [{repo-url :html_url
             forks    :forks_count
             watchers :watchers_count
             stars    :stargazers_count
             owner    :owner} repo
            {owner-url  :html_url
             owner-name :login} owner]
        (str "[*" name "*](" repo-url ") by _" owner-name "_ — "
             (or forks 0) " forks and "
             (or stars 0) " stars"))
      (str "Repo " repo-name " not found"))))

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

(defn inline [{:keys [id query offset] :as data}]
  (let [token (cfg/get :telegram-token)
        parts (str/split (or query "") #"/")]
    (if (> (count parts) 1)
      (let [[user-name repo-prefix] parts
            repos (gh/user-repos user-name repo-prefix)
            total (count repos)]
        (println "Found " total " repos for " user-name "/" repo-prefix)
        (when (> total 0)
          (t/answer-inline token id (map repo-inline repos))))
      (let [repo-name (first parts)
            repos     (gh/search-repo repo-name)
            total     (count repos)]
        (println "Found " total " repos for " repo-name)
        (when (> total 0)
          (t/answer-inline token id (map repo-inline repos))))))
  :done)

(h/defhandler handler

  (h/command-fn "start"
    (fn [{{id :id :as chat} :chat}]
      (println "Bot joined new chat: " chat)
      (t/send-text (cfg/get :telegram-token)
                   id "Welcome to octolook!")))

  (h/command-fn "help"
    (fn [{{id :id :as chat} :chat}]
      (println "Help was requested in " chat)
      (t/send-text (cfg/get :telegram-token)
                   id "Help is on the way")))

  (h/command-fn "repo"
    (fn [{{id :id} :chat text :text}]
      (t/send-text (cfg/get :telegram-token)
                   id {:parse_mode               "Markdown"
                       :disable_web_page_preview true}
                   (repo-info text))))

  (h/inline-fn
    (fn [{:keys [id query offset] :as data}]
      (inline data)))

  (h/message-fn (fn [message] (println "Unhandled 2: " message))))