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
(defn get-user-id [req]
    (sql/query db-string 
                 ["select id from users where username = (?)" req]))

(defn create-user! [email password]
   (sql/insert! db-string :users
                 {:username email 
                  :password (creds/hash-bcrypt password)
                  :roles "user"
                 }))
(defn user-exists? [email]
    (not (empty? (sql/query db-string 
                 ["select username from users where username = (?)" email]))))
             
             
(defn add-to-bookshelf! [results]
    (sql/insert! db-string :books
                 {:uid (:uid results) 
                  :title (:title results)
                  :authors (:authors results)
                  :pagecount (:pageCount results)
                 }))
             
(defn get-bookshelf [username]
    (let [uid (:id (first (get-user-id username)))]
         (sql/query db-string 
                 ["select * from books where uid = (?)" uid])))
(defn get-pagecount [username]
   (let [uid (:id (first (get-user-id username)))]
         (sql/query db-string 
                 ["select sum(pagecount) from books where uid = (?)" uid])))

             
(defn remove-book! [email bid]
   (try (sql/delete! db-string :books
                 ["uid = ? and bid = ?" (:id (first (get-user-id email))) bid])
             (catch Exception e (.getNextException e))))
    