(ns slacklog.css.messages)

(def styles
  [[:.messages
    {:list-style-type 'none
     :margin "0"
     :padding "0"}]

   [:.message
    [:&__username
     {:font-weight "bold"
      :text-align "right"}]
    [:&__date
     {:float "right"}]]])
