(ns jdbot.css
  (:require [garden.def :refer [defstyles]]
            [garden.units :as units]
            [garden.stylesheet :refer [at-media]]))

(def blue "rgb(58,105,204)")
(def silver "rgba(0,0,0,0.05)")
(def white "rgb(255, 255, 255)")
(def grey "rgb(153, 153, 153)")

(defstyles main
  [:body {:background-color white
          :padding 0
          :margin 0
          :-webkit-overflow-scrolling :touch}
          ; :overflow "auto !important"}
          ; :height "100% !important"}
    [:a {:color blue
         :text-decoration :none}]
    [:header {:z-index 2}
      [:li>p {:cursor :pointer}]
      [:.avatar {:width :60px
                 :transform "translate(-30px, 6px)"
                 :left "50%"}]]
    [:input {:outline :none
             :width "100%"}]
    [:.dialogue {:max-height "100%"
                 :margin-bottom :54px}]
    [:.message {:margin-top :4px
                :padding "10px 14px"
                :border-radius :5px}
      [:&.bot {:background-color silver}]
      [:&.visitor {:background-color blue
                   :color white}]]
    [:.image {:margin-top :4px}
      [:& :img {:border-radius :5px}]]
    [:.commands {:margin-top :4px
                 :border-radius :5px
                 :border-color silver
                 :border-width :1px
                 :border-style :solid}
      [:li {:border :inherit}]]]

  [:.send {:background "url(../img/send.svg) no-repeat 50% 50%"
           :background-size "50%"
           :width "100%"
           :height "100%"}]
  [:.sans-serif {:font-family "\"avenir next\", avenir, helvetica, \"helvetica neue\", ubuntu, roboto, noto, \"segoe ui\", arial, sans-serif"}]

  [:.message-enter
    [:.message-inner {:opacity 0.01}]

    [:&.message-enter-active
      [:.message-inner {:opacity 1
                        :transition "opacity 0.25s ease-out 0.05s"}]]]

  ;; BASSCSS Type of styles

  ;; Colors
  [:.blue {:color blue}]
  [:.white {:color white}]
  [:.silver {:color silver}]
  [:.grey {:color grey}]

  ;; Muted
  [:.muted-90 {:opacity 0.9}]
  [:.muted-80 {:opacity 0.8}]
  [:.muted-70 {:opacity 0.7}]

  ;; Border-colors
  [:.bdr-silver {:border-color silver}]
  [:.bdr-grey {:border-color grey}]

  ;; Background-colors
  [:.bg-blue {:background-color blue}]
  [:.bg-grey {:background-color grey}]
  [:.bg-silver {:background-color silver}]
  [:.bg-white {:background-color white}]
  [:.bg-white-90 {:background-color "rgba(255, 255, 255, 0.9)"}]
  [:.bg-white-80 {:background-color "rgba(255, 255, 255, 0.8)"}]

  ;; Misc
  [:.pointer {:cursor :pointer}]

  ;; Responsive
  (at-media {:min-width (units/rem 50)}
            [:.send {:margin-right (units/rem 1)}]))
