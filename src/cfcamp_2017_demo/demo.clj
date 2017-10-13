(ns cfcamp-2017-demo.demo)

(def awards [{:name "ADC Young Guns" :follow-count 650 :parent-name "" :events ["2017" "2016" "2015" "2014"] :regions [] :verified false}
             {:name "AdFest" :follow-count 717 :parent-name "" :events ["2017" "2016" "2015" "2014"] :regions ["Asia Pacific" "Middle East / North Africa"] :verified true}
             {:name "Andy Awards" :follow-count 1172 :parent-name "" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified true}
             {:name "Cannes Lions International Film of Creativity" :follow-count 2056 :parent-name "Cannes Lions" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified false}
             {:name "Caples Awards" :follow-count 492 :parent-name "" :events [2017 2016 2014] :regions ["Global"] :verified false}
             {:name "Clio Awards" :follow-count 1787 :parent-name "Clio" :events ["2017" "2016" "2015" "2014" "2013"] :regions ["Global"] :verified true}
             {:name "Clio Health Awards" :follow-count 500 :parent-name "Clio" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified false}
             {:name "D&AD" :follow-count 1732 :parent-name "D&AD" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified false}
             {:name "Epica Awards" :follow-count 881 :parent-name "" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified false}
             {:name "Eurobest Awards" :follow-count 624 :parent-name "" :events ["2017" "2016" "2015" "2014" "2013"] :regions ["Europe"] :verified false}
             {:name "Global Effie Awards" :follow-count 820 :parent-name "Effies" :events ["2017" "2016" "2015" "2013"] :regions ["Global"] :verified false}
             {:name "LIA" :follow-count 1182 :parent-name "" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified false}
             {:name "Lions Innovation" :follow-count 486 :parent-name "Cannes Lions" :events ["2017" "2016" "2015"] :regions ["Global"] :verified false}
             {:name "New York Festivals International Advertising Awards" :follow-count 1146 :parent-name "" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified false}
             {:name "North American Effie Awards" :follow-count 546 :parent-name "Effies" :events ["2018" "2017" "2016" "2015" "2014"] :regions [] :verified false}
             {:name "One Show" :follow-count 1665 :parent-name "One Club" :events ["2018 Q4" "2018 Q3" "2018 Q2" "2018 Q1" "2017 Q4" "2017 Q3" "2017 Q2" "2017 Q1" "2016 Q4" "2016 Q3" "2016 Q2" "2016 Q1"] :regions ["Global"] :verified false}
             {:name "Spikes Asia Festival of Creativity" :follow-count 575 :parent-name "Cannes Lions" :events ["2017" "2016" "2015" "2014"] :regions ["Asia Pacific"] :verified false}
             {:name "SXSW Interactive Innovation Awards" :follow-count 477 :parent-name "" :events ["2018" "2017" "2016" "2015"] :regions ["Global"] :verified false}
             {:name "The ADC 96th Annual Awards" :follow-count 1102 :parent-name "One Club" :events ["2017" "2016" "2015" "2014"] :regions ["Global"] :verified true}
             {:name "The Webby Awards" :follow-count 1401 :parent-name "" :events ["2018" "2017" "2016" "2015"] :regions ["Global" "Europe"] :verified false}])

;; (reduce f coll)
;; (reduce f val coll)
(reduce (fn [result coll] (+ result (:follow-count coll))) 0 awards)
;; Now... mind... equals... blown
(transduce (map :follow-count) + awards)


;; (filter pred)
;; (filter pred coll)

(filter (fn [coll] (= (:parent-name coll) "Cannes Lions")) awards)
(filter #(= (:parent-name %) "Cannes Lions") awards)

(filter #(true? (:verified %)) awards)
(filter #(:verified %) awards)

(filter #(contains? (set (:regions %)) "Europe") awards)
(filter #(contains? (set (:events %)) "2018") awards)

(filter (fn [coll] (> (:follow-count coll) 999)) awards)
(filter #(>= (:follow-count %) 1000) awards)

;; (count coll)
(count (filter #(>= (:follow-count %) 1000) awards))

;; intro to sort-by
(take 10 (reverse (sort-by :follow-count awards)))

;; (map f coll)
;; (map f coll & colls)
(map :name awards)
(reduce #(inc %) (range (count awards)))
(reduce (fn [coll1 coll2] (map coll1 coll2)) awards (range (count awards)))
(#(map list % (range)) awards)
(#(map list % (hash-map :position (range))) awards)

(map hash-map (repeat :position) (map inc (range 5)))
(#(map hash-map (repeat :position) (map inc (range (count %)))) awards)
;zipmap
(map
  (fn [coll cPos] (assoc coll :position cPos))
    (reverse (sort-by :follow-count awards))
    (#(map inc (range (count %))) awards))

;(map :keyword vector-of-maps)
;(group-by :keyword vector-of-maps)
;(sort-by :keyword vector-of-maps)
(map :name awards)
(group-by :parent-name awards)
(sort-by :follow-count awards)

;; initial examples
(map :parent-name awards)
(map :follow-count awards)
;(sort-by (juxt :k :w) vector-of-maps)
(def sorted-awards (sort-by (juxt :parent-name :follow-count) awards))
(map :parent-name sorted-awards)
(map :follow-count sorted-awards)
(map vector
      (map :parent-name sorted-awards)
      (map :follow-count sorted-awards)
      (map :name sorted-awards))


















;; Fin :)
