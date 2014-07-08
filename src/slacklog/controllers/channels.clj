(ns slacklog.controllers.channels
  (:require [korma.core :refer [select where with fields order]]
            [slacklog.db :as db]
            [slacklog.views.channels :as view]))

(defn show [channel-name]
  (let [channels (select db/channels (order :name))
        current-channel (first (select db/channels (where {:name channel-name})))]
    (when current-channel
      (view/show
        :channels channels
        :current-channel current-channel
        :messages (select db/messages
                          (with db/users
                            (fields [:name :user-name]))
                          (where {:channel_id (current-channel :id)})
                          (where "(hidden is null or not hidden)")
                          (order :date))))))
