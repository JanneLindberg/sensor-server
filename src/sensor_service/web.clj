(ns sensor-service.web
  "Web part to service browser requests"
  (:gen-class)
  (:use
   org.httpkit.server
   )
  (:require
   [clojure.data.json :as json]
   [org.httpkit.server :as ws]
   [sensor-service.data :refer [->vector]]
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

