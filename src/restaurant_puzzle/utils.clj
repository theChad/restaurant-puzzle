(ns restaurant-puzzle.utils
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]))

;;; Read in clues, rearrange words and break down into phonemes

(defn read-clue-rows
  "Read the rows of clue words into a vector."
  ([filename]
   (-> filename
       slurp
       clojure.string/upper-case
       (clojure.string/split #"\n")
       )))

(defn read-clue-words
  "Read the clue words into a vector"
  ([filename]
   (map #(clojure.string/split % #" ") (read-clue-rows filename)))
  ([]
   (read-clue-words "resources/clue_words.txt")))

(defn words-to-phonemes
  "From a list of words, return a list of lists of phonemes"
  [phoneme-dictionary words]
  (map 
   (fn [w] (map (partial pd/get-phonemes-from-word phoneme-dictionary) w))
   words))


(read-clue-words "resources/clue_words.txt")
(second (words-to-phonemes (pd/get-phoneme-dictionary)
                           (read-clue-words "resources/clue_words.txt")))
