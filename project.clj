(defproject jdbot "0.1.0-SNAPSHOT"
  :dependencies [[reagent "0.6.0" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "15.2.1-0"]
                ;  [cljsjs/react-with-addons "15.4.0-0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [org.clojure/clojure "1.8.0"]
                 [funcool/cuerdas "2.0.1"]
                 [cljsjs/jquery "2.2.4-0"]
                 [cljsjs/fuse "2.5.0-0"]
                 [ns-tracker "0.3.0"]
                 [cljs-ajax "0.5.8"]
                 [re-frame "0.8.0"]
                 [garden "1.3.2"]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-garden "0.2.8"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js" "target"
                                    "resources/public/css"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   jdbot.css/main
                     :compiler     {:output-to     "resources/public/css/main.css"
                                    :pretty-print? true}}]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.8.2"]]

    :plugins      [[lein-figwheel "0.5.7"]]}}


  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "jdbot.core/mount-root"}
     :compiler     {:main                 jdbot.core
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/out"
                    :asset-path           "js/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    :externs ["externs/rivescript.js"]
                    :foreign-libs [{:file "foreign-libs/rivescript.js"
                                    :file-min "foreign-libs/rivescript.min.js"
                                    :provides ["rivescript"]}]}}


    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            jdbot.core
                    :output-to       "resources/public/js/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false
                    :externs ["externs/rivescript.js"]
                    :foreign-libs [{:file "foreign-libs/rivescript.js"
                                    :file-min "foreign-libs/rivescript.min.js"
                                    :provides ["rivescript"]}]}}]})
