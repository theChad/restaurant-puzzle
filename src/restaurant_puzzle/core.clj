(ns restaurant-puzzle.core
  (:gen-class)
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]
            [restaurant-puzzle.utils :as utils]
            [restaurant-puzzle.phoneme-conversion :as pc]
            ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [phoneme-dictionary (pd/get-phoneme-dictionary "resources/cmudict-0.7b")
        reverse-phoneme-dictionary (pd/get-reverse-phoneme-dictionary)]
    (->> (io/read-clue-words)
        (io/words-to-phonemes phoneme-dictionary)
        second
        flatten
        (pc/get-words-from-phonemes reverse-phoneme-dictionary))))

(-main)
