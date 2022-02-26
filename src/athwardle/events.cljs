(ns athwardle.events
  (:require
   [re-frame.core :as rf]
   [athwardle.db :as db]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::keypress
 (fn [db [_ letter]]
   (if (<= 5 (count (:current-guess db)))
     db
     (update db :current-guess str letter))))

(rf/reg-event-db
 ::submit
 (fn [db _]
   (if-not (= 5 (count (:current-guess db)))
     db
     (-> db
         (update :guesses conj (:current-guess db))
         (assoc :current-guess ""))
     )))
