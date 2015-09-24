(ns sensor-service.web
  "Web part to service browser requests"
  (:gen-class)
  (:use
   org.httpkit.server
   )
  (:require
   [clojure.data.json :as json]
   [org.httpkit.server :as ws]
   [hiccup.core :as h]
   [hiccup.page :as page]
   [hiccup.util :as util]
   [hiccup.page :refer [html5 include-js include-css]]
   [sensor-service.data :as data]
   [sensor-service.data :refer [->vector get-sensor-ids]]
   )
  )


(def ^{:private true} clients (atom {}))


(defn get-clients []
  @clients)


(defn send-to-clients [msg]
  (doseq [client @clients]
    (send! (key client) (json/json-str msg)
           false)))


(defn handle-websocket [req]
  (ws/with-channel req con
    (println "Connection from:" con)
    (swap! clients assoc con true)
    (send-to-clients (sensor-service.data/->vector))

    (ws/on-receive con (fn [xxx]))

    (ws/on-close con (fn [status]
                       (swap! clients dissoc con)
                       (println con "disconnected:" (name status))))))


(defn generate-dev-divs [enclosing-div]
  (let [res (atom enclosing-div)]
    (doseq [id (data/get-sensor-ids)]
      (swap! res conj [:div.show_value {:id id} ""]))
    @res))



(defn index-page []
  "Main index page"
  (page/html5
   [:head
    [:title ""]
    (page/include-css "/css/style.css")
    (page/include-js "/js/script.js")
    ]
   [:body
    [:div {:id "header"} ""]
    (generate-dev-divs [:div])
    ]))
