(ns adventofcode-2018.day9)

(defprotocol PDoubleLinkedListNode
  (get-prev [this])
  (set-prev [this node])
  (get-next [this])
  (set-next [this node]))

(deftype DoubleLinkedListNode [^:unsynchronized-mutable prev, ^:unsynchronized-mutable next, data]
  PDoubleLinkedListNode
  (get-prev [this] prev)
  (set-prev [this node]
    (set! prev node))
  (get-next [this] next)
  (set-next [this node]
    (set! next node)))

(defn init-marble-circle
  []
  (let [node (->DoubleLinkedListNode nil nil 0)]
    (doto node
      (.set-prev node)
      (.set-next node))))

(defn place-marble-to-circle
  [circle marble]
  (let [first-node (get-next circle)
        second-node (get-next first-node)
        new-node (->DoubleLinkedListNode first-node second-node marble)]
    (set-next first-node new-node)
    (set-prev second-node new-node)
    new-node))

(defn marble-circle-7th-prev
  [circle]
  (loop [i 7, node circle]
    (if (= 0 i)
      node
      (recur (dec i) (get-prev node)))))

(defn remove-current-marble-from-circle
  [circle]
  (let [prev (get-prev circle)
        next (get-next circle)]
    (set-next prev next)
    (set-prev next prev)
    next))

(defn remove-7th-marble-from-circle
  [circle]
  (let [removed-node (marble-circle-7th-prev circle)]
    [(remove-current-marble-from-circle removed-node) (.data removed-node)]))

(defn marble-circle-to-seq
  [circle]
  (->> circle
       (iterate get-next)
       (map #(.data %))))

(defn marble-mania-turn
  [circle marble]
  (if (= 0 (mod marble 23))
    (let [[circle removed-marble] (remove-7th-marble-from-circle circle)]
      [circle (+ marble removed-marble)])
    [(place-marble-to-circle circle marble) 0]))

(defn marble-mania-winning-score
  [player-count last-marble]
  (loop [player 0
         marble 1
         circle (init-marble-circle)
         scores {}]
    (if (<= marble last-marble)
      (let [[next-circle score] (marble-mania-turn circle marble)]
        (recur (mod (inc player) player-count)
               (inc marble)
               next-circle
               (update scores player (fnil + 0) score)))
      (reduce max (vals scores)))))

(defn -main
  [& args]
  (println "Day 9: Marble Mania")
  (let [player-count 411
        last-marble 72059]
    (println "  Part 1. The winning Elf's score:" (marble-mania-winning-score player-count last-marble))
    (println "  Part 2. The winning Elf's score:" (marble-mania-winning-score player-count (* last-marble 100)))))
