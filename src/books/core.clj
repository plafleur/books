(ns books.core
    (:require [books.db :refer [db-string]]
              [clojure.java.jdbc :as sql]
              [buddy.hashers :as hashers]))

(defn get-password [email]
  (-> (sql/query db-string
            ["select password from users where email = (?)" email]) first :password))

(defn create-user! [email password]
   (sql/insert! db-string :users
                 {:email email 
                  :password (hashers/derive password)}))
                
(defn log-in [email password]
  (if (hashers/check password (get-password email))
      "success"
      "fail"))
                
(defn check-users []
  (sql/query db-string
             ["select * from users"]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
