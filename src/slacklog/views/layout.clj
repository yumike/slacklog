(ns slacklog.views.layout
  (:require [hiccup.page :as page]))

(defn default [& {:keys [title main] :or {title "Slack Log"}}]
  (page/html5
    [:head
     [:title title]]
    [:body (seq main)]))

(defn not-found []
  (page/html5
    [:head
     [:title "Page not found"]]
    [:body "Page not found"]))
