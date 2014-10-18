(ns slacklog.core
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [slacklog.auth :refer [wrap-auth]]
            [slacklog.db :as db]
            [slacklog.worker :as worker]
            [slacklog.routes :refer [routes]])
  (:gen-class))

(def handler
  (-> routes
      (wrap-auth)
      (wrap-defaults site-defaults)))

(defn -main [& args]
  (log/info (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))
  (log/info "Database name:" db/db-name)
  (log/info "Database user:" db/db-user)

  (worker/start)
  (run-server handler {:port 3000}))
