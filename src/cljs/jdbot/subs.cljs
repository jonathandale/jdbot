(ns jdbot.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :dialogue
 (fn [db]
   (:dialogue db)))

(re-frame/reg-sub
 :input
 (fn [db]
   (:input db)))
