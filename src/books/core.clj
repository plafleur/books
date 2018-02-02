(ns books.core
    (:require [books.db :refer [db-string]]
              [clojure.java.jdbc :as sql]
              [compojure.core :refer [defroutes GET POST]]
              [compojure.handler :as handler]
              [compojure.route :as route]
              [ring.adapter.jetty :as ring]
              [hiccup.page :as page]
              [hiccup.form :as form]
              [cemerick.friend :as friend]
              (cemerick.friend [workflows :as workflows]
                               [credentials :as creds])))

(defn get-user-info [req]
    (let [user-info (first (sql/query db-string 
                 ["select username, password, roles from users where username = (?)" req]))]
            (cond
            (= (:roles user-info) "user") (assoc user-info :roles #{::user})
            (= (:roles user-info) "admin") (assoc user-info :roles #{::admin})
            )
        )
        )


(defn create-user! [email password]
   (sql/insert! db-string :users
                 {:username email 
                  :password (creds/hash-bcrypt password)}))
                
;(defn authenticated? [email password]
 ; (creds/bcrypt-credential-fn (get-user-info email) {:username email :password password}))

(defn home []
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
   [:body
    [:h1.title "Books"]
    [:div.login-form
    (form/form-to
      [:post "/login"]
    (form/label {:class "login-form-username"} "username" "Username:")
    (form/text-field "username")
    [:br]
    (form/label {:class "login-form-password"} "username" "Password:")
    (form/password-field "password")
    [:br][:br]
    (form/submit-button {:class "btn" :name "submit"} "Submit"))]]
    ))


(defroutes app-routes
  (GET "/authorized" request
       (friend/authorize #{::user} "This page can only be seen by authenticated users."))
    (GET "/login" [] (home))
  (route/resources "/")
  (route/not-found "Not Found")
   )


(def app
  (handler/site
   (friend/authenticate app-routes
                        {:default-landing-uri "/authorized"
                         :credential-fn (partial creds/bcrypt-credential-fn get-user-info) 
                         :workflows [(workflows/interactive-form)]
                        })))

(defn -main []
  (ring/run-jetty #'app {:port 8080 :join? false}))