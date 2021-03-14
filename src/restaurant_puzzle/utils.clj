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

;;; Paring down the output

;;; The solution probably isn't just a permutation of original words

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
  "True if a word chain lacks less than n of the base words.
   word-chain: [[homonymns] [homonymns2] ..]
   base-words: [w1 w1..]"
  [n base-words word-chain]
  ;; Technically, this won't count homonmyms in the base pair as separate
  ;; words, but that's probably fine here.
  (> n (count (clojure.set/difference (set (flatten base-words)) (set (flatten word-chain)))))
  )

;;; Only choose one word per homophone group
;;; This is a little tougher to interpret with something like the bland vowel dictionary

(defn choose-single-homophone
  "Return a word chain of containing just one word per homophone group."
  [word-chain]
  (map first word-chain))

(defn homophone-trim-all-word-chains
  "Pare down to one homophone in every word-chain in the list"
  [word-chains]
  (map choose-single-homophone word-chains))

;;; Trim permutations

(defn trim-permuted-word-chains
  "Only keep one permutation of each word-chain"
  [word-chains]
  (set (map set word-chains)))




;;; Just a couple cartesian product functions for fun. I use clojure.combinatorics/cartesian-product
;;; when I actually need it.

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
  (memoize
   (fn [factors]
     (cond (empty? factors) ['()]
           ;; Scalar value? Just append it and move onto the rest of the factors
           ;;(not (coll? (first factors))) (cartesian-product (rest factors) (conj working-product (first factors)))
           ;; Ignore empty vectors. Could also see letting it return an empty vector for final product
           ;;(empty? (first factors)) (cartesian-product (rest factors) working-product)

           ;; Continue the working product with each of the availble values in the first factor.
           :else (for [cart-prod (cartesian-product-mem (rest factors))
                       first-element (first factors)]
                   (conj cart-prod first-element))))))


(cartesian-product-mem [[1 2 10] [4 5 7] [7 8 9]])

