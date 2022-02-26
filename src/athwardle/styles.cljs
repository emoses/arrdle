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
   {:color               :red
    :background-color    :#ddd
    :display "flex"
    :flex-direction "column"
    :justify-content "space-between"
    :align-items "center"}])


(defglobal board
  [:.board
   {:display "grid"
    :grid-template-rows "repeat(6, 1fr)"
    :grid-gap "5px"
    :padding "10px"}]
  [:header
   {:flex "1 100%"}])


(defclass letter-key
  []
  {:display :inline-block
   :padding "10px"
   :border-radius "2px"
   :background-color "#ddd"}
  )

(defattrs guess
  []
  {:display "grid"
   :grid-template-columns"repeat(5, 1fr)"
   :grid-gap "5px"
   :padding "10px"}
  [:.letter {:display "inline-block"
             :border "1px solid black"}]
  [:.correct {:background-color "green"
              :color "white"}]
  [:.in-word {:background-color "#ea0"
              :color "white"}]
  [:.incorrect {:background-color "#999"
                :color "black"}]
  [:.current {:background-color "white"
              :color "black"}]
  )
