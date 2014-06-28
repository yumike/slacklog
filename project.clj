(defproject slacklog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.0"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [postgresql "9.3-1101.jdbc4"]
                 [ragtime/ragtime.sql.files "0.3.6"]
                 [korma "0.3.2"]
                 [environ "0.5.0"]
                 [ring/ring-core "1.3.0"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [ring/ring-defaults "0.1.0"]
                 [compojure "1.1.8"]
                 [garden "1.1.8"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.16"]
                 [clj-time "0.7.0"]]
  :plugins [[ragtime/ragtime.lein "0.3.6"]
            [lein-ring "0.8.11"]]
  :ragtime {:migrations ragtime.sql.files/migrations
            :database "jdbc:postgresql://localhost:5432/slacklog-clj?user=yumike"}
  :ring {:handler slacklog.core/handler}
  :jvm-opts ["-Djava.awt.headless=true"]
  :main ^:skip-aot slacklog.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
