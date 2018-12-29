(ns adventofcode-2018.day7-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day7 :refer :all]))

(def instructions [[\C \A]
                   [\C \F]
                   [\A \B]
                   [\A \D]
                   [\B \E]
                   [\D \E]
                   [\F \E]])

(deftest instructions-to-steps-order-test
  (testing
    (is (= "CABDFE" (apply str (instructions-to-steps-order instructions))))))

(deftest steps-deps-test
  (testing
    (is (= {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}} (steps-deps instructions)))))

(deftest instructions-perform-time-test
  (testing
    (is (= 15 (instructions-perform-time instructions 2 0)))))
