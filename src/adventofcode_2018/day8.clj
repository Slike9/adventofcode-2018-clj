(ns adventofcode-2018.day8
  (:require [clojure.string :as str]))

; Utils

(defn read-license
  [file-path]
  (->> file-path
       slurp
       (#(str/split % #"\s"))
       (map #(Integer/parseInt %))))

; Puzzles

(declare sum-meta-of-license-node)

(defn sum-meta-of-license-nodes
  [license-nums node-count]
  (reduce (fn [[all-meta-sum nums] _]
            (let [[node-meta-sum other-nums] (sum-meta-of-license-node nums)]
              [(+ all-meta-sum node-meta-sum) other-nums]))
          [0 license-nums]
          (range node-count)))

(defn sum-meta-of-license-node
  [license-nums]
  (let [[child-count meta-count & license-nums] license-nums
        [children-meta-sum license-nums] (sum-meta-of-license-nodes license-nums child-count)
        [node-meta-coll license-nums] (split-at meta-count license-nums)]
    [(reduce + children-meta-sum node-meta-coll) license-nums]))

(defn sum-license-meta
  [license-nums]
  (->> license-nums
       sum-meta-of-license-node
       first))

(declare license-node-value)

(defn values-of-license-nodes
  [license-nums node-count]
  (reduce (fn [[values nums] _]
            (let [[node-value nums] (license-node-value nums)]
              [(conj values node-value) nums]))
          [[] license-nums]
          (range node-count)))

(defn value-of-license-node-with-children
  [license-nums]
  (let [[child-count meta-count & other-nums] license-nums
        [child-node-values other-nums] (values-of-license-nodes other-nums child-count)
        [node-meta-coll other-nums] (split-at meta-count other-nums)
        node-value (->> node-meta-coll
                        (map #(get child-node-values (dec %) 0))
                        (reduce + 0))]
    [node-value other-nums]))

(defn value-of-license-node-without-children
  [license-nums]
  (let [[_ meta-count & other-nums] license-nums
        [meta-coll other-nums] (split-at meta-count other-nums)]
    [(reduce + 0 meta-coll) other-nums]))

(defn license-node-value
  [license-nums]
  (let [child-count (first license-nums)]
    (if (= 0 child-count)
      (value-of-license-node-without-children license-nums)
      (value-of-license-node-with-children license-nums))))

(defn license-root-value
  [license]
  (->> license
       license-node-value
       first))

(defn -main
  [& args]
  (println "--- Day 8: Memory Maneuver ---")
  (let [license (read-license "inputs/day8.txt")]
    (println "  Part 1:")
    (println "    Sum of metadata:" (sum-license-meta license))
    (println "  Part 2:")
    (println "    License root value:" (license-root-value license))))
