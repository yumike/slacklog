(ns slacklog.views.layout
  (:require [hiccup.page :as page]))

(defn default [& {:keys [title main] :or {title "Slack Log"}}]
  (page/html5
    [:head
     [:title title]
     (page/include-css "/application.css")]
    [:body
     [:div.container (seq main)]
     (page/include-js "/application.js")]))

(defn not-found []
  (page/html5
    [:head
     [:title "Page not found"]]
    [:body "Page not found"]))
