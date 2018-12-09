(ns adventofcode-2018.day3
  (:require [clojure.java.io :as jio]))

; Utils

(defn parse-fabric-area-claim
  [s]
  (->> s
       (re-matches #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")
       rest
       (map #(Integer/parseInt %))
       (zipmap [:id :left :top :width :height])))

(defn read-fabric-area-claims
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         (map parse-fabric-area-claim)
         (into []))))

(defn fabric-claim-right
  [fabric-claim]
  (+ (:left fabric-claim) (:width fabric-claim)))

(defn fabric-claim-bottom
  [fabric-claim]
  (+ (:top fabric-claim) (:height fabric-claim)))

(defn fabric-claim-inches
  [fabric-claim]
  (for [x (range (:left fabric-claim) (fabric-claim-right fabric-claim))
        y (range (:top fabric-claim) (fabric-claim-bottom fabric-claim))]
    [x y]))

(defn fabric-claim-layout
  [fabric-claim]
  (->> fabric-claim
       fabric-claim-inches
       (reduce #(assoc %1 %2 1) {})))

(def merge-fabric-claim-layouts (partial merge-with +))

(defn layout-of-fabric-claims
  [fabric-claims]
  (->> fabric-claims
       (map fabric-claim-layout)
       (reduce merge-fabric-claim-layouts {})))

(defn fabric-claim-not-overlapped?
  [fabric-claim layout]
  (->> fabric-claim
       fabric-claim-inches
       (every? #(= (layout %) 1))))

(defn fabric-claims-not-overlapped
  [fabric-claims]
  (let [layout (layout-of-fabric-claims fabric-claims)]
    (->> fabric-claims
         (filter #(fabric-claim-not-overlapped? % layout)))))

; Puzzles

(defn overlapped-square-inches-count
  [fabric-claims]
  (->> fabric-claims
       layout-of-fabric-claims
       (filter #(> (val %) 1))
       count))

(defn id-of-first-not-overlapped-claim
  [fabric-claims]
  (->> fabric-claims
       fabric-claims-not-overlapped
       first
       :id))

(defn -main
  [& args]
  (println "--- Day 3: No Matter How You Slice It ---")
  (let [fabric-claims (read-fabric-area-claims "inputs/day3.txt")]
    (println "Square inches of fabric within two or more claims:" (overlapped-square-inches-count fabric-claims))
    (println "The ID of the only claim that doesn't overlap:" (id-of-first-not-overlapped-claim fabric-claims))))
