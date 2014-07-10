(ns slacklog.handlers.channels
  (:require [korma.core :refer [select where with fields order]]
            [slacklog.db :as db]
            [slacklog.views.channels :as view]))

(defn show [channel-name]
  (let [users (select db/users)
        channels (select db/channels (order :name))
        current-channel (first (select db/channels (where {:name channel-name})))]
    (when current-channel
      (view/show
        :users users
        :channels channels
        :current-channel current-channel
        :messages (select db/messages
                          (with db/users
                            (fields [:name :user-name]))
                          (where {:channel_id (current-channel :id)
                                  :text [not= nil]})
                          (where "(hidden is null or not hidden)")
                          (order :date))))))
