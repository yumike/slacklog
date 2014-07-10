(ns slacklog.views.channels
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [hiccup.core :refer [h]]
            [slacklog.views.layout :as layout]
            [slacklog.utils :as utils]
            [slacklog.utils.messages :as utils.messages]))

(defn channels-block [channels {current-name :name}]
  [:ul.channels
   (for [{:keys [name]} channels]
     [:li (if (= name current-name)
            name
            [:a {:href (str "/" name)} name])])])

(defn messages-block [messages users-map channels-map]
  [:ul.messages
   (for [{:keys [user-name text date] :as message} messages]
     [:li.clearfix.message
      [:div.message__username user-name]
      [:div.message__text
       [:span.message__date (utils/to-message-time date)]
       (utils.messages/format text users-map channels-map)]])])

(defn message-groups-block [message-groups users-map channels-map]
  [:ul.message-groups
   (for [messages message-groups]
     [:li
      [:strong.message-groups__day
       (f/unparse (f/with-zone (f/formatters :year-month-day) utils/msk-time-zone)
                  (c/from-sql-time (:date (first messages))))]
      (messages-block messages users-map channels-map)])])

(defn to-message-groups [messages]
  (partition-by
    #(let [date (t/to-time-zone (c/from-sql-time (:date %)) utils/msk-time-zone)]
       (t/date-time (t/year date) (t/month date) (t/day date)))
    messages))

(defn show [& {:keys [users channels current-channel messages]}]
  (layout/default
    :main [(channels-block channels current-channel)
           (message-groups-block
             (to-message-groups messages)
             (zipmap (map :sid users) users)
             (zipmap (map :sid channels) channels))]))
