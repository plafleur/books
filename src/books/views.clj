(ns books.views
    (:require [hiccup.page :as page]
              [hiccup.form :as form]))
          
(def header-login
    (page/html5
    [:div#header.topnav [:a.text {:href "/"} "Books" ]
    [:a.signup {:href "/signup"} "Sign Up"]])
)

(def header-signup
    (page/html5
    [:div#header.topnav [:a.text {:href "/"} "Books" ]
    [:a.signup {:href "/login"} "Log In"]])
)


(def header-other
    (page/html5
    [:div#header.topnav [:a.text {:href "/"} "Books" ]
    [:a.logout {:href "/logout"} "Logout"]])
)

(defn home [& message]
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-login
    [:body
    [:div.error message]
    [:div.login-form
    (form/form-to
      [:post "/login"]
    (form/text-field {:placeholder "Email"} "username")
    [:br]
    (form/password-field {:placeholder "Password"} "password")
    [:br]
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

(defn signup [& [message email success?]]
     (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-signup
     [:body
      [:h1.signup-title "Create a new account:"]
      (if (nil? success?)
      [:div.error message]
      [:div.success message])
    [:div.login-form
    (form/form-to
      [:post "/signup"]
    ;(form/label {:class "login-form-username"} "username" "Username:")
    (form/email-field {:placeholder "Email" :value email} "username" )
    [:br]
    ;(form/label {:class "login-form-password"} "password" "Password:")
    (form/password-field {:placeholder "Password"} "password")
    [:br]
    ;(form/label {:class "login-form-password"} "password-reenter" "Re-enter Password:")
    (form/password-field {:placeholder "Password Confirmation"} "password-reenter")
    [:br]
    (form/submit-button {:class "btn" :name "submit"} "Submit"))]]
    ))