(ns webstat.http-client
  (:require
    [org.httpkit.client :as http]
    [clj-time.core :as t]))

(defn http-check
  [url]
  (let [ts (t/now)
        resp @(http/get url)
        te (t/now)]
    {:http-response resp
     :time-start ts
     :time-end te
     :total-time (t/in-millis (t/interval ts te))
     :http-code (:status resp)
     :good? (= (:status resp) 200)
     :server-error? (= (:status resp) 500) 
     :bad-gateway? (= (:status resp) 502)
     :down? (not (nil? (:error resp)))}))

