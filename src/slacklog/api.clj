(ns slacklog.api
  (:require [clojure.set :refer [rename-keys]]
            [clojure.data.json :as json]
            [clj-time.coerce :as timec]
            [environ.core :refer [env]]
            [org.httpkit.client :as http]
            [slacklog.utils :as utils]))

(def token (env :slacklog-api-token))

(defn request [url params]
  (let [options {:query-params params}
        response @(http/get url options)]
    (json/read-str (:body response) :key-fn keyword)))

(defn fetch-channels []
  (map #(update-in (rename-keys
                     (select-keys % [:id :name :created])
                     {:id :sid, :created :date})
                   [:date]
                   utils/long-to-timestamp)
       (:channels (request "https://slack.com/api/channels.list" {:token token}))))

(defn fetch-users []
  (map #(rename-keys
          (select-keys % [:id :name])
          {:id :sid})
       (:members (request "https://slack.com/api/users.list" {:token token}))))

(defn fetch-messages [channel oldest]
  (letfn [(fetch-part [latest]
            (let [params {:token token, :channel channel, :latest latest, :oldest oldest}]
              (request "https://slack.com/api/channels.history"
                       (select-keys params (for [[k v] params :when v] k)))))
          (parts [latest]
            (lazy-seq
              (let [part (fetch-part latest)
                    messages (:messages part)]
                (if (:has_more part)
                  (concat messages (parts (-> messages last :ts)))
                  messages))))]
    (map #(rename-keys % {:ts :date}) (parts nil))))
