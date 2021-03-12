(ns restaurant-puzzle.utils
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]
            [clojure.set]
            [clojure.string]))

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
  "From a list of word lists, return a list of list of lists of phonemes"
  [phoneme-dictionary words]
  (map 
   (fn [w] (map (partial pd/get-phonemes-from-word phoneme-dictionary) w))
   words))

(defn uses-all-base-words?
  "True if a word chain contains all the base words.
   word-chain: [[homonymns] [homonymns2] ..]
   base-words: [w1 w1..]"
  [base-words word-chain]
  ;; Technically, this won't count homonmyms in the base pair as separate
  ;; words, but that's probably fine here.
  (empty? (clojure.set/difference (set (flatten base-words)) (set (flatten word-chain))))
  )




(read-clue-words "resources/clue_words.txt")
(def a (words-to-phonemes (pd/get-phoneme-dictionary)
                          (read-clue-words "resources/clue_words.txt")))


(remove (partial uses-all-base-words? [[["this" "is" "a" "test"]]])                      
        [[["this" "and"] ["a"] ["is"] "tess"]])


;;; This is just clojure.math.combinatorics/cartesian-product.
;;; I'd still like to come back to a recurseive version sometime.

(defn pronunciation-combos
  "Return all combinations of pronunciations of words from a collection
   of possible pronunciations.
  E.g. [[pron-a1 pron-a2] [pron-b1 pron-b2]] should return
  [[pron-a1 pronb1] [pron-a1 pron-b2] [pron-a2 pron b-1] [pron-a2 pron b-2]]"
  ([p-poss p-combos partial-combo]
   (println "p-combos " p-combos "partial:  " partial-combo)
   (cond (empty? p-poss) (conj p-combos partial-combo)
         (not (coll? (first p-poss)))
         ;; First element of p-poss is scalar, just add to p-combos
         (recur (rest p-poss)
                p-combos
                (conj partial-combo (first p-poss)))
         ;; Cycled through all first element pronunciations, move on.
         (empty? (first p-poss)) (recur (rest p-poss) p-combos partial-combo)
         ;; Append one of the first element possibilities to the end of the last combo.
         :else (do  (println "p-poss: " p-poss)
                    (println "passing " [(drop 1 (first p-poss)) (rest p-poss)])
                 (println "adding "(first (take 1 (first p-poss))))
                 (recur (conj (rest p-poss) (drop 1 (first p-poss)))
                          p-combos
                          (conj partial-combo (first (take 1 (first p-poss))))))))
  ([p-poss]
   (pronunciation-combos p-poss [] [])))

