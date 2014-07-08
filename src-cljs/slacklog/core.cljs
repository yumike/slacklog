(ns slacklog.core)

(defn document-ready-handler []
  (let [ready-state (.-readyState js/document)]
    (when (= ready-state "complete")
      (.scrollTo js/window 0 (-> js/document .-body .-scrollHeight)))))

(set! (.-onreadystatechange js/document) document-ready-handler)
