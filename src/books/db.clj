(ns books.db
    (:require
        [clojure.java.jdbc :as sql]
        [cemerick.friend :as friend]
              (cemerick.friend [workflows :as workflows]
                               [credentials :as creds])))

(def db-string
  {:dbtype "postgresql"
 :dbname "books"
 :host "localhost"
 :port 5432})

(defn get-user-info [req]
    (let [user-info (first (sql/query db-string 
                 ["select username, password, roles from users where username = (?)" req]))]
            (cond
            (= (:roles user-info) "user") (assoc user-info :roles #{:books.core/user})
            (= (:roles user-info) "admin") (assoc user-info :roles #{:books.core/admin})
            )))

(defn create-user! [email password]
   (sql/insert! db-string :users
                 {:username email 
                  :password (creds/hash-bcrypt password)}))