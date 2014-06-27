(ns slacklog.worker
  (:require [clojure.tools.logging :as log]
            [korma.db :refer [transaction]]
            [slacklog.api :as api]
            [slacklog.db :as db]
            [slacklog.utils :as utils]))

(defn sync-users []
  (doseq [api-user (api/fetch-users)]
    (db/update-or-create
      db/users
      (select-keys api-user [:sid])
      api-user)))

(defn sync-channels []
  (doseq [api-channel (api/fetch-channels)]
    (db/update-or-create
      db/channels
      (select-keys api-channel [:sid])
      api-channel)))

(defn sync-messages []
  (let [user-id-map (db/get-user-id-map)]
    (doseq [channel (db/select-channels)]
      (let [latest (db/get-latest-message-date (:id channel))
            latest-str (when latest (utils/timestamp-to-string latest))]
        (log/info "channel:" channel "latest:" latest-str)
        (transaction
          (doseq [api-message (api/fetch-messages (:sid channel) latest-str)]
            (db/create-message {:channel_id (:id channel)
                                :user_id (user-id-map (:user api-message))
                                :type (:type api-message)
                                :subtype (:subtype api-message)
                                :hidden (:hidden api-message)
                                :date (utils/string-to-timestamp (:date api-message))
                                :text (:text api-message)})))))))

(defn start []
  (log/info "Starting worker")
  (future
    (loop []
      (log/info "Syncing users")
      (sync-users)

      (log/info "Syncing channels")
      (sync-channels)

      (log/info "Syncing messages")
      (sync-messages)

      (log/info "Going to sleep")
      (Thread/sleep (-> 5 (* 60) (* 1000)))
      (recur))))
