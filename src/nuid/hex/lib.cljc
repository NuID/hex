(ns nuid.hex.lib
  (:require
   [clojure.string :as string]))

(defn prefixed
  [h]
  (if (string/starts-with? h "0x")
    h
    (str "0x" h)))

(defn unprefixed
  [h]
  (if (string/starts-with? h "0x")
    (subs h 2)
    h))
