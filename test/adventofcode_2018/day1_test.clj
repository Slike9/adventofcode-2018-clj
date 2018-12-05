(ns adventofcode-2018.day1-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day1 :refer :all]))

(deftest resulting-freq-test
  (testing
    (is (= 3 (resulting-freq [+1 +1 +1])))))

(deftest first-repeating-resulting-freq-test
  (testing
    (is (= 10 (first-repeating-resulting-freq [+3 +3 +4 -2 -4])))))
