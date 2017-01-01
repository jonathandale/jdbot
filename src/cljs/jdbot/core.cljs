(ns jdbot.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [jdbot.events]
            [jdbot.subs]
            [jdbot.views :as views]
            [jdbot.config :as config]
            [cljs.core.async :refer [<! timeout]]
            [cljs-time.core :as time]
            [cljs-time.format :as format]
            [cuerdas.core :as str]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (reagent/render [views/main]
                  (.getElementById js/document "app")))

(defn initial-message []
  (let [time-of-day #(let [t (time/hour (time/time-now))]
                      (cond
                        (< t 12) "morning"
                        (< t 18) "afternoon"
                        :else "evening"))]
    (go
      (<! (timeout 1000))
      (re-frame/dispatch [:message {:body ["Hello!" (str "Hope your " (time-of-day) "'s going well.")]} :bot]))))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root)
  (initial-message))
