(ns slacklog.utils
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clojure.string :as string]))

(def msk-time-zone
  (t/time-zone-for-offset 4))

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

(defn to-message-time [timestamp]
  (f/unparse (f/with-zone (f/formatters :hour-minute) msk-time-zone)
             (c/from-sql-time timestamp)))

(defn message-day [{:keys [date]}]
  (t/to-time-zone (c/from-sql-time date) msk-time-zone))
