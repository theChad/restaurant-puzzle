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

(defn uses-most-base-words?
  "True if a word chain lacks at most n of the base words.
   word-chain: [[homonymns] [homonymns2] ..]
   base-words: [w1 w1..]"
  [n base-words word-chain]
  ;; Technically, this won't count homonmyms in the base pair as separate
  ;; words, but that's probably fine here.
  (>= n (count (clojure.set/difference (set (flatten base-words)) (set (flatten word-chain)))))
  )



(read-clue-words "resources/clue_words.txt")
(def a (words-to-phonemes (pd/get-phoneme-dictionary)
                          (read-clue-words "resources/clue_words.txt")))


(remove (partial uses-most-base-words? 1 [[["this" "is" "a" "test"]]])                      
        [[["this" "and"] ["a"] ["is"] "tes"]])
(uses-most-base-words? 1 [[["this" "is" "a" "test"]]]                      
                       [[["this" "and"] ["a"] ["is"] "tes"]])



;;; This is just clojure.math.combinatorics/cartesian-product.
;;; I'd still like to come back to a recurseive version sometime.

;;; pronunciation-combos doesn't work.

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

;;; Working, non-memoized cartesian product. No tail recursion.
;;; Does use an accumulator though.

(defn cartesian-product
  "Recursive cartesian product of vectors in the factors sequence."
  ([factors working-product]
   (cond (empty? factors) [working-product]
         ;; Scalar value? Just append it and move onto the rest of the factors
         (not (coll? (first factors))) (cartesian-product (rest factors) (conj working-product (first factors)))
         ;; Ignore empty vectors. Could also see letting it return an empty vector for final product
         (empty? (first factors)) (cartesian-product (rest factors) working-product)
         ;; Continue the working product with each of the availble values in the first factor.
         :else (reduce into (map #(cartesian-product (rest factors) (conj working-product %)) (first factors)))))
  ([factors]
   (cartesian-product factors [])))

;;; Memoized cartesian product, but also without tail recursion.

(def cartesian-product-mem
  "Recursive cartesian product of vectors in the factors sequence."
  (memoize (fn [factors]
             (println "ran it")
             (cond (empty? factors) ['()]
                   ;; Scalar value? Just append it and move onto the rest of the factors
                   ;;(not (coll? (first factors))) (cartesian-product (rest factors) (conj working-product (first factors)))
                   ;; Ignore empty vectors. Could also see letting it return an empty vector for final product
                   ;;(empty? (first factors)) (cartesian-product (rest factors) working-product)

                   ;; Continue the working product with each of the availble values in the first factor.
                   :else (for [first-element (first factors)
                               cart-prod (cartesian-product-mem (rest factors))]
                           (conj cart-prod first-element))))))


(cartesian-product-mem [[1 2 10] [4 5 7] [7 8 9]])
