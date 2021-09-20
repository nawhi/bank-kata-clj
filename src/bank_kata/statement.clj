(ns bank-kata.statement
  (:require [clojure.string :as str])
  (:import (java.time.format DateTimeFormatter)))

(def HEADER "| Date       | Credit  | Debit   | Balance |")
(def EMPTY-CELL "       ")

(defn format-date [date] (.format date (DateTimeFormatter/ofPattern "dd/MM/yyyy")))

(defn format-value [value] (format "%7s" (format "%4.2f" (double value))))

(defn stringify-tx [{:keys [value date]} balance]
  (let [
        credit (if (> value 0) (format-value value) EMPTY-CELL)
        debit (if (< value 0) (format-value (- value)) EMPTY-CELL)
        date (format-date date)]
    (str "| " date " | " credit " | " debit " | " (format-value balance) " |")))


(defn stringify-rows [account]
  (loop [rows account
         result ()
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
  (str/join "\n" (concat [HEADER] (stringify-rows @account))))
