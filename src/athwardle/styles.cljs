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
    :background-image    [(linear-gradient :white (px 2) :transparent (px 2))
                          (linear-gradient (deg 90) :white (px 2) :transparent (px 2))
                          (linear-gradient (rgba 255 255 255 0.3) (px 1) :transparent (px 1))
                          (linear-gradient (deg 90) (rgba 255 255 255 0.3) (px 1) :transparent (px 1))]
    :background-size     [[(px 100) (px 100)] [(px 100) (px 100)] [(px 20) (px 20)] [(px 20) (px 20)]]
    :background-position [[(px -2) (px -2)] [(px -2) (px -2)] [(px -1) (px -1)] [(px -1) (px -1)]]}])

(defclass level1
  []
  {:color :green})

(defattrs guess
  []
  [:.letter {:display "inline-block"
             :border "1px solid black"}]
  [:.correct {:background-color "green"
              :color "white"}]
  [:.in-word {:background-color "yellow"
              :color "white"}]
  [:.incorrect {:background-color "#999"
                :color "black"}]
  [:.current {:background-color "white"
              :color "black"}]
  )
