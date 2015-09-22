(defproject sensor-service "0.1.0-SNAPSHOT"
  :description "Service to handle sensor data"
  :url "https://github.com/JanneLindberg/sensor-service"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [http-kit "2.1.16"]
                 [org.clojure/data.json "0.2.6"]
                 [ring "1.4.0"]
                 [compojure "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.7"]
                 ]

  :profiles {:uberjar {:main sensor-service.core :aot :all}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}}
  )
