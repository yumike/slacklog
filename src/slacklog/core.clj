(ns slacklog.core
  (:require [org.httpkit.server :refer [run-server]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [slacklog.auth :refer [wrap-auth]]
            [slacklog.worker :as worker]
            [slacklog.routes :refer [routes]])
  (:gen-class))

(def handler
  (-> routes
      (wrap-auth)
      (wrap-defaults site-defaults)))

(defn -main [& args]
  (worker/start)
  (run-server handler {:port 3000}))
