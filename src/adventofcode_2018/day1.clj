(ns adventofcode-2018.day1
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
   (if (seq coll)
     (cons init (lazy-seq (cumulative-sums (rest coll) (+ init (first coll)))))
     (list init))))

(defn first-duplicate
  ([coll] (first-duplicate coll #{}))
  ([coll met]
   (if (seq coll)
     (let [x (first coll)]
       (if (met x)
         x
         (recur (rest coll) (conj met x))))
     nil)))

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
