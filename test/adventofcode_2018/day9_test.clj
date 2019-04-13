(ns adventofcode-2018.day9-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day9 :refer :all]))

(deftest marble-mania-turn-test
  (testing
    (let [[new-circle score] (marble-mania-turn (init-marble-circle) 1)
          marbles (->> new-circle marble-circle-to-seq (take 2))]
      (is (= [[1 0] 0] [marbles score])))))

(deftest marble-mania-winning-score-test
  (testing
    (is (= 32 (marble-mania-winning-score 9 25)) )))
