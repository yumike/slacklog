(ns slacklog.views.channels
  (:require [slacklog.views.layout :as layout]
            [slacklog.util :as util]
            [slacklog.util.messages :as util.messages]))

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
       [:span.message__date (util/timestamp->time-string date)]
       (util.messages/format text users-map channels-map)]])])

(defn message-groups-block [message-groups users-map channels-map]
  [:ul.message-groups
   (for [messages message-groups]
     [:li
      [:strong.message-groups__day
       (util/timestamp->date-string (:date (first messages)))]
      (messages-block messages users-map channels-map)])])

(defn to-message-groups [messages]
  (partition-by #(util/timestamp->date (:date %)) messages))

(defn show [& {:keys [users channels current-channel messages]}]
  (layout/default
    :main [(channels-block channels current-channel)
           (message-groups-block
             (to-message-groups messages)
             (zipmap (map :sid users) users)
             (zipmap (map :sid channels) channels))]))
