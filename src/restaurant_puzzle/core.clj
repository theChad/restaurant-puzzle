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
    ;; Just look at the top line of clue data.
    (->> (utils/read-clue-words) ; clue word-lists
         (utils/words-to-phonemes phoneme-dictionary) ; clue phoneme lists
         first ; first line only
         (apply combo/cartesian-product) ; all pronunciations per word
         (map combo/permutations) ; all combinations of words
         (reduce into)
         (map flatten)
         ;; Get possible words from the phoneme lists
         (map (partial pc/get-words-from-phonemes reverse-phoneme-dictionary))
         (reduce into)
         ;; Remove entries with that still use almost all of the base words
         (remove (partial utils/uses-most-base-words? 3 (utils/read-clue-words)))
         (pc/string-from-word-chains)
         (spit "resources/answers.txt")
         )))



