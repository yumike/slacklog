(ns slacklog.css.core
  (:require [garden.core :as garden]
            [slacklog.css.messages :as messages]))

(def styles
  (garden/css messages/styles))
