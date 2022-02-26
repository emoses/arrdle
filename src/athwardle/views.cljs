(ns athwardle.views
  (:require
   [re-frame.core :as rf]
   [athwardle.styles :as styles]
   [athwardle.subs :as subs]
   [athwardle.events :as events]
   [athwardle.game :as game]
   [goog.string :as gstring]
   ))

(defn letter [letter-state]
  [:div {:class [ (name (:state letter-state)) "letter"]}
   (or (:letter letter-state) (gstring/unescapeEntities "&nbsp;"))])

(defn blank-row []
  [:div (styles/guess) (for [i (range game/LEN)]
                         [:div.letter.blank {:key i} (gstring/unescapeEntities "&nbsp;")])])

(defn guess-row [guess answer]
  (if (empty? guess)
    [blank-row]
    (let [states (game/calc-guess guess answer)]
      [:div (styles/guess) (map-indexed (fn [i l] ^{:key i} [letter l]) states)])))

(defn current-guess [guess]
  (let [guess-sp (take game/LEN (lazy-cat (or guess "") (repeat " ")))]
    [:div (styles/guess)
     (map-indexed (fn [i l] ^{:key i} [letter l])
                  (map (fn [l] {:letter l :state :current}) guess-sp))]))

(defn key-button [letter]
  (let [status (rf/subscribe [::subs/status])]
    [:div
     {:class (styles/letter-key)
      :on-click #(when (= @status :playing) (rf/dispatch [::events/keypress letter]))}
     letter]))

(defn submit-button []
  [:div {:class (styles/letter-key)
         :on-click #(rf/dispatch [::events/submit])}
   "Enter"])

(defn backspace-button []
  [:div {:class (styles/letter-key)
         :on-click #(rf/dispatch [::events/backspace])}
   "⌫"])

(defn close-button [handler]
  [:div {:class (styles/close-button)
         :on-click #(rf/dispatch [::events/close-modal])} "⚔"])

(defn modal [& content]
  [:div (styles/modal)
   [close-button]
   (map-indexed #(with-meta %2 {:key %1}) content)])

(defn info-modal []
  [modal [:header "Aaardle"]
   [:p "A pirate walks into a bar and walks up to order a drink.  The bartender looks down and sees that the pirate has a ship's wheel attached to the front of his pants.  \"What's that ship's wheel for?\" asks the bartender."]
   [:p "The pirate replies, \"Arr, I dunno, but it's drivin' me nuts\""]
   [:hr]
   [:p "Aaardle, the pirate daily word game."]
  [:footer "© 2022 Evan Moses"]])

(defn win-modal []
  [modal [:header "Arrr, ye win!"]
   [:p "Shiver me timbers, ye guessed me word."]])

(defn lose-modal [correct-word]
  [modal [:header "Ye lose, ye scurvy dog!"]
   [:p "Walk the plank ye scallywag.  The answer was " correct-word]])

(defn board []
  (let [cur (rf/subscribe [::subs/current-guess])
        guesses (rf/subscribe [::subs/guesses])
        answer (rf/subscribe [::subs/answer])
        status (rf/subscribe [::subs/status])]
   [:section.board
      (doall (map-indexed (fn [i g] ^{:key i} [guess-row g @answer ]) @guesses))
      (when (= @status :playing) [current-guess @cur])
      (for [i (range (inc (count @guesses)) game/MAX-GUESSES)] ^{:key i} [guess-row nil nil])]))

(defn main-panel []
  (let [status (rf/subscribe [::subs/status])
        modal-shown (rf/subscribe [::subs/modal])
        answer (rf/subscribe [::subs/answer])]
    [:div#main
     (condp = @modal-shown
       :info [info-modal]
       :win  [win-modal]
       :lose [lose-modal @answer]
       nil)
     [:header [:div.left-buttons] [:div.title "Aaardle"]
      [:div.right-buttons
       [:div.info-button.icon-button
        {:on-click #(rf/dispatch [::events/show-modal :info])} "?"]]]
     [board]
     [:section.keyboard
      [backspace-button]
      [key-button "A"] [key-button "R"]
      [submit-button]]
     ]))
