(ns adventofcode-2018.day1
  (:gen-class)
  (:require [clojure.java.io :as jio]))

(defn read-input
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         (mapv #(Integer/parseInt %)))))

(defn resulting-freq
  [freq-changes]
  (reduce + freq-changes))

(defn resulting-freqs
  ([freq-changes]
   (resulting-freqs freq-changes 0))
  ([freq-changes freq]
   (lazy-seq
     (let [[freq-change & rest-freq-changes] freq-changes
           next-freq (+ freq freq-change)]
       (cons freq (resulting-freqs rest-freq-changes next-freq))))))

(defn first-repeating-resulting-freq
  [freq-changes]
  (loop [freqs (resulting-freqs (cycle freq-changes))
         met-freqs #{}]
    (let [[freq & next-freqs] freqs]
      (if (met-freqs freq)
       freq
       (recur next-freqs (conj met-freqs freq))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [input (read-input "inputs/day1.txt")]
    (println "Resulting frequency:" (resulting-freq input))
    (println "First repeating resulting frequency:" (first-repeating-resulting-freq input))))
