(ns athwardle.game)

(def LEN 5)
(def MAX-GUESSES 6)

(def state {
            :answer ""
            :guesses []
            })

(defn gen-answer []
  (let [num-a (+ 1 (rand-int (dec LEN)))]
    (apply str (concat (repeat num-a "A") (repeat (- LEN num-a) "R")))))

(defn init-answer [state]
  (assoc state :answer (gen-answer)))


(defn guess [state guess]
  (update state :guesses conj guess))

(defn valid-guess? [guess]
  (not (nil? (re-matches #"A[AR]{3}R" guess))))

(defn status [state]
  (cond
    (= (last (:guesses state)) (:answer state)) :win
    (= (count (:guesses state)) MAX-GUESSES) :lose
    :else :playing))

(defn print-game [state]
  (dorun (map #(println (interpose " " %)) (:guesses state))))
