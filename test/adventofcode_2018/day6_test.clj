(ns adventofcode-2018.day6-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day6 :refer :all]))

(def coordinates [[1, 1]
                  [1, 6]
                  [8, 3]
                  [3, 4]
                  [5, 5]
                  [8, 9]])

(deftest largest-area-size-test
  (testing
    (is (= 17 (largest-area-size coordinates)))))
