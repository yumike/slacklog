(ns slacklog.routes
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [slacklog.controllers.channels :as channels]
            [slacklog.controllers.utils :as utils]))


(defroutes routes
  (GET "/" [] (channels/index))
  (GET "/:channel-name" [channel-name] (channels/show channel-name))
  (GET "/static/application.css" [] (utils/css))
  (route/not-found "Not Found"))
