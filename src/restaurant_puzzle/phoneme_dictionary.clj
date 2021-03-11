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
                              (clojure.string/replace #"[0-9]+" "")
                              (clojure.string/split #" +")))
               (clojure.string/split string #"\n"))))

(defn make-dictionary-from-rows
  "Create a collection of word to phoneme-list pairs."
  [rows]
  (reduce #(assoc %1 (first %2) (rest %2)) {} rows))

(defn get-phoneme-dictionary
  "Read the dictionary in from a file to list of maps
  {:word word :phonemes [sequence of phonemes]}"
  ([dictionary-file]
   (make-dictionary-from-rows (parse (slurp dictionary-file))))
  ([]
   (get-phoneme-dictionary "resources/cmudict-0.7b")))

(defn get-phonemes-from-word
  "Return a sequence of the phonemes of a given word."
  [phoneme-dictionary word]
  (phoneme-dictionary (.toUpperCase word)))



(get-phonemes-from-word (get-phoneme-dictionary) "test")




