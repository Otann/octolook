(ns octolook.telegram
  (:require [clojure.string :as str]
            [octolook.github :as gh]
            [morse.api :as t]
            [taoensso.timbre :as log]))

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
  (let [parts (str/split (or query "") #"/")]
    (if (> (count parts) 1)
      (let [[user-name repo-prefix] parts
            repos (gh/user-repos user-name repo-prefix)
            total (count repos)]
        (println "Found " total " repos for " user-name "/" repo-prefix)
        (when (> total 0)
          (t/answer-inline token id (map repo-inline repos))))
      (let [repo-name (first parts)
            repos (search-repo repo-name)
            total (count repos)]
        (println "Found " total " repos for " repo-name)
        (when (> total 0)
          (t/answer-inline token id (map repo-inline repos))))))
  :done)