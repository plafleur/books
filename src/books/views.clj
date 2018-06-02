(ns books.views
    (:require [books.db :as db]
              [hiccup.page :as page]
              [hiccup.form :as form]))
          
(def header-login
    (page/html5
    [:div#header.topnav [:a.text {:href "/login"} "Books" ]
    [:a.signup {:href "/signup"} "Sign Up"]])
)

(def header-signup
    (page/html5
    [:div#header.topnav [:a.text {:href "/login"} "Books" ]
    [:a.signup {:href "/login"} "Log In"]])
)


(def header-other
    (page/html5
    [:div#header.topnav [:a.text {:href "/bookshelf"} "Books" ]
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
(defn search [& results]
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-other
    [:body
     (form/form-to
         [:post "/search"]
         (form/text-field "book-title")
         [:br]
         (form/submit-button {:class "btn" :name "submit"} "Submit")
    results
    )]))

(defn bookshelf [& [books pagecount]]
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-other
    [:body
    [:h1 (str "You've read " pagecount " pages so far.")]
    [:br]
    [:a  {:href "/search"} "Add a book"]
    books
    ]))

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
    (form/email-field {:placeholder "Email" :value email} "username" )
    [:br]
    (form/password-field {:placeholder "Password"} "password")
    [:br]
    (form/password-field {:placeholder "Password Confirmation"} "password-reenter")
    [:br]
    (form/submit-button {:class "btn" :name "submit"} "Submit"))]]
    ))