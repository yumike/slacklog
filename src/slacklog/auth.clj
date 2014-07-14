(ns slacklog.auth
  (:require [ring.util.request :refer [path-info]]
            [environ.core :refer [env]]
            [cemerick.friend :as friend]
            [cemerick.friend.openid :as openid]))

(def allow-anon?
  (= (env :slacklog-auth-anon) "true"))

(defn wrap-xrds [handler]
  (fn [{:keys [params] :as request}]
    (handler
      (if (and (= (path-info request) "/openid")
               (not (contains? params "openid.return_to")))
        (-> request
            (assoc-in [:request-method] :post)
            (assoc-in [:params "identifier"] "http://id.trilandev.com/openid/xrds/"))
        request))))

(defn wrap-auth [handler]
  (-> handler
      (friend/authenticate {:allow-anon? allow-anon?
                            :login-uri "/openid"
                            :workflows [(openid/workflow :credential-fn identity)]})
      (wrap-xrds)))
