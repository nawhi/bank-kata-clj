(ns bank-kata.core
  (:require [clojure.string :as str])
  (:import (java.time.format DateTimeFormatter)))

(defn create-account
  "Create a new Bank Account"
  [] (atom {:transactions []}))

(defn deposit! [account amount date]
  "Deposits the given amount of money into the given account on the given date"
  (swap! account (fn [account]
                   (update account :transactions
                           #(conj % {:value amount :date date})))))

(defn withdraw! [account amount date]
  "Withdraws the given amount of money from the given account on the given date"
  (deposit! account (- amount) date))

(def HEADER "| Date       | Credit  | Debit   | Balance |")

(defn format-date [date] (.format date (DateTimeFormatter/ofPattern "dd/MM/yyyy")))
(defn format-value [value] (format "%7s" (format "%4.2f" (double value))))

(def EMPTY "       ")

(defn stringify-tx [{:keys [value date]} balance]
  (let [
        credit (if (> value 0) (format-value value) EMPTY)
        debit (if (< value 0) (format-value (- value)) EMPTY)
        date (format-date date)]
    (str "| " date " | " credit " | " debit " | " (format-value balance) " |"))
  )


(defn stringify-rows [{:keys [transactions]}]
  (loop [rows transactions
         result []
         old-balance 0]
    (if (empty? rows)
      result
      (let [{:keys [value] :as tx} (first rows)
            new-balance (+ old-balance value)]
        (recur
          (drop 1 rows)
          (conj result (stringify-tx tx new-balance))
          new-balance)))))

(defn statement [account]
  "Returns a printable version of the statement for the given bank account"
  (str/join "\n" (concat [HEADER] (reverse (stringify-rows account)))))

