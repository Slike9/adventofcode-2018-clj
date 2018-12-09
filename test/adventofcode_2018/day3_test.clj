(ns adventofcode-2018.day3-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day3 :refer :all]))

(deftest parse-fabric-area-claim-test
  (testing
    (is (= {:id 1, :left 604, :top 670, :width 22, :height 16}
           (parse-fabric-area-claim "#1 @ 604,670: 22x16")))))

(deftest fabric-claim-inches-test
  (testing
    (is (= [[0 0] [1 0]] (fabric-claim-inches {:left 0, :top 0, :width 2, :height 1})))))

(deftest fabric-claim-layout-test
  (testing
    (is (= {[0 0] 1, [1 0] 1} (fabric-claim-layout {:left 0, :top 0, :width 2, :height 1})))))

(deftest overlapped-square-inches-count-test
  (testing
    (is (= 4 (overlapped-square-inches-count [{:id 1, :left 1, :top 3, :width 4, :height 4}
                                              {:id 2, :left 3, :top 1, :width 4, :height 4}
                                              {:id 3, :left 5, :top 5, :width 2, :height 2}])))))

(deftest fabric-claims-not-overlapped-test
  (testing
    (is (= [{:id 3, :left 5, :top 5, :width 2, :height 2}]
           (fabric-claims-not-overlapped [{:id 1, :left 1, :top 3, :width 4, :height 4}
                                          {:id 2, :left 3, :top 1, :width 4, :height 4}
                                          {:id 3, :left 5, :top 5, :width 2, :height 2}])))))
