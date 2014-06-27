(ns slacklog.views.channels
  (:require [hiccup.core :refer [h]]
            [slacklog.views.layout :as layout]))

(defn index [& {:keys [channels]}]
  (layout/default
    :main (for [{:keys [id name]} channels]
            [:div [:a {:href (str "/" name)} name]])))

(defn show [& {:keys [channel messages]}]
  (layout/default
    :main [[:h1 (channel :name)]
           [:div (for [message messages]
                   [:div (str (message :user.name) ": " (h (message :text)))])]]))
