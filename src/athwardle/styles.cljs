(ns athwardle.styles
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
  [:#main
   {:display "flex"
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
    :font-size "26px"}]
  [:.keyboard
   {:display "flex"
    :justify-content :center
    :max-width "500px"
    :width "100%"}
   [:>* {:margin "2px"}]  ])

(defclass letter-key
  []
  {:display :inline-block
   :padding "10px"
   :border-radius "8px"
   :background-color "#eee"}
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
