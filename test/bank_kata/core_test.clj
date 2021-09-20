(ns bank-kata.core-test
  (:require [clojure.test :refer :all]
            [bank-kata.core :refer :all])
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))

(defn date [iso] (LocalDate/parse iso))

(deftest bank-kata-tests
  (testing "Acceptance Test"
    (let [account (create-account)]
      (do (deposit! account 1000 (date "2021-08-01"))
          (deposit! account 2000 (date "2021-08-03"))
          (withdraw! account 500 (date "2021-08-07")))
      (let [expected (str "| Date       | Credit  | Debit   | Balance |\n"
                          "| 07/08/2021 |         |  500.00 | 2500.00 |\n"
                          "| 03/08/2021 | 2000.00 |         | 3000.00 |\n"
                          "| 01/08/2021 | 1000.00 |         | 1000.00 |")
            actual (statement account)]
        (is (= expected actual)))))

  (testing "Deposits"
    (let [account (create-account)]
      (do (deposit! account 100 (date "2021-08-01"))
          (is (= [{:value 100 :date (date "2021-08-01")}] @account))
          (do (deposit! account 200 (date "2021-08-02"))
              (is (= [{:value 100 :date (date "2021-08-01")}
                      {:value 200 :date (date "2021-08-02")}] @account))))))

  (testing "Withdrawals"
    (let [account (create-account)]
      (do (deposit! account 1000 (date "2021-08-01"))
          (withdraw! account 500 (date "2021-08-02"))
          (let [expected [{:value 1000 :date (date "2021-08-01")}
                          {:value -500 :date (date "2021-08-02")}]]
            (is (= expected @account))))))

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
             (statement account)))))

  (testing "Handles floating points"
    (let [account (create-account)]
      (do (deposit! account 123.33 (date "2021-08-01"))
          (withdraw! account 123.33 (date "2021-08-02"))
          (let [expected [{:value 123.33 :date (date "2021-08-01")}
                                         {:value -123.33 :date (date "2021-08-02")}]]
            (is (= expected @account)))))))



