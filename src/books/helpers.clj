(ns books.helpers
    (:require [books.db :as db]
              [books.views :as views]
              [books.config :refer [token]]
              [clojure.walk :as walk]
              [hiccup.table :as table]
              [ring.util.response :as response]
              [cheshire.core :as json]
              [clj-http.client :as client]))
          
(defn signup-check [email pwd pwd-re]
    (cond
        (empty? email) (views/signup "Email can't be blank.")
        (db/user-exists? email) (views/signup "User already exists, please log in." email)
        (not= pwd pwd-re) (views/signup "Entered passwords don't match, please try again." email)
        (< (count pwd) 10) (views/signup "Password is too short, it must be at least 10 characters." email)
        :else (do (db/create-user! email pwd) (views/signup "User created, please log in." nil "yes"))
    ))

(defn book-search [query]
    (get (json/parse-string (str (:body (client/get (str "https://www.googleapis.com/books/v1/volumes?q=" query "&key=" token)))))"items"))


(defn book-add [results]
    (walk/keywordize-keys {:id (get results "id") :info [(select-keys (get results "volumeInfo") ["authors","title","pageCount","publishedDate"])]}))

(defn book-full [results title]
    (merge {"link" (str "<a href=\"/add?id=" (get results "id") "&title=" title "\">Add to bookshelf</a>" )} (select-keys (get results "volumeInfo") ["authors","title","pageCount","publishedDate"])))

(defn check-id [id col]
    (if (= (:id col) id) (:info col)))
    

(defn book-add-final [query]
    (map #(assoc % :authors (apply str (interpose ", " (:authors %))))(map #(book-add %)(book-search query))))

(defn info-for-db [id query]
    (first (remove nil? (map #(check-id id %) (book-add-final query)))))


(defn db-clean [id query]
    (map #(assoc % :authors (apply str (interpose ", " (:authors %))))(info-for-db id query)))

(defn book-final [query]
    (walk/keywordize-keys (map #(book-full % query)(book-search query))))


(defn book-table [query]
    (table/to-table1d (map #(assoc % :authors (apply str (interpose ", " (:authors %))))(book-final query))[:authors "Author(s)" :title "Title" :publishedDate "Publication Date" :pageCount "Page Count" :link "Link"]))


(defn add-to-bookshelf [id title uid]
                        (do (db/add-to-bookshelf! (assoc (first (db-clean id title)) :uid (:id (first (db/get-user-id uid)))))
                            (response/redirect "/bookshelf"))
)

(defn add-links-to-results [results]
    (map #(assoc % :bid (str "<a href=\"/remove?bid="(:bid %)"\">Remove from bookshelf</a>")) results))
(defn bookshelf-view [results]
    (table/to-table1d (add-links-to-results results) [:authors "Author(s)" :title "Title" :pagecount "Page Count" :created_at "Date Added" :bid "Remove?"]))