(ns adventofcode-2018.day5
  (:require [clojure.java.io :as jio]))

; Utils

(defn read-polymer
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         first)))

(defn char-swap-case
  [ch]
  (if (Character/isUpperCase ch)
    (Character/toLowerCase ch)
    (Character/toUpperCase ch)))

(defn opposite-units?
  [unit-1 unit-2]
  (= (char-swap-case unit-1) unit-2))

(defn same-unit-types?
  [unit-1 unit-2]
  (or (= unit-1 unit-2) (opposite-units? unit-1 unit-2)))

(defn char-range
  [from to]
  (->> (range (int from) (inc (int to)))
       (map char)))

(defn remove-unit-type-from-polymer
  [polymer unit]
  (remove (partial same-unit-types? unit) polymer))

; Puzzles

(defn reduce-polymer
  [polymer]
  (loop [left (), right polymer]
    (if (seq right)
      (if (and (seq left) (opposite-units? (first left) (first right)))
        (recur (rest left) (rest right))
        (recur (cons (first right) left) (rest right)))
      (reverse left))))

(defn shortest-polymer-length-after-removing-a-unit
  [polymer]
  (->> (char-range \a \z)
       (map (partial remove-unit-type-from-polymer polymer))
       (map reduce-polymer)
       (map count)
       (reduce min)))

(defn -main
  [& args]
  (println "--- Day 5: Alchemical Reduction ---")
  (let [polymer (read-polymer "inputs/day5.txt")]
    (println "  Part 1:")
    (println "    Reduced polymer size:", (count (reduce-polymer polymer)))
    (println "  Part 2:")
    (println "    Length of the shortest polymer:", (shortest-polymer-length-after-removing-a-unit polymer))))
