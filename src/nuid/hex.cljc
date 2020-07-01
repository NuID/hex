(ns nuid.hex
  (:refer-clojure :exclude [str])
  (:require
   [clojure.string :as string]
   [nuid.hex.impl]
   [nuid.hex.lib :as lib]
   [nuid.hex.proto :as proto]
   #?@(:clj  [[clojure.alpha.spec :as s]]
       :cljs [[clojure.spec.alpha :as s]])))

(def regex #"^0[xX]?[a-fA-F0-9]+$")

(s/def ::encoded
  (s/and
   string?
   seq
   (fn [s] (re-matches regex s))))

(def encoded?
  (partial s/valid? ::encoded))

  ;; 2019.10.01 NOTE: These are a strange fit for the `nuid.hex` namespace;
  ;; they're here because we may not want all of Web3j(s) to get them.
  ;; Someday these will find a more permanent home.
(s/def :nuid.ethereum.transaction/nil
  #{"0x0000000000000000000000000000000000000000000000000000000000000000"
    "0000000000000000000000000000000000000000000000000000000000000000"})

(s/def :nuid.ethereum.transaction/id
  (s/and
   ::encoded
   (fn [id]
     (and
      (not (s/valid? :nuid.ethereum.transaction/nil id))
      (or
       (= (count id) 64)
       (and
        (= (count id) 66)
        (string/starts-with? id "0x")))))))

(defn encode
  ([x]         (proto/encode x))
  ([x charset] (proto/encode x charset)))

(defn decode
  [h]
  (proto/decode h))

(defn str
  ([h]         (proto/str h))
  ([h charset] (proto/str h charset)))

(def prefixed   lib/prefixed)
(def unprefixed lib/unprefixed)
