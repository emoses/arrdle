(ns athwardle.events
  (:require
   [re-frame.core :as rf]
   [athwardle.db :as db]
   [athwardle.game :as game]
   [goog.events :as gevent]
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
        (= (.-ENTER ^js gevent/KeyCodes) keycode) (rf/dispatch [::submit])
        (or (= (.-DELETE ^js gevent/KeyCodes) keycode)
            (= (.-BACKSPACE ^js gevent/KeyCodes) keycode)) (rf/dispatch [::backspace]))))
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

(rf/reg-event-db
 ::submit
 (fn [db _]
   (if-not (= game/LEN (count (:current-guess db)))
     db
     (-> db
         (update :guesses conj (:current-guess db))
         (assoc :current-guess ""))
     )))

(rf/reg-event-db
 ::backspace
 (rf/path :current-guess)
 (fn [g _]
   (if (empty? g)
     g
     (subs g 0 (dec (count g))))))

(rf/reg-event-db
 ::reset
 (fn [_ _]
   db/default-db))
