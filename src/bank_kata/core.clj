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
(defn format-amount [amount] (format "%-7s" (format "%4.2f" (double amount))))

(def EMPTY "       ")

(defn stringify-tx [{:keys [amount date]} balance]
  (let [
        credit (if (> amount 0) (format-amount amount) EMPTY)
        debit (if (< amount 0) (format-amount (- amount)) EMPTY)
        date (format-date date)]
    (str "| " date " | " credit " | " debit " | " (format-amount balance) " |"))
  )


(defn rows [account]
  (let [result []
        rows account
        balance 0]
    (let [{:keys [amount date] :as row} (first account)]
      "TODO";(recur (stringify-tx row balance) (subvec rows 1) (+ balance amount)))
    )))

(defn statement [account]
  (str/join "\n" (concat [HEADER] (rows account))))

