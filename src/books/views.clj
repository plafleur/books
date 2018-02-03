(ns books.views
    (:require [hiccup.page :as page]
              [hiccup.form :as form]))

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