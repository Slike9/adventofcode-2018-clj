(ns adventofcode-2018.day2
  (:require [clojure.java.io :as jio]))

; Utils

(defn read-box-ids
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         (into []))))

(defn contains-any-char-n-times?
  [s n]
  (->> s
       frequencies
       (some #(= (val %) n))))

(defn count-contain-any-char-n-times
  [coll n]
  (->> coll
       (filter #(contains-any-char-n-times? % n))
       count))

(defn strs-differ-by-one-char?
  [s1 s2]
  (let [length (count s1)]
    (loop [i 0 diffs 0]
      (cond (> diffs 1) false
            (= i length) (= diffs 1)
            (< i length) (recur (inc i) (if (= (nth s1 i) (nth s2 i)) diffs (inc diffs)))))))

(defn find-strs-diffrent-by-one-char
  [coll]
  (for [i (range (count coll))
        j (range (inc i) (count coll))
        :let [si (nth coll i)
              sj (nth coll j)]
        :when (strs-differ-by-one-char? si sj)]
    [si sj]))

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
