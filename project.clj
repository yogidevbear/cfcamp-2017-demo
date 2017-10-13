(defproject cfcamp-2017-demo "0.1.0-SNAPSHOT"
  :description "CFCamp 2017 talk code samples (Clojure: Manipulating the Immutable)"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.7.0-alpha3"]
                 [net.sourceforge.jtds/jtds "1.3.1"]
                 [proto-repl "0.3.1"]]
  :main ^:skip-aot cfcamp-2017-demo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
