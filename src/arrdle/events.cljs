(ns arrdle.events
  (:require
   [re-frame.core :as rf]
   [arrdle.db :as db]
   [arrdle.game :as game]
   [clojure.string :refer [upper-case]]
   ))

(defn key-handler [evt]
  (let [key (.-key evt)
        keycode (.-keyCode evt)
        key-norm (upper-case key)]
    (when-not (or (.-altKey evt)
                  (.-ctrlKey evt)
                  (.-metaKey evt))
      (cond
        (game/VALID-LETTERS key-norm) (rf/dispatch [::keypress key-norm])
        ;;13 = enter
        (= 13 keycode) (rf/dispatch [::submit])
        ;; 46 = delete, 8 = backspace
        (or (= 46 keycode)
            (= 8 keycode)) (rf/dispatch [::backspace]))))
  )

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::keypress
 (fn [db [_ letter]]
   (if (<= game/LEN (count (:current-guess db)))
     db
     (update db :current-guess str letter))))

(rf/reg-event-fx
 ::submit
 (fn [{:keys [db]} _]
   (if-not (= game/LEN (count (:current-guess db)))
     {:db db}
     (let [new-state (-> db
                         (update :guesses conj (:current-guess db))
                         (assoc :current-guess ""))]
       (condp = (game/status new-state)
         :win {:db new-state
               :fx [[:dispatch [::show-modal :win]]]}
         :lose {:db new-state
                :fx [[:dispatch [::show-modal :lose]]]}
         {:db new-state}))
     )))

(rf/reg-event-db
 ::backspace
 (fn [db _]
   (when (= :playing (game/status db))
     (let [g (:current-guess db)]
       (if (empty? g)
         db
         (assoc db :current-guess (subs g 0 (dec (count g)))))))))

(rf/reg-event-db
 ::reset
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::close-modal
 (rf/path :modal)
 (fn [m _]
   nil))

(rf/reg-event-db
 ::show-modal
 (rf/path :modal)
 (fn [_ [_ new-modal]]
   new-modal))

(rf/reg-event-db
 ::share-social-clipboard
 (fn [db [_ text]]
   (-> (.writeText (.-clipboard js/navigator) text)
       (.then #(rf/dispatch [::share-social-clipboard-done]))
       (.catch #(rf/dispatch [::share-social-clipboard-error %])))
   db))

(rf/reg-event-db
 ::share-social-clipboard-done
 (fn [db _]
   (js/setTimeout
    #(rf/dispatch [::toast-hide])
    5000)
   (assoc db :toast "Copied to clipboard")))

(rf/reg-event-db
 ::share-social-clipboard-error
 (fn [db [_ err]]
   (js/setTimeout
    #(rf/dispatch [::toast-hide])
    5000)
   (.log js/console err)
   (assoc db :toast "Error copying to clipboard")))

(rf/reg-event-db
 ::toast-hide
 (rf/path :toast)
 (fn [_ _ ]
   nil))
