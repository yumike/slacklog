(ns slacklog.utils.messages
  (:refer-clojure :exclude [format])
  (:require [clojure.string :as string]))

(defn tag-strong [text]
  (str "<strong>" text "</strong>"))

(defn tag-a [href title]
  (str "<a href='" href "'>" title "</a>"))

(defn format-mention [prefix sid names-map]
  (tag-strong (str prefix (get (names-map sid) :name sid))))

(defn format-link [link users-map channels-map]
  (let [[link title] (string/split link #"\|")]
    (cond
      (.startsWith link "#C") (format-mention "#" (subs link 1) channels-map)
      (.startsWith link "@U") (format-mention "@" (subs link 1) users-map)
      (.startsWith link "!")  (tag-strong link)
      :else (tag-a link (or title link)))))

(defn format [text users-map channels-map]
  (-> text
      (string/replace #"<(.*?)>" (fn [[_ link]] (format-link link users-map channels-map)))
      (string/replace "\n" "<br>")))
