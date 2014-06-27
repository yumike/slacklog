(ns slacklog.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [slacklog.worker :as worker]
            [slacklog.routes :refer [routes]])
  (:gen-class))

(def handler
  (wrap-defaults routes site-defaults))

(defn -main [& args]
  (worker/start)
  (jetty/run-jetty handler {:port 3000}))
