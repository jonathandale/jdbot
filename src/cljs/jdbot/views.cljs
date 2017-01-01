(ns jdbot.views
  (:require [re-frame.core :as re-frame :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            [cuerdas.core :as str]))


(def cst
  (reagent/adapt-react-class js/React.addons.CSSTransitionGroup))

(defn header []
  [:header.fixed.top-0.left-0.right-0.px1.bg-blue.white
    [:div.p2.clearfix
      [:div.col.col-6.xs-hide
        [:p.m0.p0 "Chatting with Jon Dale"]]
      [:div.col.col-6.sm-hide.md-hide.lg-hide
        [:p.m0.p0 "Jon Dale"]]
      [:img.circle.absolute.avatar {:src "img/jon.png"}]
      [:div.col.col-6.right-align
        [:ul.list-reset.m0.p0
          [:li.inline-block
            [:p.m0.p0 {:on-click #(dispatch [:visitor-message ["help"]])} "Help"]]]]]])

(defn render-command [command]
  [:li.bg-white.blue.center.bdr-silver
    [:a.block.px3.py1.pointer {:on-click #(dispatch [:visitor-message [command]])} command]])

(defn message [item]
  (fn [{:keys [owner message]}]
    [:li.my1.flex {:class (str "justify-" (if (= owner :bot) "start" "end"))}
      [:div.message-inner {:style {:max-width "75%"}}
        (if (= owner :visitor)
          [:p.m0.right-align.grey "You"]
          [:p.m0.left-align.grey "jd_bot"])
        [:ul.list-reset.message-block
          (for [m (:body message)]
            ^{:key m} [:li
                        [:div.inline-block.message {:class (str (name owner))}
                          [:p.m0.p0 m]]])
          (when-let [commands (:commands message)]
            [:li
              [:ul.list-reset.inline-block.commands
                (for [command commands]
                  ^{:key command} [render-command command])]])]]]))

(defn dialogue []
  (let [items (subscribe [:dialogue])]
    (reagent/create-class
      {:component-did-update #(-> js/document
                                (.querySelector (str "ul li:nth-child(" (count @items) ")"))
                                (.scrollIntoView (clj->js {:block :end :behavior :smooth})))
       :reagent-render
          (fn []
            [:div.absolute.top-0.bottom-0.left-0.right-0
              [:div.absolute.bottom-0.left-0.right-0.dialogue
                [:div.max-width-3.mx-auto.px2.mt4
                  [:div.clearfix
                    [:ul.list-reset.my4.flex.flex-column
                      [cst {:transition-name "message"
                            :transition-enter-timeout 250
                            :transition-leave-timeout 250}
                        (map-indexed
                          (fn [idx item]
                            ^{:key (str "item-" idx)} [message item])
                          @items)]]]]]])})))

(defn input []
  (let [text (subscribe [:input])
        save #(let [t (-> @text (str/trim))]
                (when-not (str/empty-or-nil? t)
                  (dispatch [:visitor-message [t]])
                  (.blur (.-activeElement js/document))))
        stop #(dispatch [:input-change ""])]
    (reagent/create-class
      {:component-did-mount #(.focus (reagent/dom-node %))
       :component-did-update #(.focus (reagent/dom-node %))
       :reagent-render
          (fn []
            [:input.border-none.h4.py2.sans-serif
              {:on-change #(dispatch [:input-change (-> % .-target .-value)])
               :on-key-down #(do
                               (case (.-which %)
                                 13 (save)
                                 27 (stop)
                                 nil))
               :value @text
               :placeholder "Type message..."}])})))

(defn send []
  (let [text (subscribe [:input])]
    (fn []
      [:div.absolute.right-0.top-0.bottom-0.bg-blue.pointer.send
        {:style {:width :54px}
         :on-click #(let [t (-> @text (str/trim))]
                     (when-not (str/empty-or-nil? t)
                       (dispatch [:visitor-message [t]])
                       (.blur (.-activeElement js/document))))}])))

(defn main []
  [:div.sans-serif.overflow-hidden
    [header]
    [dialogue]
    [:footer.fixed.bottom-0.left-0.right-0.bg-white.border-top.bdr-silver
      [:div.max-width-3.mx-auto.relative.px2
        [:div.clearfix
          [input]
          [send]]]]])
