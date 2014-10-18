(ns slacklog.db.migrations
  (:require [clojure.java.io :as io])
  (:import (java.net JarURLConnection)))

(defn get-jar-filenames [jar-file]
  (map #(.getName %)
       (filter #(not (.isDirectory %))
               (enumeration-seq (.entries jar-file)))))

(defn get-jar-migrations [url]
  (let [^JarURLConnection conn (.openConnection url)
        dir-name (.getName (.getJarEntry conn))]
    (map io/resource
         (filter #(.startsWith % dir-name)
                 (get-jar-filenames (.getJarFile conn))))))

(defn get-file-migrations [url]
  (map io/as-url (.listFiles (io/as-file url))))

(defn get-migrations [path]
  (let [url (io/resource path)]
    (case (.getProtocol url)
      "file" (get-file-migrations url)
      "jar" (get-jar-migrations url))))
