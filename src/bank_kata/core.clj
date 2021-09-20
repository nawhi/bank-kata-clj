(ns bank-kata.core
  (:require [clojure.string :as str])
  (:import (java.time.format DateTimeFormatter)))

(defn create-account
  "Create a new Bank Account"
  [] (atom {:transactions []}))

(defn deposit! [account amount date]
  (swap! account (fn [account]
                   (update account :transactions
                           #(conj % {:value amount :date date})))))

(defn withdraw! [account amount date] (deposit! account (- amount) date))

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
  (let [tx (first transactions)]
    (list (stringify-tx tx (:value tx)))))

(defn statement [account]
  (str/join "\n" (concat [HEADER] (stringify-rows account))))

