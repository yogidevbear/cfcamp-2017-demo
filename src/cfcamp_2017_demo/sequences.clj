(ns cfcamp-2017-demo.sequences)

;; ===========================================
;; The key sequence abstraction functions are:

;; -------------------------------------------------------
;; (first coll)
;; Return the first element of a collection

;; Call (first) function and pass it a vector
(first [1 2.0 :three])
;; Call (first) function and pass it a map
(first {:a 1 :b 2.0 :c :three})
;; What happens when pass (first) a list?
(first (1 2.0 :three))

;; Lists will try to "call" the first item in the collection like a function when
;; it appears in a context where it would be evaluated according to Clojure rules
(1 2.0 :three)

;; (quote) doesn't evaluate/"call" the first item in the collection
(quote (1 2.0 :three))
(first (quote (1 2.0 :three)))
(first '(1 2.0 :three))

;; Use (list) function and pass it a collection
(list 1 2.0 :three)
(first (list 1 2.0 :three))

;; So what is the difference between (quote) and (list)?
(let [a 1 b 2 c 3] (first (list a b c)))
(let [a 1 b 2 c 3] (first (quote (a b c))))

;; Example of (first) function using (seq) internally for nil-punning
(first [])
;(first ())
;(first {})
;(first #{})

;; -------------------------------------------------------
;; (rest coll)
;; Returns a logical coll of the rest of elements (not necessarily a seq).
;; Never returns nil.

(rest [1 2.0 :three])
(rest '(1 2.0 :three))
(rest {:a 1 :b 2.0 :c :three})
(rest [1])
;; We can use (seq) for nil-punning
(seq (rest [1]))

;; -------------------------------------------------------
;; (next coll)
;; Returns a seq of the rest of the elements,
;; which will be nil if no elements remain.
(next [1 2 3])
(next [1])

;; -------------------------------------------------------
;; (cons item seq)
;; Construct a new sequence with the item prepended to seq
(cons 1 [2.0 :three])

;; ===========================================
;; Seqable:

;; Sequence functions (map, filter, etc) implicitly call (seq) on the
;; incoming (seqable) collection and return a sequence (possibly empty, not nil).
;; You must use seq on the result to nil pun.
(sequence (next [1]))
(filter odd? [1 3 5])
(filter odd? [2 4 6])
(filter odd? (rest [1]))
(filter odd? (next [1]))
;; nil-punning
(seq (filter odd? (next [1])))

;You can just hand wave a bit and say "map, filter, reduce etc can operate on any data structure that can be viewed as a sequence of things" and give a struct and a string as examples of things that don't look like arrays (lists, vectors).
;Hash maps become sequences of key/value pairs, strings become sequences of chars. I don't think you need to go any deeper?

;; ===========================================
;; Immutability:

;; Clojure is immutable - Java is not
(let [arr (long-array (range 5))
      as (seq arr)]
  (println as)
  (aset arr 0 99)
  (println as))

(let [arr (long-array (range 5))
      copy (into-array Long/TYPE arr)
      as (seq copy)]
  (println as)
  (aset arr 0 99)
  (println as))


;; A hash map has keys _and_ values, a set only has keys
;; (or only has values, depending on how you look at it).
(def my-map {:a 1 :b 2})
(def my-set #{:a :b})
(contains? my-map :a) => true
(contains? my-map 1) => false
(contains? my-set :a) => true
(:a my-map) => 1
(:a my-set) => :a
(seq my-map) => ([:a 1] [:b 2]) ; a sequence of MapEntry pairs -- note: order not guaranteed
(seq my-set) => (:b :a) ; a sequence of the set elements, whatever they are -- note: order not guaranteed

;; ===========================================
;; Laziness:
;; TODO: Come back to this
