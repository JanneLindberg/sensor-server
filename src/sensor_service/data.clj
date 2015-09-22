(ns sensor-service.data
  "Simple in-memory store for the sensor data"
  (:gen-class)
)


(def ^{:private true} sensor-data (atom {}))

(defn init-storage []
  (reset! sensor-data {}))

(defn- add-reg-data [pno entry]
  (if-not (:timestamp entry)
    (swap! sensor-data conj { pno (merge entry {:timestamp (str (System/currentTimeMillis))})})
    (swap! sensor-data conj { pno entry })))

(defn put-data [id body]
  (add-reg-data (keyword id) body))

(defn get-data [id]
  ((keyword id) @sensor-data)
  )

(defn get-sensor-ids
  "Get a list of the registered sensor ids"
  []
  (map #(name %) (keys @sensor-data)))

(defn ->vector
  "Get the sensor data as a vector"
  []
  (into [] (map #(get-data %) (get-sensor-ids))))

