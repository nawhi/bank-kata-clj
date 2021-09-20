(ns bank-kata.core
  (:require [clojure.string :as str]))

(defn create-account
  "Create a new Bank Account"
  [] (atom []))

(defn deposit! [account value date]
  "Deposits the given amount of money into the given account on the given date"
  (swap! account #(conj % {:value value :date date})))

(defn withdraw! [account value date]
  "Withdraws the given amount of money from the given account on the given date"
  (deposit! account (- value) date))

