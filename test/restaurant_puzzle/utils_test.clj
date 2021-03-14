(ns restaurant-puzzle.utils-test
  (:require [clojure.test :refer :all]
            [restaurant-puzzle.utils :refer :all]))

(deftest uses-most-base-words?-test
  (testing "Uses most base words?"
    (are
        [v n base-words word-chain]
        (= v (uses-most-base-words? n base-words word-chain))
      false 1 [[["this" "is" "a" "test"]]] [[["this" "and"] ["a"] ["is"] "tes"]]
      true 3 [[["this" "is" "a" "test"]]] [[["this" "and"] ["a"] ["is"] "tes"]]
      )))

(def cart-prod-result
  '((1 4 7) (2 4 7) (10 4 7) (1 5 7) (2 5 7) (10 5 7) (1 7 7) (2 7 7) (10 7 7) (1 4 8) (2 4 8) (10 4 8) (1 5 8) (2 5 8) (10 5 8) (1 7 8) (2 7 8) (10 7 8) (1 4 9) (2 4 9) (10 4 9) (1 5 9) (2 5 9) (10 5 9) (1 7 9) (2 7 9) (10 7 9)))
(def cart-prod-factors [[1 2 10] [4 5 7] [7 8 9]])

(deftest cartesian-product-test
  (testing "First cartesian product fn"
    (are
        [prod factors]
        (= (set prod) (set (cartesian-product factors)))
      cart-prod-result
      cart-prod-factors
      [[1 1] [1 2] [2 1] [2 2]]
      [[1 2] [1 2]])))


(deftest cartesian-product-mem-test
  (testing "First cartesian product fn"
    (are
        [prod factors]
        (= (set prod) (set (cartesian-product-mem factors)))
      cart-prod-result
      cart-prod-factors
      [[1 1] [1 2] [2 1] [2 2]]
      [[1 2] [1 2]])))

