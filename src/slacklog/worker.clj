(ns slacklog.worker
  (:require [clojure.tools.logging :as log]
            [korma.db :refer [transaction]]
            [slacklog.api :as api]
            [slacklog.db :as db]
            [slacklog.util :as util]))

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
            latest-str (when latest (util/timestamp-to-string latest))]
        (transaction
          (doseq [api-message (api/fetch-messages (:sid channel) latest-str)]
            (db/create-message {:channel_id (:id channel)
                                :user_id (user-id-map (:user api-message))
                                :type (:type api-message)
                                :subtype (:subtype api-message)
                                :hidden (:hidden api-message)
                                :date (util/string->timestamp (:date api-message))
                                :text (:text api-message)})))))))

(defn start []
  (log/info "Starting worker")
  (future
    (loop []
      (try
        (log/info "Syncing users")
        (sync-users)

        (log/info "Syncing channels")
        (sync-channels)

        (log/info "Syncing messages")
        (sync-messages)

        (catch Exception e (log/error e "Error during syncing.")))

      (log/info "Going to sleep")
      (Thread/sleep (-> 5 (* 60) (* 1000)))

      (recur))))
