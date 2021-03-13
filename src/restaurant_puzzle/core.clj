(ns restaurant-puzzle.core
  (:gen-class)
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]
            [restaurant-puzzle.utils :as utils]
            [restaurant-puzzle.phoneme-conversion :as pc]
            [clojure.math.combinatorics :as combo]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.pprint]
            ))

(defn write-possible-answers
  "Output the possible answers"
  [phoneme-dictionary reverse-phoneme-dictionary clue-word-file answer-file base-words]
   ;; Only looks at first line of clue-word-file
  (->> (utils/read-clue-words clue-word-file) ; clue word-lists
       (utils/words-to-phonemes phoneme-dictionary) ; clue phoneme lists
       first ; first line only
       (apply combo/cartesian-product) ; all pronunciations per word
       (map combo/permutations) ; all combinations of words
       (reduce into)
       (map flatten)
       ;; Get possible words from the phoneme lists
       (map (partial pc/get-words-from-phonemes reverse-phoneme-dictionary))
       (reduce into)
       ;; Remove entries with that still use almost all of the base words
       (remove (partial utils/uses-most-base-words? base-words (utils/read-clue-words)))
       (pc/string-from-word-chains)
       (spit answer-file)
       ))

(def cli-options
  "Options for command line arguments in running -main"
  [["-d" "--dictionary DICTIONARY FILE" "Dictionary file location"
    :default "resources/bland-vowel-dict"]
   ["-c" "--clues CLUE FILE" "Clue file location"
    :default "resources/clue_words.txt"]
   ["-a" "--answers ANSWER FILE" "File to write answers to (overwrite)"
    :default "resources/answers.txt"]
   ["-b" "--base-words BASE WORDS" "Allow all but b base words in answers"
    :parse-fn #(Integer/parseInt %)
    :default 1]
   ["-h" "--help" "Display usage information."
    :default nil]])

(defn validate-arguments
  "Validate command-line arguments."
  [args]
  (let [{:keys [options arguments errors summary]} args]
    (cond
      (:help options)
      {:exit-message summary}
      :else {:options options})))

(defn -main
  [& args]
  ;; Get dictionary and reverse dictionary.
  (let [{:keys [options exit-message]} (validate-arguments (parse-opts args cli-options))]
    (clojure.pprint/pprint options)
    (if exit-message
      (println exit-message)
      (let [{:keys [dictionary clues answers base-words]} options
            phoneme-dictionary (pd/get-phoneme-dictionary dictionary)
            reverse-phoneme-dictionary (pd/get-reverse-phoneme-dictionary dictionary)]
        (write-possible-answers phoneme-dictionary reverse-phoneme-dictionary clues answers base-words)))

    ))
