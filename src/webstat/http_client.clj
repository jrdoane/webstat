(ns webstat.http-client
  (:require
    [webstat.store :as store]
    [manifold.time :refer [every]]
    [org.httpkit.client :as http]
    [clj-time.core :as t]))

(defn http-check
  [url]
  (let [ts (t/now)
        resp @(http/get url)
        te (t/now)]
    {:http-response resp
     :endpoint url
     :time-start ts
     :time-end te
     :total-time (t/in-millis (t/interval ts te))
     :http-code (:status resp)
     :good? (= (:status resp) 200)
     :server-error? (= (:status resp) 500) 
     :bad-gateway? (= (:status resp) 502)
     :down? (not (nil? (:error resp)))}))

(defn refresh-check-group! [[group endpoints]]
  (doseq [endpoint endpoints]
    (swap! store/server-status assoc-in [group endpoint]
           (http-check endpoint))))

(defn refresh-all-checks! []
  (doseq [te @store/tracked-endpoints]
    (refresh-check-group! te)))

(defn start-checker!
  [freq]
  (every freq refresh-all-checks!))

