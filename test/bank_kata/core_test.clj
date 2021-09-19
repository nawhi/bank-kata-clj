(ns bank-kata.core-test
  (:require [clojure.test :refer :all]
            [bank-kata.core :refer :all])
  (:import (java.time LocalDate)))

(defn date [iso] (LocalDate/parse iso))

(defn big-string [& strings] (clojure.string/join "\n" strings))

(deftest bank-kata-acceptance
  (testing "Bank Kata Acceptance Test"
    (let [account (create-account)]
      (do (deposit! account 1000 (date "2021-08-01"))
          (deposit! account 2000 (date "2021-08-03"))
          (withdraw! account 500 (date "2021-08-07")))
      (let [expected (big-string "| Date       | Credit  | Debit   | Balance |"
                                 "| 07/08/2021 |         | 500.00  | 2500.00 |"
                                 "| 03/08/2021 | 2000.00 | 500.00  | 2500.00 |"
                                 "| 01/08/2021 | 1000.00 | 500.00  | 2500.00 |")]
        (is (= expected (statement account)))))))
