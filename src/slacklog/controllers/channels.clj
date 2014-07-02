(ns slacklog.controllers.channels
  (:require [korma.core :refer [select where with fields order]]
            [slacklog.db :refer [users channels messages]]
            [slacklog.views.channels :as view]
            [slacklog.controllers.utils :refer [not-found]]))

(defn index []
  (view/index :channels (select channels (order :name))))

(defn show [channel-name]
  (if-let [channel (first (select channels (where {:name channel-name})))]
    (let [channel-messages (select messages
                                   (with users
                                     (fields [:name :user.name]))
                                   (where {:channel_id (channel :id)})
                                   (where "(hidden is null or not hidden)")
                                   (order :date))]
      (view/show :channel channel :messages channel-messages))))
