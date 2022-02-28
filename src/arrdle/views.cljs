(ns arrdle.views
  (:require
   [re-frame.core :as rf]
   [arrdle.styles :as styles]
   [arrdle.subs :as subs]
   [arrdle.events :as events]
   [arrdle.game :as game]
   [goog.string :as gstring]
   ))

(defn letter-state-social [letter-state]
  (condp = (:state letter-state)
    :correct "🟩"
    :in-word "🟨"
    "⬛"))

(defn guess-to-social [answer guess]
  (let [states (game/calc-guess guess answer)]
    (apply str (interpose " " (map letter-state-social states)))))

(defn social-text [guesses answer win?]
  (let [all-guesses (map (partial guess-to-social answer) guesses)]
    (str (gstring/format "🏴‍☠️ Arrdle %d/6 🏴‍☠" (count all-guesses)) "\n\n"
         (apply str (interpose "\n" all-guesses)) "\n\n"
         "https://arrdle.7sirenscove.com")))

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
  [:div {:class (styles/letter-key :large)
         :on-click #(rf/dispatch [::events/submit])}
   "Enter"])

(defn backspace-button []
  [:div {:class (styles/letter-key :large)
         :on-click #(rf/dispatch [::events/backspace])}
   "⌫"])

(defn close-button [handler]
  [:div {:class (styles/close-button)
         :on-click #(rf/dispatch [::events/close-modal])} "⚔"])
"<a href=\"https://twitter.com/share?ref_src=twsrc%5Etfw\" class=\"twitter-share-button\" data-text=\"some default text\" data-show-count=\"false\"></a><script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>"

(defn tweet-button [social-text]
  [:span.tweet
   [:a {:href (gstring/format "https://twitter.com/intent/tweet/?text=%s" (gstring/urlEncode social-text))
        :target "_blank"
        :title "Share on Twitter"}
    [:img.icon
     {:src "img/twitter.svg"
      :alt "Tweet"}]]])

(defn copy-button [social-text]
  [:span.copy
   [:button {:on-click #(rf/dispatch-sync [::events/share-social-clipboard social-text])}
    "🏴‍☠ Share 🏴‍☠" [:img.icon {:src "img/share-white.svg" :alt ""}]]])

(defn modal [& content]
  [:div (styles/modal)
   [close-button]
   (map-indexed #(with-meta %2 {:key %1}) content)])

(defn info-modal []
  [modal [:header "Arrdle"]
   [:p "A pirate walks into a bar and walks up to order a drink.  The bartender looks down and sees that the pirate has a ship's wheel attached to the front of his pants.  \"What's that ship's wheel for?\" asks the bartender."]
   [:p "The pirate replies, \"Arr, I dunno, but it's drivin' me nuts\""]
   [:hr]
   [:p "Arrdle, the pirate daily word game."]
  [:footer "© 2022 Evan Moses"]])

(defn win-modal []
  (let [guesses (rf/subscribe [::subs/guesses])
        answer (rf/subscribe [::subs/answer])]
    [modal [:header "Arrr, ye win!"]
     [:p "Shiver me timbers, ye guessed me word."]
     (let [s (social-text @guesses @answer true)]
       [:div {:class (styles/social)}
        [tweet-button s]
        [copy-button s]])]))

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

(defn toast []
  (let [toast (rf/subscribe [::subs/toast])]
    [:div {:class [(styles/toast) (if-not @toast "hidden")]} @toast]))

(defn main-panel []
  (let [status (rf/subscribe [::subs/status])
        modal-shown (rf/subscribe [::subs/modal])
        answer (rf/subscribe [::subs/answer])]
    [:div#main
     [toast]
     (condp = @modal-shown
       :info [info-modal]
       :win  [win-modal]
       :lose [lose-modal @answer]
       nil)
     [:header [:div.left-buttons] [:div.title "Arrdle"]
      [:div.right-buttons
       [:div.info-button.icon-button
        {:on-click #(rf/dispatch [::events/show-modal :info])} "?"]]]
     [board]
     [:section.keyboard
      [backspace-button]
      [key-button "A"] [key-button "R"]
      [submit-button]]
     ]))
