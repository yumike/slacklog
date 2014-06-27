(ns slacklog.db
  (:require [environ.core :refer [env]]
            [korma.db :refer [defdb postgres]]
            [korma.core :refer [defentity select insert update where aggregate
                                belongs-to set-fields values order]]))

(defdb db
  (postgres
    {:db   (env :slacklog-db-name)
     :user (env :slacklog-db-user)}))

(defentity users)
(defentity channels)

(defentity messages
  (belongs-to users {:fk :user_id}))

(defn update-or-create [ent form defaults]
  (if-let [obj (first (select ent (where form)))]
    (if-not (empty? defaults)
      (update ent (set-fields defaults) (where (select-keys obj [:id])))
      obj)
    (insert ent (values (merge form defaults)))))

(defn select-channels []
  (select channels (order :name)))

(defn get-channel [form]
  (first (select channels (where form))))

(defn get-latest-message-date [channel-id]
  (-> (select messages
          (aggregate (max :date) :max_date)
          (where {:channel_id channel-id}))
      first
      :max_date))

(defn get-user-id-map []
  (into {} (map (fn [u] [(:sid u) (:id u)]) (select users))))

(defn create-message [message]
  (insert messages (values message)))

(defn select-messages [channel-id]
  (select messages (where {:channel_id channel-id}) (order :date)))
