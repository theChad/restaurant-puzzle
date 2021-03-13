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

(defn replace-vowels-in-phonemes
  "Replace the vowels in phonemes only, given a row of the dict file."
  [row]
  (into [(first row)]
        (map #(clojure.string/replace % #"[AEIOU]+[WYH]*" "A") (rest row))))

(defn dictionary-rows-to-string
  "From a vector of vectors representing the dictionary rows,
   return a string"
  [rows]
  (reduce #(str %1 (clojure.string/join " " %2) "\n") "" rows))

(defn make-bland-vowel-dictionary-file
  "Create a new dictionary file with aeiou all assinged to the same letter."
  [dictionary-file out-file]
  (let [d (parse (slurp dictionary-file))]
    (spit out-file
          (dictionary-rows-to-string
           (map replace-vowels-in-phonemes d)))))

(defn get-phonemes-from-word
  "Return a sequence of the phonemes of a given word."
  [phoneme-dictionary word]
  (phoneme-dictionary (.toUpperCase word)))

(defn get-word-from-phonemes
  "Return a word from its sequence of phonemes."
  [reverse-phoneme-dictionary phonemes]
  (reverse-phoneme-dictionary phonemes))




;;(print (dictionary-rows-to-string (map replace-vowels-in-phonemes (take 5 (parse (slurp "resources/cmudict-0.7b"))))))
;;(make-bland-vowel-dictionary-file "resources/cmudict-0.7b" "resources/bland-vowel-dict")


