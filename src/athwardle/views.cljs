(ns athwardle.views
  (:require
   [re-frame.core :as rf]
   [athwardle.styles :as styles]
   [athwardle.subs :as subs]
   ))

(def GUESS-LEN 5)

(defn calc-guess [guess answer]
  (loop [letter-count (frequencies answer)
         [l & ll] guess
         [a & aa] answer
         result []]
    (if-not l
      result
      (if (> (letter-count l) 0)
        (recur
         (update letter-count l dec)
         ll
         aa
         (conj result {:letter l
                       :state (if (= l a) :correct :in-word)}))
        (recur
         letter-count
         ll
         aa
         (conj result {:letter l
                       :state :incorrect}))))))

(defn letter [letter-state & attrs]
  [:div (merge (first attrs) {:class [ (name (:state letter-state)) "letter"]}) (:letter letter-state)])

(def blanks (repeatedly [:div.blank]))

(defn guess [guess answer & attrs]
  (let [states (calc-guess guess answer)]
    [:div (merge (first attrs) (styles/guess)) (map-indexed (fn [i l] (letter l {:key i})) states)]))

(defn current-guess [guess]
  (let [guess-sp (take GUESS-LEN (lazy-cat guess " "))]
    [:div (styles/guess)
     (map-indexed (fn [i l] (letter l {:key i}))
                  (map (fn [l] {:letter l :state :current}) guess-sp))
     ]))

(defn main-panel []
  (let [cur (rf/subscribe [::subs/current-guess])
        guesses (rf/subscribe [::subs/guesses])
        answer (rf/subscribe [::subs/answer])]
    [:div
     [:h1
      {:class (styles/level1)}
      (map-indexed (fn [i g] (guess g @answer {:key i})) @guesses)
      (current-guess @cur)]
     ]))
