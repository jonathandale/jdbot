(ns jdbot.fuzzy
  (:require [cljsjs.fuse]))

(def candidates {:projects ["Do you have any projects to show?"
                            "What have you been working on?"
                            "What projects have you been working on?"
                            "Worked on anything interesting?"
                            "Do you have a portfolio?"
                            "Do you have any code samples?"]
                 :work ["Where do you work?"
                        "Where do you work now?"
                        "What do you do for a living?"
                        "What do you do?"]
                 :typezero ["tell me more about Type Zero"
                            "What do you do at type zero"]
                 :about ["about"
                         "Tell me about yourself"]})

(def options {:shouldSort true
              :threshold 0.4
              :include ["score"]
              ; :location 0
              ; :distance 100
              :maxPatternLength 32
              ; :minMatchCharLength 100
              :keys [{:name :question
                      :weight 0.4}
                     {:name :command
                      :weight 0.6}]})

(defonce fuse (js/Fuse. (clj->js (flatten (map (fn [[c qs]]
                                                 (map #(assoc {} :question % :command c) qs)) candidates)))
                        (clj->js options)))

(defn search [term]
  (js->clj (.search fuse (first term)) :keywordize-keys true))
