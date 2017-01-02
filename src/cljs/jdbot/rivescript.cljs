(ns jdbot.rivescript
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :as re-frame]
            [cuerdas.core :as str]
            [cljsjs.jquery]
            [cljs.core.async :refer [<! timeout]]
            [ajax.core :refer [GET]]
            [rivescript]
            [jdbot.fuzzy :refer [search]]
            [cljs.reader :as reader]))

(defonce rs (js/RiveScript.))

(defn on-success []
  (.sortReplies rs))

(defn on-error [e]
  (prn "Error" e))

(defn load-files []
  (.loadFile rs (clj->js ["brain/begin.rive"
                          "brain/myself.rive"
                          "brain/javascript.rive"
                          "brain/star.rive"])
                on-success
                on-error))

(defn get-reply [message cb]
  (-> rs
    (.replyAsync "visitor" message)
    (.then (fn [reply] (cb nil reply)))
    (.catch (fn [error] (cb error)))))

(.setSubroutine rs "catchAllAsync"
  (fn [rs* args]
    (let [promise (aget rs* "Promise")]
      (new js/promise
        (fn [resolve reject]
            (resolve {:body ["Sorry, didn't quite understand that, try \"help\""]}))))))
(load-files)
