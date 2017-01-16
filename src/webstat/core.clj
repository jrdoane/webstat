(ns webstat.core
  (:require
    [webstat.http-server :refer [start-server]]
    [webstat.store :as store])
  (:gen-class))

(def test-endpoints
  {"TechPowerUp"
   ["https://www.techpowerup.com/"
    "https://www.techpowerup.com/forums/"
    "http://www.generalnonsense.com/"]})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (reset! store/tracked-endpoints test-endpoints)
  (start-server {:port 8080}))
