(ns arrdle.db
  (:require
   [arrdle.game :as game]))

(def default-db
  {:answer (game/gen-answer)
   :guesses []
   :current-guess ""
   :modal nil
   :toast nil})
