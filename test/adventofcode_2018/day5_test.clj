(ns adventofcode-2018.day5-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day5 :refer :all]))

(def polymer "dabAcCaCBAcCcaDA")

(deftest reduce-polymer-test
  (testing
    (is (= (seq "dabCBAcaDA") (reduce-polymer polymer)))))

(deftest remove-unit-type-from-polymer-test
  (testing
    (is (= (seq "abAcCaCBAcCcaA") (remove-unit-type-from-polymer polymer \d)))))

(deftest shortest-polymer-length-after-removing-a-unit-test
  (testing
    (is (= 4 (shortest-polymer-length-after-removing-a-unit polymer)))))
