(ns athwardle.views
  (:require
   [re-frame.core :as rf]
   [athwardle.styles :as styles]
   [athwardle.subs :as subs]
   [athwardle.events :as events]
   ))

(def GUESS-LEN 5)
(def NUM-GUESSES 6)

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

(defn letter [letter-state]
  [:div {:class [ (name (:state letter-state)) "letter"]} (:letter letter-state)])

(defn blank-row []
  [:div (styles/guess) (for [i (range GUESS-LEN)] [:div.letter.blank {:key i}])])

(defn guess-row [guess answer]
  (if-not guess
    [blank-row]
    (let [states (calc-guess guess answer)]
      [:div (styles/guess) (map-indexed (fn [i l] ^{:key i} [letter l]) states)])))

(defn current-guess [guess]
  (let [guess-sp (take GUESS-LEN (lazy-cat (or guess "") (repeat " ")))]
    [:div (styles/guess)
     (map-indexed (fn [i l] ^{:key i} [letter l])
                  (map (fn [l] {:letter l :state :current}) guess-sp))]))

(defn key-button [letter]
  [:div
   {:class (styles/letter-key)
    :on-click #(rf/dispatch [::events/keypress letter])}
   letter])

(defn submit-button []
  [:div {:class (styles/letter-key)
         :on-click #(rf/dispatch [::events/submit])}
   "RET"])

(defn main-panel []
  (let [cur (rf/subscribe [::subs/current-guess])
        guesses (rf/subscribe [::subs/guesses])
        answer (rf/subscribe [::subs/answer])]
    [:div
     [:header "Aaardle"]
     [:section.board
      (doall (map-indexed (fn [i g] ^{:key i} [guess-row g @answer ]) @guesses))
      [current-guess @cur]
      (for [i (range (inc (count @guesses)) NUM-GUESSES)] ^{:key i} [guess-row nil nil])]
     [:section.keyboard (key-button "A") (key-button "R") [submit-button {:key 3}]]
     ]))
