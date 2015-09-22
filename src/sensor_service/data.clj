(ns sensor-service.data
  "Simple in-memory store for the sensor data"
  (:gen-class)
)


(def ^{:private true} sensor-data (atom {}))

(defn init-storage []
  (reset! sensor-data {}))

(defn put-data [id body]
  (swap! sensor-data conj { (keyword id) body }))

(defn get-data [id]
  ((keyword id) @sensor-data)
  )

(defn get-sensor-ids
  "Get a list of the registered sensor ids"
  []
  (map #(name %) (keys @sensor-data)))

(defn ->vector
  "Get all the sensor data as a vector"
  []
  (into [] (map #(get-data %) (get-sensor-ids))))

