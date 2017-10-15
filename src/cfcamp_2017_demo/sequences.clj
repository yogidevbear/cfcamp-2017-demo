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

;; Lists will try to "call" the first item in the collection
;; like a function when it appears in a context where
;; it would be evaluated according to Clojure rules
(1 2.0 :three)

;; (quote) doesn't evaluate/"call" the first item in the collection
(first (quote (1 2.0 :three)))
(first '(1 2.0 :three))

;; Use (list) function and pass it a collection
(first (list 1 2.0 :three))

;; So what is the difference between (quote) and (list)?
(let [a 1 b 2 c 3] (first (list a b c)))
(let [a 1 b 2 c 3] (first (quote (a b c))))

;; Example of (first) function using (seq) internally for nil-punning
(first [])
;; Works with list, vectors, hash-maps and sets


;; -------------------------------------------------------
;; (rest coll)
;; Returns a logical coll of the rest of elements
;; (not necessarily a seq).
;; Never returns nil.

(rest [1 2.0 :three])
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

;; -------------------------------------------------------
;; (into type ...)
(into [] (rest [1 2/3 "four"]))
(into {} (rest {:a 1 :b 2.0 :c :three}))

;; ===========================================
;; Sequence functions (map, filter, reduce, etc)
;; can operate on any data structure
;; that can be viewed as a sequence of things
;; e.g. a struct and a string are examples of things that
;; don't look like arrays (lists, vectors).

;; Hash maps become sequences of key/value pairs
(first {:a 1 :b 2})
;; Strings become sequences of chars
(rest "string")

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

;; -------------------------------------------------------
;; Fin - Sequences
;; -------------------------------------------------------
