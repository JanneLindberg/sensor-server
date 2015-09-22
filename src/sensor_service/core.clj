(ns sensor-service.core
  (:gen-class)
  (:use
   org.httpkit.server
   ring.middleware.params
   [ring.middleware.json :only [wrap-json-body]]
   [ring.util.response :only [response]]
   )
  (:require
   [compojure.core :refer :all ]
   [compojure.route :as route]
   [compojure.handler :as handler]
   [ring.middleware.json :refer [wrap-json-response]]
   [clojure.data.json :as json]
   [ring.middleware.reload :as reload]
   [sensor-service.web :only [handle-websocket send-to-clients]]
   [sensor-service.data :refer [put-data get-data]]
   )
  )


(def default-port 8097)

(defonce ^{:private true} server (atom nil))


(defn check-and-add-ts
  "Add a timestamp if not provided"
  [message]
  (if-not (:timestamp message)
    (merge message {:timestamp (str (System/currentTimeMillis))})
    message
    ))


(defn process-post [id body]
  (let [msg0 (check-and-add-ts body)
        dev-id (or id (:param msg0))
        msg (merge {:param dev-id} msg0)
        ]
    (put-data dev-id msg)
    (sensor-service.web/send-to-clients [msg]))
  {:status 200}
)


(defn json-response
  ([^Integer code]
   (json-response code nil))

  ([^Integer code ^String data]
   (merge
    { :status code
      :headers {"Content-Type" "application/json; charset=utf-8"
                "Access-Control-Allow-Origin" "*" }
     }
    (when-let [d data]
      { :body (str (json/write-str data))})
    )))


(defroutes app-routes
  (GET "/ws" [] sensor-service.web/handle-websocket)

  (POST "/data" {body :body} (process-post nil body))

  (context "/data/:id" [id]
           (POST "/" {body :body} (process-post id body))
           (GET "/" [] (json-response  200 (sensor-service.data/get-data id)))
           )

  (route/files "/" {:root "public"})
  (route/resources "/html/")
  (route/not-found "Not Found 404")
)


(def handler (->
              app-routes
              reload/wrap-reload
              (wrap-json-body {:keywords? true})
              wrap-json-response
              ))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))


(defn start-server [port-no]
  (reset! server (run-server #'handler {:port port-no})))


; (start-server default-port)


(defn -main [& args]
  (let [port (Integer. (or (System/getenv "PORT") default-port))]
    (println "Using port:" port)

    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (println "Shutting down...")
                                 (shutdown-agents)
                                 )))
    (start-server default-port)))
