(ns bank-kata.statement-test
  (:require [clojure.test :refer :all]
            [bank-kata.statement :refer :all])
  (:import (java.time LocalDate)))

(defn date [iso] (LocalDate/parse iso))

(deftest bank-kata.statement-test
  (testing "stringify-tx"
    (is (= "| 07/08/2021 |  500.00 |         | 1250.00 |"
           (stringify-tx {:value 500 :date (date "2021-08-07")} 1250)))

    (is (= "| 08/09/2021 |         | 1200.00 | 1000.00 |"
           (stringify-tx {:value -1200 :date (date "2021-09-08")} 1000))))

  (testing "Statements"
    (let [account (atom [{:value 500 :date (date "2021-08-01")}])]
      (is (= (str "| Date       | Credit  | Debit   | Balance |\n"
                  "| 01/08/2021 |  500.00 |         |  500.00 |")
             (statement account))))

    (let [account (atom [{:value 500 :date (date "2021-08-01")}
                         {:value -125 :date (date "2021-08-03")}])]
      (is (= (str "| Date       | Credit  | Debit   | Balance |\n"
                  "| 03/08/2021 |         |  125.00 |  375.00 |\n"
                  "| 01/08/2021 |  500.00 |         |  500.00 |")
             (statement account))))))
