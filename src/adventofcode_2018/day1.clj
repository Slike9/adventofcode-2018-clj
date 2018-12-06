(ns adventofcode-2018.day1
  (:gen-class)
  (:require [clojure.java.io :as jio]))

; Utils

(defn read-input
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         (mapv #(Integer/parseInt %)))))

(defn cumulative-sums
  ([coll] (cumulative-sums coll 0))
  ([coll init]
   (if (empty? coll)
     init
     (lazy-seq
       (let [[x & more] coll]
         (cons init (cumulative-sums more (+ init x))))))))

(defn first-duplicate
  ([coll] (first-duplicate coll #{}))
  ([coll met]
   (if (empty? coll)
     nil
     (let [[x & more] coll]
       (if (met x)
         x
         (recur more (conj met x)))))))

; Puzzles

(defn resulting-freq
  [freq-changes]
  (reduce + freq-changes))

(defn first-repeating-resulting-freq
  [freq-changes]
  (->> freq-changes
       cycle
       cumulative-sums
       first-duplicate))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "--- Day 1: Chronal Calibration ---")
  (let [input (read-input "inputs/day1.txt")]
    (println "Resulting frequency:" (resulting-freq input))
    (println "First repeating resulting frequency:" (first-repeating-resulting-freq input))))
