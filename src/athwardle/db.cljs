(ns athwardle.db
  (:require
   [athwardle.game :as game]))

(def default-db
  {:answer (game/gen-answer)
   :guesses []
   :current-guess ""})
