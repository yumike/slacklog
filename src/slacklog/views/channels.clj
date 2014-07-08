(ns slacklog.views.channels
  (:require [hiccup.core :refer [h]]
            [slacklog.views.layout :as layout]))

(defn channels-block [channels {current-name :name}]
  [:ul.col-xs-3.channels
   (for [{:keys [name]} channels]
     [:li (if (= name current-name)
            name
            [:a {:href (str "/" name)} name])])])

(defn messages-block [messages]
  [:ul.col-xs-9.col-xs-offset-3.messages
   (for [{:keys [user-name text date]} messages]
     [:li
      [:strong user-name ": "]
      (h text)])])

(defn show [& {:keys [channels current-channel messages]}]
  (layout/default
    :main [(channels-block channels current-channel)
           (messages-block messages)]))
