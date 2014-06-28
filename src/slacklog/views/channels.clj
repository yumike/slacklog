(ns slacklog.views.channels
  (:require [hiccup.core :refer [h]]
            [slacklog.views.layout :as layout]))

(defn message-block [message]
  [:li.message
   [:div.col-md-2.message__username (message :user.name)]
   [:div.col-md-10
    [:div.text-muted.message__date (message :date)]
    [:div.message__text (h (message :text))]]])

(defn index [& {:keys [channels]}]
  (layout/default
    :main (for [{:keys [id name]} channels]
            [:div [:a {:href (str "/" name)} name]])))

(defn show [& {:keys [channel messages]}]
  (layout/default
    :main [[:h1 (channel :name)]
           [:ul.messages (map message-block messages)]]))
