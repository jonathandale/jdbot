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

(def answers {:work {:body ["I work at Type Zero"]
                     :commands ["More about Type Zero"
                                "Past work"]}
              :typezero {:body ["I am a software dev & designer at T0"]}
              :projects {:body ["At work, I've been working on some AWS lambda functions using Python, lots
                                 of mobile UI design, and some tooling for site generation using gulpjs."
                                "As far as projects to see, here are some you can choose from"]
                         :commands ["Crowd Reviewed" "Weather clock" "Code Jobs"]}
              :help {:body ["coming soon"
                            "I know, super useful, right?"]}
              :about {:body ["about Jon" "blah blah"]}})

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

; (doseq [[c _] answers]
;   (.setSubroutine rs (name c)
;     #(c answers)))

(.setSubroutine rs "catchAllAsync"
  (fn [rs* args]
    (let [promise (aget rs* "Promise")]
      (new js/promise
        (fn [resolve reject]
          ; (if-let [result (first (search args))]
          ;   (do
          ;     (prn result)
          ;     (resolve (answers (keyword (get-in result [:item :command])))))
            (resolve {:body ["Sorry, didn't quite understand that, try \"help\""]}))))))

          ; (GET "https://api.github.com/users/jonathandale"
          ;   {:handler #(resolve %)
          ;    :error-handler #(reject %)
          ;    :response-format :json
          ;    :keywords? true}))))))

(load-files)
