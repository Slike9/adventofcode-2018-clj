(ns adventofcode-2018.day4
  (:require [clojure.java.io :as jio]))

; Utils

(defn parse-repose-record
  [repose-record]
  (let [[_ raw-minute message] (re-matches #"\[.+:(\d+)\] (.+)" repose-record)
        minute (Integer/parseInt raw-minute)]
    (if-let [[_ guard-id] (re-matches #"Guard #(\d+) begins shift" message)]
      {:minute minute, :message "begins shift", :guard-id (Integer/parseInt guard-id)}
      {:minute minute, :message message})))

(defn read-repose-records
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         sort
         (map parse-repose-record)
         (into []))))

(defn repose-records-to-sleep-intervals-by-guards
  ([repose-records] (repose-records-to-sleep-intervals-by-guards repose-records {} nil))
  ([repose-records sleep-intervals-by-guards guard-id]
   (if (seq repose-records)
     (let [record (first repose-records)]
       (case (:message record)
         "begins shift" (recur (rest repose-records) sleep-intervals-by-guards (:guard-id record))
         "falls asleep" (recur (drop 2 repose-records)
                               (update sleep-intervals-by-guards
                                       guard-id
                                       (fnil conj [])
                                       {:from (:minute record) :to (:minute (second repose-records))})
                               guard-id)))
     sleep-intervals-by-guards)))

(defn interval-length
  [interval]
  (- (:to interval) (:from interval) 1))

(defn sum-length-of-intervals
  [intervals]
  (->> intervals
       (map interval-length)
       (reduce +)))

(defn interval-include-number?
  [interval number]
  (and (<= (:from interval) number) (< number (:to interval))))

(defn count-intervals-including-number
  [intervals number]
  (->> intervals
       (filter #(interval-include-number? % number))
       count))

; Puzzles

(defn most-sleepy-guard-id
  [sleep-intervals-by-guards]
  (->> sleep-intervals-by-guards
       (reduce (partial max-key #(sum-length-of-intervals (val %))))
       key))

(defn most-frequent-minute-with-count
  [intervals]
  (->> (range 0 60)
       (map #(identity {:minute %, :count (count-intervals-including-number intervals %)}))
       (reduce (partial max-key :count))))

(defn most-frequent-minute
  [intervals]
  (->> intervals
       most-frequent-minute-with-count
       :minute))

(defn most-sleepy-minute-with-guard
  [sleep-intervals-by-guards]
  (->> sleep-intervals-by-guards
       (map #(assoc (most-frequent-minute-with-count (val %)) :guard-id (key %)))
       (reduce (partial max-key :count))))

(defn -main
  [& args]
  (println "--- Day 4: Repose Record ---")
  (let [repose-records (read-repose-records "inputs/day4.txt")
        sleep-intervals-by-guards (repose-records-to-sleep-intervals-by-guards repose-records)
        most-sleepy-guard-id (most-sleepy-guard-id sleep-intervals-by-guards)
        most-sleepy-minute-of-most-sleepy-guard (most-frequent-minute (get sleep-intervals-by-guards most-sleepy-guard-id))
        most-sleepy-minute-with-guard (most-sleepy-minute-with-guard sleep-intervals-by-guards)]
    (println "  Part 1:")
    (println "    Guard that has the most minutes asleep: ", most-sleepy-guard-id)
    (println "    Minute that guard spend asleep the most: ", most-sleepy-minute-of-most-sleepy-guard)
    (println "    Composition: ", (* most-sleepy-guard-id most-sleepy-minute-of-most-sleepy-guard))
    (println "  Part 2:")
    (println "    Most sleepy minute:", (:minute most-sleepy-minute-with-guard))
    (println "    Guard with most sleepy minute:", (:guard-id most-sleepy-minute-with-guard))
    (println "    Composition:" (* (:minute most-sleepy-minute-with-guard) (:guard-id most-sleepy-minute-with-guard)))))
