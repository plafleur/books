(ns books.views
    (:require [hiccup.page :as page]
              [hiccup.form :as form]))
          
(def header-login
    (page/html5
    [:div#header.topnav [:a.text {:href "/"} "Books" ]
   [:a.signup{:href "/logout"} "Logout"] [:a.signup {:href "/signup"} "Sign Up"]])
)

(def header-other
    (page/html5
    [:div#header.topnav [:a.text {:href "/"} "Books" ]
    [:a.logout {:href "/logout"} "Logout"]])
)

(defn home []
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-login
    [:body
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
(defn bookshelf []
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-other
    [:body
     "List of books"]
    ))