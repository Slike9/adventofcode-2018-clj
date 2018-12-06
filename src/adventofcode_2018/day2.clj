(ns adventofcode-2018.day2
  (:gen-class)
  (:require [clojure.java.io :as jio]))

; Utils

(defn read-box-ids
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         doall)))

(defn contains-any-char-n-times?
  [s n]
  (->> s
       frequencies
       (some #(= (val %) n))))

(defn count-predicate
  [f coll]
  (reduce #(if (f %2) (inc %1) %1) 0 coll))

(defn count-contain-any-char-n-times
  [coll n]
  (count-predicate #(contains-any-char-n-times? % n) coll))

(defn count-different-chars
  [s1 s2]
  (->> (range (.length s1))
       (count-predicate #(not (= (.charAt s1 %) (.charAt s2 %))))))

(defn less-than?
  [o1 o2]
  (neg? (compare o1 o2)))

(defn find-strs-diffrent-by-one-char
  [coll]
  (for [s1 coll
        s2 coll
        :when (and (less-than? s1 s2) (= (count-different-chars s1 s2) 1))]
    [s1 s2]))

(defn common-chars
  [s1 s2]
  (->> (map #(when (= %1 %2) %1) s1 s2)
       (filter identity)))

; Puzzles

(defn box-ids-checksum
  [ids]
  (* (count-contain-any-char-n-times ids 2) (count-contain-any-char-n-times ids 3)))

(defn correct-box-ids-common-chars
  [box-ids]
  (->> (find-strs-diffrent-by-one-char box-ids)
       first
       (apply common-chars)
       (apply str)))

(defn -main
  [& args]
  (println "--- Day 2: Inventory Management System ---")
  (let [box-ids (read-box-ids "inputs/day2.txt")]
    (println "  Part 1: Box IDs checksum:", (box-ids-checksum box-ids))
    (println "  Part 2: Common letters between the two correct box IDs:", (correct-box-ids-common-chars box-ids))))
