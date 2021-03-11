(ns restaurant-puzzle.phoneme-dictionary
  (:require [clojure.string]))


(defn is-comment-row
  "True if the row (as a list) is a comment row."
  [row]
  (= (subs (first row) 0 (min 3 (count (first row)))) ";;;"))

(defn parse
  "Parse the text into a sequence of rows of words and their phonemes.
  Ignore comment rows"
  [string]
  (filter (fn [row] (not (is-comment-row row)))
          (map (fn [row]  (-> row
                              ;; numbers indicate vowel accent, which we don't need.
                              ;; (1) after a word indicates another pronunciation.
                              ;; I'll just store all pronunciations in the same place.
                              (clojure.string/replace #"[0-9]+|\(|\)" "")
                              (clojure.string/split #" +")))
               (clojure.string/split string #"\n"))))

(defn make-dictionary-from-rows
  "Create a collection of word to phoneme-list pairs."
  [rows]
  (reduce (fn [old-map new-word] (update old-map (first new-word) #(conj % (rest new-word)))) {} rows))

(defn get-phoneme-dictionary
  "Read the dictionary in from a file to list of maps
  {:word word :phonemes [sequence of phonemes]}"
  ([dictionary-file]
   (make-dictionary-from-rows (parse (slurp dictionary-file))))
  ([]
   (get-phoneme-dictionary "resources/cmudict-0.7b")))

(defn make-reverse-dictionary-from-rows
  "Create a map of phoneme-vec to word pairs."
  [rows]
  (reduce (fn [old-map new-word] (update old-map (rest new-word) #(conj % (first new-word)))) {} rows))

(defn get-reverse-phoneme-dictionary
  "Get a map from vectors of phonemes to words."
  ([dictionary-file]
   (make-reverse-dictionary-from-rows (parse (slurp dictionary-file))))
  ([]
   (get-reverse-phoneme-dictionary "resources/cmudict-0.7b")))

(defn get-phonemes-from-word
  "Return a sequence of the phonemes of a given word."
  [phoneme-dictionary word]
  (phoneme-dictionary (.toUpperCase word)))

(defn get-word-from-phonemes
  "Return a word from its sequence of phonemes."
  [reverse-phoneme-dictionary phonemes]
  (reverse-phoneme-dictionary phonemes))



(get-phonemes-from-word (get-phoneme-dictionary) "read")
(get-word-from-phonemes (get-reverse-phoneme-dictionary) ["IH" "N"])

;;(println (take 5 (get-reverse-phoneme-dictionary)))

