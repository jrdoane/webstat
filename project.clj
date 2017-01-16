(defproject io.doane/webstat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [http-kit "2.2.0"]
                 [clj-time "0.13.0"]
                 [hiccup "1.0.5"]
                 [manifold "0.1.5"]]
  :main ^:skip-aot webstat.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
