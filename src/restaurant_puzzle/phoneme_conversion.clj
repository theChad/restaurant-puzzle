(ns restuarant-puzzle.phoneme-conversion
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]))

;; For now I'll use the phoneme-list first search.
;; Alternative is to recursively search for first element in every word's
;; phoneme, then second, etc.

(defn get-words-from-phonemes
  "Return collection of possible word sequences using all phonemes in sequence."
  [reverse-phoneme-dictionary phonemes]
  )

(defn append-phoneme-words
  "Append words to the possible words list, returning that and remaining phonemes.
   Returns collection of [word-list remaining-phoneme] pairs."
  [reverse-phoneme-dictionary possible-words phonemes]
  
  )
