(ns jdbot.fuzzy
  (:require [cljsjs.fuse]))

(def candidates {:projects ["What have you been working on?"
                            "What projects have you been working on?"
                            "Worked on anything interesting?"
                            "Do you have a portfolio?"
                            "Do you have any code samples?"]
                 :work ["Where do you work?"
                        "Where are you working?"
                        "Who's your employer?"]
                 :occupation ["What do you do for a living?"
                              "What do you do?"
                              "What's your job?"]
                 :typezero ["About TypeZero"
                            "Tell me about TypeZero"
                            "What do you do at TypeZero"
                            "about TypeZero"]
                 :about ["About"
                         "About you"
                         "Tell me about yourself"
                         "Tell me about you"]})


(def options {:shouldSort true
              :threshold 0.3
              :include ["score"]
              :maxPatternLength 32
              :keys [{:name :question
                      :weight 0.45}
                     {:name :command
                      :weight 0.55}]})

(defonce fuse (js/Fuse. (clj->js (flatten (map (fn [[c qs]]
                                                 (map #(assoc {} :question % :command c) qs)) candidates)))
                        (clj->js options)))

(defn search [term]
  (js->clj (.search fuse (first term)) :keywordize-keys true))
