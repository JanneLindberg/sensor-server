(ns sensor-service.data-test
  (:require [clojure.test :refer :all]
            [sensor-service.data :refer :all]))


(deftest data-test
  (testing "Basic data store"
    (init-storage)

    (let [entry-1 {:description "desc" :value "123"}
          entry-2 {:description "desc" :value "543" :timestamp "9876"}
          ]
      (put-data "entry-1" entry-1)

      (is (= "123" (:value (get-data "entry-1" ))))

      (is (= "123" (:value (get-data "entry-1" ))))

      (is (nil? (get-data "entry-2" )))

      (put-data "entry-2" entry-2)

      (is (= 2 (count (get-sensor-ids))))

      ))


  (testing "Timestamp"
    (init-storage)

    (let [
          entry-2 {:description "desc" :value "543" :timestamp "9876"}
          ]

      (put-data "entry-2" entry-2)

      (is (= "543" (:value (get-data "entry-2" ))))

      (is (= "9876" (:timestamp (get-data "entry-2" ))))

    )
  ))


