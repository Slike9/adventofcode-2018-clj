(ns adventofcode-2018.day2-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day2 :refer :all]))

(deftest contains-any-char-n-times?-test
  (testing
    (is (contains-any-char-n-times? "abada", 3))))

(deftest count-contain-any-char-n-times-test
  (testing
    (is (= 2 (count-contain-any-char-n-times ["aabc", "abcd", "bbca"] 2)))))

(deftest count-different-chars-test
  (testing
    (is (= 1 (count-different-chars "abcd" "accd")))))

(deftest find-strs-diffrent-by-one-char-test
  (testing
    (is (= [["abc", "acc"]] (find-strs-diffrent-by-one-char ["abc", "dde", "acc"])))))

(deftest common-chars-test
  (testing
    (is (= [\a \b \c] (common-chars "abdc", "abcc")))))
