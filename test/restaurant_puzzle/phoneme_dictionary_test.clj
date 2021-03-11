(ns restaurant-puzzle.phoneme-dictionary-test
  (:require [clojure.test :refer :all]
            [restaurant-puzzle.phoneme-dictionary :refer :all]))

(deftest get-phonemes-from-word-test
  (testing "Get phonemes from word."
    (is (= '(("T" "EH" "S" "T"))
         (get-phonemes-from-word (get-phoneme-dictionary) "test")))))

(deftest get-word-from-phonemes-test
  (testing "Get word from phonemes."
    (is (= ["TEST"]
           (get-word-from-phonemes (get-reverse-phoneme-dictionary) ["T" "EH" "S" "T"])))))
