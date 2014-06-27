(ns slacklog.controllers.utils
  (:require [ring.util.response :as ring]
            [slacklog.views.layout :as view]))

(defn not-found []
  (ring/not-found (view/not-found)))
