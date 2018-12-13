(ns adventofcode-2018.day4-test
  (:require [clojure.test :refer :all]
            [adventofcode-2018.day4 :refer :all]))

(deftest parse-repose-record-test
  (testing
    (is (= {:minute 5, :message "falls asleep"}
           (parse-repose-record "[1518-11-01 00:05] falls asleep")))
    (is (= {:minute 0, :message "begins shift", :guard-id 10}
           (parse-repose-record "[1518-11-01 00:00] Guard #10 begins shift")))))

(deftest repose-records-to-sleep-intervals-by-guards-test
  (testing
    (is (= {10 [{:from 5, :to 10}]}
           (repose-records-to-sleep-intervals-by-guards [{:minute 0, :message "begins shift", :guard-id 10}
                                                         {:minute 5, :message "falls asleep"}
                                                         {:minute 10, :message "wakes up"}])))))

(def sleep-intervals-by-guards {10 [{:from 5, :to 25}, {:from 30, :to 55}, {:from 24, :to 29}]
                                99 [{:from 40, :to 50}, {:from 36, :to 46}, {:from 45, :to 55}]})

(deftest most-sleepy-guard-id-test
  (testing
    (is (= 10 (most-sleepy-guard-id sleep-intervals-by-guards)))))

(deftest most-sleepy-minute-with-guard-test
  (testing
    (is (= {:minute 45, :guard-id 99, :count 3} (most-sleepy-minute-with-guard sleep-intervals-by-guards)))))
