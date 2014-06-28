(ns slacklog.controllers.utils
  (:require [ring.util.response :as ring]
            [slacklog.css.core :refer [styles]]
            [slacklog.views.layout :as view]))

(defn css []
  {:headers {"Content-Type" "text/css"}
   :body styles})

(defn not-found []
  (ring/not-found (view/not-found)))
