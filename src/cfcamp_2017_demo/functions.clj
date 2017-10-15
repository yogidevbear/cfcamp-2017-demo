(ns cfcamp-2017-demo.functions)

;; ===========================================
;; (reduce f coll)
;; (reduce f val coll)
;; The backbone of many of the Clojure sequence functions

(defn my-reduce-val-coll [f val coll]
	(if (seq coll)
		(my-reduce-val-coll f (f val (first coll)) (rest coll))
		val))
(range 10)
(my-reduce-val-coll + 0 (range 10))

;; The recursion consumes stack space
(my-reduce-val-coll + 0 (range 10000))

(defn my-reduce-val-coll-recur [f val coll]
	(if (seq coll)
		(recur f (f val (first coll)) (rest coll))
		val))
(my-reduce-val-coll-recur + 0 (range 10))
(my-reduce-val-coll-recur + 0 (range 10000000))
;; Using (recur) gives us tail-call optimization:
;; constant-space recursive looping by rebinding & jumping to
;; the nearest enclosing loop or function frame

;; What happens if we don't pass in the val argument? (reduce f coll)
(my-reduce-val-coll-recur + (range 10))

;; Add in extra arity
(defn my-reduce-final
	([f coll]
		(if (seq coll)
	 		(my-reduce-final f (first coll) (rest coll))
	 		(f)))
	([f val coll]
		(if (seq coll)
			(recur f (f val (first coll)) (rest coll))
			val)))
;; Now we can mimic (reduce f coll)
(my-reduce-final + (range 10))
(my-reduce-final + (range 10000000))

;; Finally, if val is provided and coll is empty
;; or if val is omitted and coll has just a single element
;; returns val or first element of coll (respectively)
;; f is not called
(my-reduce-final + (list 3))
(my-reduce-final + 3 ())


;; ===========================================
;; (count coll)
;; Returns the number of items in the collection.
;; (count nil) returns 0.
;; Also works on strings, arrays, and Java Collections and Maps.

(defn my-count [coll] ;; (f coll)
  (reduce (fn [result _] (inc result)) 0 coll))
	;; (reduce f val coll)

(my-count ())
(my-count (range 10))
(my-count [1 "two" 3.0 4/5])
(my-count {:a 1 :b 2.0 :c "three"})

;; Note: This implementation is O(n)
;; Some of the built-in functions are implemented as O(1)
;;   if the collection is Counted
;; Vector, hash-map (possibly other types too)
;;   implement clojure.lang.Counted
;; An interface that says "I know how many elements I contain"

;; ===========================================
;; (filter pred)
;; (filter pred coll)
;; Returns a lazy sequence of the items in coll
;;   for which (pred item) returns true.
;; pred must be free of side-effects.
;; Returns a transducer when no collection is provided.

;; lazy-seq: macro to make a lazy sequence
;; Takes a body of expressions that return an ISeq or nil
;; Yields a Seqable object that will invoke the body only
;;   the first time seq is called
;; Caches the result and returns that on all subsequent seq calls

;; Bad use of lazy-seq
;; (only making the final result lazy)
(defn silly-lazy-filter [pred coll]
	(loop [input coll result []]
		(if (seq input)
			(recur 	(rest input)
				(if (pred (first input))
					(conj result (first input))
					result))
			(lazy-seq result))))
(silly-lazy-filter even? (range 10))
(take 2 (silly-lazy-filter even? (range 10000000)))

;; Better lazy-seq filter implementation
;; We want the computation to be lazy
(defn my-lazy-filter [pred coll]
	(lazy-seq
		(when (seq coll)
			(if (pred (first coll))
				(cons (first coll) (my-lazy-filter pred (rest coll)))
				(my-lazy-filter pred (rest coll))))))
(my-lazy-filter even? (range 10))
(take 2 (my-lazy-filter even? (range 10000000)))

;; Note: filter will always return a lazy seq
;; rather than the input coll type
(my-lazy-filter even? [0 1 2 3 4 5 6 7 8 9])
;; Use (into) and coll type to keep working in the same coll type
(into [] (my-lazy-filter even? [0 1 2 3 4 5 6 7 8 9]))


;; ===========================================
;; (map f)
;; (map f coll)
;; (map f c1 c2)
;; (map f c1 c2 c3)
;; (map f c1 c2 c3 & colls)

;; Returns a lazy-seq:
;; result of applying f to the set of first items of each coll,
;; followed by applying f to the set of second items in each coll,
;; until any one of the colls is exhausted.
;; Any remaining items in other colls are ignored.
;; Function f should accept number-of-colls arguments.
;; Returns a transducer when no collection is provided.

;; first arity to accept a single collection
(defn my-map-1 [f coll]
		(lazy-seq (when (seq coll)
				(cons (f (first coll)) (my-map-1 f (rest coll))))))
(my-map-1 (fn [n] (* n 2)) (range 5))

;; add 2nd arity to accept 2 collections
(defn my-map-2
	([f coll]
		(lazy-seq (when (seq coll)
				(cons (f (first coll)) (my-map-2 f (rest coll))))))
	([f c1 c2]
		(lazy-seq (when (and (seq c1) (seq c2))
				(cons (f (first c1) (first c2)) (my-map-2 f (rest c1) (rest c2)))))))
(my-map-2
  (fn [i1 i2] (* i1 i2))
    (my-lazy-filter odd? (rest (range 11)))
    (my-lazy-filter even? (rest (range 11))))

;; Before we expand on this further...
;; (every? pred coll)
;; Returns true if (pred x) is logical true for every x in coll, else false.
(every? seq [ [1 2 3] [4 5 6] ])
(every? seq [ [1 2 3] [] ])
(seq [])

;; Now let's update the map function to allow for n collections

;; We need to declare my-map before we define reorder
;; as the two functions are mutually recursive
(declare my-map)
;; Now we can define reorder which references my-map
(defn reorder [c]
  (when (every? seq c)
    (cons (my-map first c) (reorder (my-map rest c)))))
;; Finally, we can define my-map which is recursively using reorder
(defn my-map
	([f coll]
  	(lazy-seq (when (seq coll)
			(cons (f (first coll)) (my-map f (rest coll))))))
	([f c1 & colls]
 		(my-map #(apply f %) (reorder (cons c1 colls)))))

(my-map
  (fn [c1 c2 c3 c4] (+ c1 c2 c3 c4))
    (range 10)
    (range 10 20)
    (range 20 30)
    ;; Uncomment to show how remaining collection items
		;; are ignored when a coll is exhausted
    ;(range 30 35))
    (range 30 40))

;; -------------------------------------------------------
;; Fin - functions
;; -------------------------------------------------------
