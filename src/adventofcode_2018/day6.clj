(ns adventofcode-2018.day6
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

; Utils

(defn parse-point
  [s]
  (->> (str/split s #", ")
       (mapv #(Integer/parseInt %))))

(defn read-points
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         (mapv parse-point))))

(defn points-on-horizontal
  [y left right]
  (for [x (range left (inc right))]
    [x y]))

(defn points-on-vertical
  [x top bottom]
  (for [y (range top (inc bottom))]
    [x y]))

(defn points-on-bounds-edges
  [{:keys [left top right bottom]}]
  (concat (points-on-horizontal top left right)
          (points-on-vertical right top bottom)
          (points-on-horizontal bottom left right)
          (points-on-vertical left top bottom)))

(defn points-with-infinite-areas
  [closest-points-map bounds]
  (->> bounds
       points-on-bounds-edges
       (filter #(= 1 (count %)))
       (mapcat #(closest-points-map %))))

(defn points-with-finite-areas
  [points closest-points-map bounds]
  (->> points
       (remove (set (points-with-infinite-areas closest-points-map bounds)))))

(defn point-bounds
  [[x y]]
  {:left x, :top y, :right x, :bottom y})

(defn add-point-to-bounds
  [{:keys [left top right bottom]} [x y]]
  {:left (min left x)
   :top (min top y)
   :right (max right x)
   :bottom (max bottom y)})

(defn points-bounds
  [[first-point & rest-points]]
  (reduce add-point-to-bounds (point-bounds first-point) rest-points))

(defn distance
  [p1 p2]
  (->> (map #(Math/abs (- %1 %2)) p1 p2)
       (reduce +)))

(defn closest-points-to
  ([src-points dst-point] (closest-points-to src-points dst-point Integer/MAX_VALUE []))
  ([src-points dst-point min-distance closest-points]
   (if (seq src-points)
     (let [point (first src-points)
           distance (distance point dst-point)]
       (cond
         (< distance min-distance) (recur (rest src-points) dst-point distance [point])
         (= distance min-distance) (recur (rest src-points) dst-point min-distance (conj closest-points point))
         (> distance min-distance) (recur (rest src-points) dst-point min-distance closest-points)))
     closest-points)))

(defn closest-points-map
  [src-points dst-points]
  (->> dst-points
       (reduce (fn [result dst-point]
                 (assoc result dst-point (closest-points-to src-points dst-point)))
               {})))

(defn points-in-bounds
  [{:keys [left top right bottom]}]
  (for [y (range top (inc bottom))
        x (range left (inc right))]
    [x y]))

(defn closest-points-in-bounds-map
  [src-points bounds]
  (closest-points-map src-points (points-in-bounds bounds)))

(defn count-point-areas
  [closest-points-map]
  (->> closest-points-map
       vals
       (reduce (fn [result closest]
                 (if (= 1 (count closest))
                   (update result (first closest) (fnil inc 0))
                   result))
               {})))

(defn count-finite-point-areas
  [points]
  (let [bounds (points-bounds points)
        closest-points-map (closest-points-in-bounds-map points bounds)
        points-with-finite-areas (points-with-finite-areas points closest-points-map bounds)]
    (-> closest-points-map
        count-point-areas
        (select-keys points-with-finite-areas))))

(defn sum-distance-to
  [src-points dst-point]
  (->> src-points
       (map #(distance % dst-point))
       (reduce +)))

; Puzzles

(defn largest-area-size
  [points]
  (->> points
       count-finite-point-areas
       vals
       (reduce max 0)))

(defn count-points-with-max-distance-from
  [src-points max-distance]
  (->> src-points
       points-bounds
       points-in-bounds
       (map #(sum-distance-to src-points %))
       (filter #(< % max-distance))
       count))

(defn -main
  [& args]
  (println "--- Day 6: Chronal Coordinates ---")
  (let [points (read-points "inputs/day6.txt")]
    (println "  Part 1:")
    ; TODO: Optimize speed: it takes ~ 1 minute.
    (println "    The size of the largest area:" (largest-area-size points))
    (println "  Part 2:")
    ; TODO: Optimize speed: it takes ~ 1 minute.
    (println "    The size with d < 10000:" (count-points-with-max-distance-from points 10000))))
