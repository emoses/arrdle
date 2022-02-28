(ns arrdle.styles
  (:require-macros
    [garden.def :refer [defcssfn]])
  (:require
    [spade.core   :refer [defglobal defclass defattrs]]
    [garden.units :refer [deg px]]
    [garden.color :refer [rgba]]))

(defcssfn linear-gradient
 ([c1 p1 c2 p2]
  [[c1 p1] [c2 p2]])
 ([dir c1 p1 c2 p2]
  [dir [c1 p1] [c2 p2]]))

(defglobal defaults
  [:body
   {:background-color    :white
    }]
  [:#app
   {:display :flex
    :justify-content :center}]
  [:#main
   {:display :flex
    :flex-direction "column"
    :justify-content "space-between"
    :align-items "center"
    :max-width "500px"}])


(defglobal boardetc
  [:.board
   {:display "grid"
    :grid-template-rows "repeat(6, 1fr)"
    :grid-gap "5px"
    :padding "10px"
    :font-size "24px"}]
  [:header
   {:flex "1 100%"
    :align-items :center
    :font-family "'Helvetica Neue', sans-serif"
    :font-weight :bold
    :font-size "26px"
    :display :flex
    :justify-content :center
    :width "100%"}
   [:.title
    {:text-align :center
     :flex "3 1"}]
   [:.right-buttons
    {:flex "1"
     :display :flex
     :justify-content :flex-end}]
   [:.left-buttons
    {:flex "1"
     :display :flex
     :justify-content :flex-start}]]
  [:.icon-button
   {:cursor :pointer}]
  [:.keyboard
   {:display "flex"
    :justify-content :center
    :max-width "500px"
    :touch-action :manipulation}
   [:>* {:margin "2px"}]  ])

(defclass letter-key
  [& [size & _]]
  (let [flex (if (= size :large) "1.5" "1")]
    {:display :inline-block
     :padding "10px 15px"
     :border-radius "8px"
     :background-color "#eee"
     :flex flex
     :cursor :pointer})
  )

(defattrs guess
  []
  {:display "grid"
   :grid-template-columns"repeat(5, 1fr)"
   :grid-gap "5px"
   :padding "1px"}
  [:.letter {:display "inline-block"
             :border "1px solid black"
             :min-width "1.75rem"
             :min-height "1rem"
             :text-align "center"
             :padding "5px"}]
  [:.correct {:background-color "green"
              :color "white"}]
  [:.in-word {:background-color "#ea0"
              :color "white"}]
  [:.incorrect {:background-color "#999"
                :color "black"}]
  [:.current {:background-color "white"
              :color "black"}]
  )

(defattrs modal
  []
  {:display :block
   :z-index 10
   :background-color :white
   :border "1px solid #ddd"
   :border-radius "10px"
   :position :absolute
   :margin "1.75rem auto"
   :left "50%"
   :transform "translate(-50%, 0)"
   :min-width "300px"
   :padding "10px"
   :box-shadow "8px 5px 5px black"}
  [:header
   {:margin-top "15px"}])

(defclass close-button
  []
  {:position :absolute
   :width "24px"
   :height "24px"
   :cursor :pointer
   :top "8px"
   :right "8px"})
