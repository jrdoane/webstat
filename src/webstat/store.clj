(ns webstat.store)

(defonce tracked-endpoints (atom {}))
(defonce server-status (atom {}))

(defn update-status!
  [group endpoint status-map]
  (swap! server-status assoc-in [group endpoint status-map]))

(defn add-tracked-endpoint [group endpoint]
  (swap! tracked-endpoints update-in [group] #(conj % endpoint)))
