(ns jdbot.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :as re-frame]
            [jdbot.db :as db]
            [jdbot.rivescript :as rs]
            [cuerdas.core :as str]
            [cljs.reader :as reader]
            [jdbot.fuzzy :refer [search]]
            [cljs.core.async :refer [<! timeout]]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :input-change
 (fn [db [_ text]]
   (assoc db :input text)))

(re-frame/reg-event-db
 :message
 (fn [db [_ message owner]]
   (update-in db [:dialogue] conj {:message message :owner owner})))

(re-frame/reg-event-db
 :visitor-message
 (fn [db [_ message]]
   (let [wait #(+ 750 (rand-int 1000))
         result (first (search message))]
     (go
       (<! (timeout (wait)))
       (rs/get-reply (if result (keyword (get-in result [:item :command])) message)
         (fn [error reply]
           (let [msg (if-not error (reader/read-string reply) {:body ["Something went awry."]})]
             (re-frame/dispatch [:message msg :bot]))))))
   (-> db
     (update-in [:dialogue] conj {:message {:body message} :owner :visitor})
     (assoc :input nil))))
