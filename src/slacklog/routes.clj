(ns slacklog.routes
  (:require [ring.util.response :refer [redirect]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [slacklog.controllers.channels :as channels]))

(defroutes routes
  (GET "/" []
       (redirect "/general"))

  (GET "/:channel-name" [channel-name]
       (channels/show channel-name))

  (route/not-found "Not Found"))
