(ns restaurant-puzzle.phoneme-conversion-test
  (:require [clojure.test :refer :all]
            [restaurant-puzzle.phoneme-conversion :refer :all]
            [restaurant-puzzle.phoneme-dictionary :as pd]))

(deftest get-words-from-phonemes-test
  (testing "Words from phonemes."
    (is (= [['("TEST")]]
           (get-words-from-phonemes (pd/get-reverse-phoneme-dictionary) '("T" "EH" "S" "T")))
        )))

(deftest string-from-word-chains-test
  (testing "Compose solutions into one string."
    (is (= "(TEST TESST) (ANOTHER OTHER)\n"
           (string-from-word-chains [['("TEST" "TESST") '("ANOTHER" "OTHER")]])))))

(deftest nil-phonemes?-test
  (testing "Predicate function"
    (are [x y] (= x (nil-phonemes? y))
      true [['("word word")]]
      false [['("wword word")] ["phonemes"]])))
