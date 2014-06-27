(ns slacklog.core
  (:require [ring.adapter.jetty :as jetty]
            [slacklog.worker :as worker]
            [slacklog.routes :refer [routes]])
  (:gen-class))

(defn -main [& args]
  (worker/start)
  (jetty/run-jetty routes {:port 3000}))
