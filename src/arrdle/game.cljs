(ns arrdle.game)

(def LEN 5)
(def MAX-GUESSES 6)
(def VALID-LETTERS #{"A" "R"})

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

(defn calc-guess [guess answer]
  "Two passes: identify matches, identify rest"
  (let [[matches letter-ct]
        (loop [letter-count (frequencies answer)
               [l & ll] guess
               [a & aa] answer
               result []]
          (if-not l
            [result letter-count]
            (if (= l a)
              (recur
               (update letter-count l dec)
               ll
               aa
               (conj result {:letter l :state :correct}))
              (recur
               letter-count
               ll
               aa
               (conj result :placeholder)))))]
    (loop [letter-count letter-ct
           [l & ll] guess
           [a & aa] answer
           [prev & prevs] matches
           result []]
      (if-not l
        result
        (if-not (= :placeholder prev)
          (recur
           letter-count
           ll
           aa
           prevs
           (conj result prev))
          (if (> (letter-count l) 0)
            (recur
             (update letter-count l dec)
             ll
             aa
             prevs
             (conj result {:letter l
                           :state :in-word}))
            (recur
             letter-count
             ll
             aa
             prevs
             (conj result {:letter l
                           :state :incorrect}))))))))
