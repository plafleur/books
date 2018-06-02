(ns books.core
    (:require [books.db :as db]
              [books.views :as views]
              [books.helpers :as helpers]
              [clojure.string :as str]
              [compojure.core :refer [defroutes GET POST ANY]]
              [compojure.handler :as handler]
              [compojure.route :as route]
              [ring.adapter.jetty :as ring]
              [ring.util.response :as response]
              [cemerick.friend :as friend]
              (cemerick.friend [workflows :as workflows]
                               [credentials :as creds])))


(defroutes app-routes
  (GET "/bookshelf" request
       (friend/authorize #{::user} (views/bookshelf (helpers/bookshelf-view (db/get-bookshelf (get-in request [:session :cemerick.friend/identity :current]))) (:sum (first (db/get-pagecount (get-in request [:session :cemerick.friend/identity :current])))))))
   (POST "/bookshelf" request
         (friend/authorize #{::user} (str request)))
    (GET "/search"  request (friend/authorize #{::user}(views/search)))
    (POST "/search" request (friend/authorize #{::user} (views/search (helpers/book-table (get-in request [:form-params "book-title"])))))
    (GET "/add*" request (friend/authorize #{::user} (helpers/add-to-bookshelf (first (vals (:query-params request))) (last (vals (:query-params request)))(str (get-in request [:session :cemerick.friend/identity :current]))))) 
    (GET "/login" request (if (nil? (:login_failed (:params request))) (views/home)(views/home "Email/password incorrect.")))
    (GET "/signup" [] (views/signup))
    (POST "/signup" [username password password-reenter] (helpers/signup-check username password password-reenter))
  (route/resources "/")
  (friend/logout (ANY "/logout" request (response/redirect "/login")))
  (route/not-found "Not Found")
   )


(def app
  (handler/site
   (friend/authenticate app-routes
                        {:default-landing-uri "/bookshelf"
                         :credential-fn (partial creds/bcrypt-credential-fn db/get-user-info) 
                         :workflows [(workflows/interactive-form)]
                        })))

(defn -main []
  (ring/run-jetty #'app {:port 8080 :join? false}))