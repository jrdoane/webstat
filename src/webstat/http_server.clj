(ns webstat.http-server
  (:require
    [webstat.store :as store]
    [webstat.http-client :refer [http-check start-checker!]]
    [hiccup.core :refer [html]]
    [org.httpkit.server :as http]
    [clj-time.core :as t]))

(def bootstrap-head
  (list
    [:link {:rel "stylesheet"
            :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
            :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
            :crossorigin "anonymous"}]
    [:script
     {:src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
      :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
      :crossorigin "anonymous"}]))

(defn page
  [options]
  (html
    [:html
     [:head
      bootstrap-head
      [:title "WebStat!"]]
     [:body (:body options)]]))

(defn status-page
  []
  (page
    {:body
     [:div.container
      [:h1 "WebStat"]
       [:p "Is everything up and running smoothly?"]
      [:div.container-fluid
       (for [[section-title endpoint-list]
             (deref store/tracked-endpoints)]
       [:div.panel.panel-primary
        [:div.panel-heading
         [:h3.panel-title section-title]]
        [:table.table.table-condensed
         [:thead
          [:tr 
           (for [i ["Endpoint" "HTTP Status"
                    "Latency (ms)" "Last Updated"]]
             [:th i])]]
         [:tbody
          (for [ep endpoint-list]
            (let [check (get-in @store/server-status
                                [section-title ep])]
              [:tr
               [:td [:a {:href ep} ep]]
               [:td 
                {:class (if (= 200 (:http-code check))
                          "success" "danger")}
                (if (:http-code check)
                  (:http-code check)
                  "Down")]
               [:td (:total-time check)]
               [:td (t/in-seconds
                     (t/interval
                      (:time-start check) (t/now))) " seconds ago."]]))]]])]]}))

(defn handler
  [request]
  {:body (status-page)
   :status 200})

(defn start-server
  [options]
  (let [checker-fn (start-checker! 60000)
        http-fn (http/run-server #'handler options)]
    (fn [] (checker-fn) (http-fn) nil)))


(comment
  
  (def shutdown-fn (start-server {:port 8080}))

  )
