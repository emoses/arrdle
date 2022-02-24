(ns athwardle.subs
  (:require
   [re-frame.core :as re-frame]
   [athwardle.game :as game]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::state
 (fn [db]
   (game/status db)))

(re-frame/reg-sub
 ::current-guess
 (fn [db]
   (:current-guess db)))

(re-frame/reg-sub
 ::guesses
 (fn [db]
   (:gusses db)))

(re-frame/reg-sub
 ::answer
 (fn [db]
   (:answer db)))
