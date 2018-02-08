(ns books.helpers
    (:require [books.db :as db]
              [books.views :as views]
              [ring.util.response :as response]))

(defn signup-check [email pwd pwd-re]
    (cond
        (db/user-exists? email) (views/signup "User already exists, please log in.")
        (not= pwd pwd-re) (views/signup "Entered passwords don't match, please try again.")
        (< (count pwd) 10) (views/signup "Password is too short, it must be at least 10 characters.")
        :else (do (db/create-user! email pwd) (response/redirect "/login"))
    ))
