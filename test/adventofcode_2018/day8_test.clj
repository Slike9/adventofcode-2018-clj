(ns adventofcode-2018.day8-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day8 :refer :all]))

(def license [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2])

(deftest sum-license-meta-test
  (testing
    (is (= 138 (sum-license-meta license)))))

(deftest license-root-value-test
  (testing
    (is (= 66 (license-root-value license)))))
