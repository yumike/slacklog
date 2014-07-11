(ns slacklog.util
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clojure.string :as string]))

(def msk-time-zone
  (t/time-zone-for-offset 4))

(defn string->long [long-str]
  (read-string (string/replace-first long-str #"^0+" "")))

(defn long->timestamp [sec]
  (new java.sql.Timestamp (* sec 1000)))

(defn string->timestamp [timestamp-str]
  (let [[sec-str microsec-str] (string/split timestamp-str #"\." 2)
        sec       (string->long sec-str)
        microsec  (if microsec-str (string->long microsec-str) 0)
        nanosec   (* microsec 1000)
        timestamp (new java.sql.Timestamp (* sec 1000))]
    (do
      (.setNanos timestamp nanosec)
      timestamp)))

(defn timestamp->string [timestamp]
  (str
    (quot (.getTime timestamp) 1000)
    "."
    (quot (.getNanos timestamp) 1000)))

(defn timestamp->time-string [timestamp]
  (f/unparse (f/with-zone (f/formatters :hour-minute) msk-time-zone)
             (c/from-sql-time timestamp)))
