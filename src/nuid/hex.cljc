(ns nuid.hex
  (:refer-clojure :exclude [str])
  (:require
   [nuid.bytes :as bytes]
   #?@(:clj
       [[clojure.alpha.spec :as s]]
       :cljs
       [[clojure.spec.alpha :as s]
        ["buffer" :as b]])))

(defn prefixed
  [h]
  (if (clojure.string/starts-with? h "0x")
    h
    (clojure.core/str "0x" h)))

(defn unprefixed
  [h]
  (if (clojure.string/starts-with? h "0x")
    (subs h 2)
    h))

(defprotocol Hexable
  (encode
    [x]
    [x charset]))

(defprotocol Hex
  (decode [h])
  (str
    [h]
    [h charset]))

(def regex #"^0[xX]?[a-fA-F0-9]+$")

(s/def ::encoded
  (s/and string?
         (fn [s] (re-matches regex s))))

;; 2019.10.01 These are a strange fit for the `nuid.hex` namespace;
;; they're here because we may not want all of Web3j(s) to get them.
;; Someday these will find a more permanent home.
(s/def :ethereum/nil-transaction-id
  #{"0x0000000000000000000000000000000000000000000000000000000000000000"
    "0000000000000000000000000000000000000000000000000000000000000000"})

(s/def :ethereum/transaction-id
  (s/and ::encoded
         (fn [txid] (and (not (s/valid? :ethereum/nil-transaction-id txid))
                         (or (= (count txid) 64)
                             (and (= (count txid) 66)
                                  (clojure.string/starts-with? txid "0x")))))))

#?(:clj
   (extend-protocol Hexable
     (type (byte-array 0))
     (encode
       ([x] (encode x nil))
       ([x _] (apply clojure.core/str (map #(format "%02x" %) x))))

     java.lang.String
     (encode
       ([x] (encode (bytes/from x)))
       ([x charset] (encode (bytes/from x charset))))))

#?(:clj
   (extend-protocol Hex
     java.lang.String
     (decode [h]
       (let [f (fn [[x y]] (unchecked-byte (Integer/parseInt (clojure.core/str x y) 16)))]
         (into-array Byte/TYPE (map f (partition 2 (unprefixed h))))))
     (str
       ([h] (bytes/str (decode h)))
       ([h charset] (bytes/str (decode h) charset)))))

#?(:cljs
   (extend-protocol Hexable
     b/Buffer
     (encode
       ([x] (encode x nil))
       ([x _] (.toString x "hex")))

     array
     (encode
       ([x] (encode (b/Buffer.from x)))
       ([x _] (encode (b/Buffer.from x))))

     string
     (encode
       ([x] (encode (bytes/from x)))
       ([x charset] (encode (bytes/from x charset))))))

#?(:cljs
   (extend-protocol Hex
     string
     (decode [h] (b/Buffer.from (unprefixed h) "hex"))
     (str
       ([h] (bytes/str (decode h)))
       ([h charset] (bytes/str (decode h) charset)))))

#?(:cljs (def exports #js {}))
