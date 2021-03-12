(ns restaurant-puzzle.core
  (:gen-class)
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]
            [restaurant-puzzle.utils :as utils]
            [restaurant-puzzle.phoneme-conversion :as pc]
            [clojure.math.combinatorics :as combo]
            ))

(defn -main
  [& args]
  ;; Get dictionary and reverse dictionary.
  (let [phoneme-dictionary (pd/get-phoneme-dictionary "resources/cmudict-0.7b")
        reverse-phoneme-dictionary (pd/get-reverse-phoneme-dictionary)]
    (println " Top. ")
    (->> (utils/read-clue-words) ; clue word-lists
         (utils/words-to-phonemes phoneme-dictionary) ; clue phoneme lists
         first
         (apply combo/cartesian-product)
         (map combo/permutations)
         (reduce into)
         (map flatten)
         (map (partial pc/get-words-from-phonemes reverse-phoneme-dictionary))
         (reduce into)
         (remove (partial utils/uses-all-base-words? (utils/read-clue-words)))
         ;;(#(do (println "remove. " %) %))
         (pc/string-from-word-chains)
         (spit "resources/answers.txt")
         )))
(-main)

;;(combo/permutations [1 2 3])


