(ns slacklog.utils
  (:require [clojure.string :as string]))

(defn string-to-long [long-str]
  (read-string (string/replace-first long-str #"^0+" "")))

(defn long-to-timestamp [sec]
  (new java.sql.Timestamp (* sec 1000)))

(defn string-to-timestamp [timestamp-str]
  (let [[sec-str microsec-str] (string/split timestamp-str #"\." 2)
        sec       (string-to-long sec-str)
        microsec  (if microsec-str (string-to-long microsec-str) 0)
        nanosec   (* microsec 1000)
        timestamp (new java.sql.Timestamp (* sec 1000))]
    (do
      (.setNanos timestamp nanosec)
      timestamp)))

(defn timestamp-to-string [timestamp]
  (str
    (quot (.getTime timestamp) 1000)
    "."
    (quot (.getNanos timestamp) 1000)))
