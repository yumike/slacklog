(ns slacklog.routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [slacklog.controllers.channels :as channels]))


(defroutes routes
  (GET "/" [] (channels/index))
  (GET "/:channel-name" [channel-name] (channels/show channel-name))
  (route/not-found "Not Found"))
