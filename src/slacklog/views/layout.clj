(ns slacklog.views.layout
  (:require [hiccup.page :as page]))

(defn default [& {:keys [title main] :or {title "Slack Log"}}]
  (page/html5
    [:head
     [:title title]
     [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"}]
     [:link {:rel "stylesheet" :href "/static/application.css"}]]
    [:body
     [:div.container (seq main)]]))

(defn not-found []
  (page/html5
    [:head
     [:title "Page not found"]]
    [:body "Page not found"]))
