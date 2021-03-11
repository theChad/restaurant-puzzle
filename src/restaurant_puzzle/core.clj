(ns restaurant-puzzle.core
  (:gen-class)
  (:require [restaurant-puzzle.phoneme-dictionary :as pd]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (pd/get-phoneme-dictionary "resources/cmudict-0.7b"))
