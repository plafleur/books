(ns books.views
    (:require [books.db :as db]
              [hiccup.page :as page]
              [hiccup.form :as form]
              [clojure.string :as string]))
          
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

(defn home [& [email message]]
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-login
    [:body
    [:div.error message]
    [:div {:id "login-form"}
    (form/form-to
      [:post "/login"]
    (form/email-field {:placeholder "Email" :value email} "username")
    [:br]
    (form/password-field {:placeholder "Password"} "password")
    [:br]
    (form/submit-button {:id "login-button" :name "submit"} "Log In"))]]
    ))
(defn search [& [message search-term results]]
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-other
    [:body
    [:a {:id "warning-message"} message] 
     (form/form-to
         [:post "/search"]
         (form/text-field {:placeholder "Search term" :value search-term} "book-title")
         [:br]
         (form/submit-button {:id "search-button" :class "btn" :name "submit"} "Submit")
    results
    )]))

(defn bookshelf [& [books pagecount date]]
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-other
    [:body
     [:h1 {:id "Bookshelf"} "Bookshelf"]
     [:a {:href "/search"} 
    [:button {:id "add-button"} "Add a book"]]
    [:br]
    [:h1 {:id "pagecount"} (str "You've read " (if (nil? pagecount) "0" pagecount) " pages so far. You last addded a book " date ".")]
    [:br]
    (if (empty? books)
        [:h2 "You need to add a book to your bookshelf!"]
        books
    )
    ]))

(defn bookshelf-empty []
  (page/html5
    [:head 
     [:title "Books"]
     (page/include-css "style.css")]
    header-other
    [:body
    [:a {:href "/search"} 
    [:button {:id "add-button"} "Add a book"]]
    [:h2 {:id "no-books"} "You need to add a book to your bookshelf!"]
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