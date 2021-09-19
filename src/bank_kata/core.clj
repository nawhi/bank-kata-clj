(ns bank-kata.core)

(defn create-account
  "Create a new Bank Account"
  [] (atom {:transactions []}))

(defn deposit! [account amount date]
  (swap! account (fn [account]
                   (update account :transactions
                           #(conj % {:value amount :date date})))))

(defn withdraw! [account amount date] (deposit! account (- amount) date))
(defn statement [account] "TODO")
