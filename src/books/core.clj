(ns books.core
    (:require [books.db :as db]
              [books.views :as views]
              [compojure.core :refer [defroutes GET POST]]
              [compojure.handler :as handler]
              [compojure.route :as route]
              [ring.adapter.jetty :as ring]
              [cemerick.friend :as friend]
              (cemerick.friend [workflows :as workflows]
                               [credentials :as creds])))


(defroutes app-routes
  (GET "/authorized" request
       (friend/authorize #{::user} "This page can only be seen by authenticated users."))
    (GET "/login" [] (views/home))
  (route/resources "/")
  (route/not-found "Not Found")
   )


(def app
  (handler/site
   (friend/authenticate app-routes
                        {:default-landing-uri "/authorized"
                         :credential-fn (partial creds/bcrypt-credential-fn db/get-user-info) 
                         :workflows [(workflows/interactive-form)]
                        })))

(defn -main []
  (ring/run-jetty #'app {:port 8080 :join? false}))