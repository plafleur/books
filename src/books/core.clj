(ns books.core
    (:require [books.db :as db]
              [books.views :as views]
              [compojure.core :refer [defroutes GET POST ANY]]
              [compojure.handler :as handler]
              [compojure.route :as route]
              [ring.adapter.jetty :as ring]
              [cemerick.friend :as friend]
              (cemerick.friend [workflows :as workflows]
                               [credentials :as creds])))


(defroutes app-routes
  (GET "/bookshelf" request
       (friend/authorize #{::user} (views/bookshelf)))
    (GET "/login" [] (views/home))
  (route/resources "/")
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/login")))
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