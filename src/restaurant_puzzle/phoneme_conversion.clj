(ns restaurant-puzzle.phoneme-conversion
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]))

;; For now I'll use the phoneme-list first search.
;; Alternative is to recursively search for first element in every word's
;; phoneme, then second, etc.
;; I operate with a list of word-chain pairs [word-chain remaining-phonemes].
;; An empty remaining-phonemes means the word-chain is finished.


(defn nil-phonemes?
  "True if the phoneme part of a word-chain pair is nil"
  [word-chain-pair]
  (empty? (second word-chain-pair)))

(defn new-word-chain
  "From a potential word chain and phoneme sequence, generate an updated word chain.
   remaining-phonemes is a pair of [phonemes-to-search leftover-phonemes].
   Returns pair of [new-word-chain leftover-phonemes]."
  [reverse-phoneme-dictionary word-chain remaining-phonemes]
  (let [search-phonemes (first remaining-phonemes)
        new-remaining-phonemes (second remaining-phonemes)]
     (if (empty? search-phonemes)
      [word-chain []]
      (if-let [new-word (pd/get-word-from-phonemes reverse-phoneme-dictionary search-phonemes)]
        [(conj word-chain new-word) new-remaining-phonemes]
        nil))))

(defn new-word-chains
  "From a potential word chain and phoneme sequence, generate a collection of updated
   word chains and phoneme sequences. Try the first n phonemes, up to the entire sequence."
  [reverse-phoneme-dictionary word-chain remaining-phonemes]
  (if (empty? remaining-phonemes)
    [[word-chain remaining-phonemes]]
    (remove nil?
            (map (partial new-word-chain reverse-phoneme-dictionary word-chain)
                 (for [x (range (count remaining-phonemes))]
                   (split-at (inc x) remaining-phonemes))))))

(defn update-word-chains
  "Update the list of possible words chains.
   Returns collection of [word-list remaining-phoneme] pairs."
  [reverse-phoneme-dictionary word-chain-list]
  (if (every? nil-phonemes? word-chain-list)
    word-chain-list
    (update-word-chains reverse-phoneme-dictionary 
                        (reduce into []
                                (map #(new-word-chains reverse-phoneme-dictionary (first %) (second %)) word-chain-list))))
  )


(defn old-update-word-chains
  "Update the list of possible words chains.
   Returns collection of [word-list remaining-phoneme] pairs."
  [reverse-phoneme-dictionary word-chain-list]
  (reduce conj []
          (map #(new-word-chains reverse-phoneme-dictionary (first %) (second %)) word-chain-list))
  )

(defn get-words-from-phonemes
  "Return collection of possible word sequences using all phonemes in sequence."
  [reverse-phoneme-dictionary phonemes]
  (update-word-chains reverse-phoneme-dictionary [[[] phonemes]])
  )


(defn string-from-word-chains
  "Return a string of just the possible word chains."
  [word-chains]
  (clojure.string/replace (reduce #(str %1 "\n" (clojure.string/join " " (first %2))) ""  word-chains)
                          #"\"" ""))


;;(new-word-chain (pd/get-reverse-phoneme-dictionary) ["test" "is"] [["IH" "N"] ["EH" "S" "T"]])
;;(new-word-chains (pd/get-reverse-phoneme-dictionary) ["test" "is"] ["T" "EH" "S" "T" "IH" "NG"])

(print (string-from-word-chains (get-words-from-phonemes (pd/get-reverse-phoneme-dictionary) ["T" "EH" "S" "T" "IH" "NG"])))
;;(old-update-word-chains (pd/get-reverse-phoneme-dictionary) [[["test" "is"] ["EH" "S" "T" "IH" "NG"]]])

