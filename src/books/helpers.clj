(ns books.helpers
    (:require [books.db :as db]
              [ring.util.response :as response]))

(defn signup-check [email pwd pwd-re]
    (cond
        (db/user-exists? email) (response/redirect "/signup-user-error")
        (not= pwd pwd-re) (response/redirect "/signup-password-match")
        (<= (count pwd) 10) (response/redirect "/signup-password-length")
        :else (do (db/create-user! email pwd) (response/redirect "/login-signup"))
    ))