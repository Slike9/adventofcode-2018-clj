(ns adventofcode-2018.day7
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

; Utils

(defn parse-instruction
  [s]
  (let [[_ [before] [after]] (re-matches #"Step (\w) must be finished before step (\w) can begin." s)]
    [before after]))

(defn read-instructions
  [file-path]
  (with-open [r (jio/reader file-path)]
    (->> r
         line-seq
         (mapv parse-instruction))))

(defn instructions-steps
  [instructions]
  (->> instructions
       flatten
       distinct))

(defn- init-steps-deps
  [steps]
  (reduce #(assoc %1 %2 #{}) {} steps))

(defn steps-deps
  [instructions]
  (reduce (fn [result [before after]]
            (update result after conj before))
          (init-steps-deps (instructions-steps instructions))
          instructions))

(defn ready-steps
  [steps-deps]
  (->> steps-deps
       (filter #(empty? (val %)))
       (map key)
       sort))

(defn perform-step
  [steps-deps step]
  (reduce-kv (fn [m k v]
               (if (= k step)
                 m
                 (assoc m k (disj v step))))
             {}
             steps-deps))

(defn steps-deps-to-steps-order
  ([steps-deps] (steps-deps-to-steps-order steps-deps []))
  ([steps-deps result]
   (if (seq steps-deps)
     (let [ready-step (->> steps-deps
                           ready-steps
                           first)]
       (recur (perform-step steps-deps ready-step) (conj result ready-step) ))
     result)))

(defn step-time
  [step base-time]
  (+ base-time 1 (- (int step) (int \A))))

(defn remove-item-from-waiting-steps
  [waiting-steps step]
  (dissoc waiting-steps step))

(defn add-item-to-processing-steps
  [processing-steps step step-base-time]
  (conj processing-steps {:step step, :time-to-finish (step-time step step-base-time)}))

(defn remove-dep-from-steps-deps
  [steps-deps step]
  (reduce-kv (fn [m k v]
               (assoc m k (disj v step)))
             {}
             steps-deps))

(defn peek-done-from-processing-steps
  [processing-steps]
  (->> processing-steps
       (filter #(= 0 (:time-to-finish %)))
       (map :step)))

(defn assign-steps-to-process
  [waiting-steps processing-steps worker-count step-base-time]
  (let [free-worker-count (- worker-count (count processing-steps))
        assigning-steps (take free-worker-count (ready-steps waiting-steps))
        new-waiting-steps (reduce remove-item-from-waiting-steps waiting-steps assigning-steps)
        new-processing-steps (reduce #(add-item-to-processing-steps %1 %2 step-base-time)
                                     processing-steps
                                     assigning-steps)]
    [new-waiting-steps new-processing-steps]))

(defn process-step
  [processing-step process-time]
  (update processing-step :time-to-finish - process-time))

(defn process-steps
  [waiting-steps processing-steps]
  (let [min-time-to-finish (->> processing-steps
                                (map :time-to-finish)
                                (reduce min))
        processed-steps (map #(process-step % min-time-to-finish) processing-steps)
        done-steps (set (peek-done-from-processing-steps processed-steps))
        new-waiting-steps (reduce remove-dep-from-steps-deps waiting-steps done-steps)
        new-processing-steps (remove #(done-steps (:step %)) processed-steps)]
    [new-waiting-steps new-processing-steps min-time-to-finish]))

; Puzzles

(defn instructions-to-steps-order
  [instructions]
  (->> instructions
       steps-deps
       steps-deps-to-steps-order))

(defn instructions-process-time
  [instructions worker-count step-base-time]
  (loop [current-time 0
         waiting-steps (steps-deps instructions)
         processing-steps []]
    (let [[waiting-steps processing-steps] (assign-steps-to-process waiting-steps
                                                                    processing-steps
                                                                    worker-count
                                                                    step-base-time)]
      (if (seq processing-steps)
        (let [[waiting-steps processing-steps process-time] (process-steps waiting-steps processing-steps)]
          (recur (+ current-time process-time) waiting-steps processing-steps))
        current-time))))

(defn -main
  [& args]
  (println "--- Day 7: The Sum of Its Parts ---")
  (let [instructions (read-instructions "inputs/day7.txt")
        worker-count 5
        step-base-time 60]
    (println "  Part 1:")
    (println "    Steps order" (apply str (instructions-to-steps-order instructions)))
    (println "  Part 2:")
    (println "    Processing time" (instructions-process-time instructions worker-count step-base-time))))
