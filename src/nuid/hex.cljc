(ns nuid.hex
  (:require
   [clojure.string :as str]
   [nuid.bytes :as bytes]
   #?@(:cljs [["buffer" :as b]]))
  (:refer-clojure :exclude [str]))

(defn prefixed [h] (if (str/starts-with? h "0x") h (clojure.core/str "0x" h)))
(defn unprefixed [h] (if (str/starts-with? h "0x") (subs h 2) h))

(defprotocol Hexable
  (encode [x] [x charset]))

(defprotocol Hex
  (decode [h])
  (str [h] [h charset]))

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
     js/Buffer
     (encode
       ([x] (encode x nil))
       ([x _] (.toString x "hex")))

     js/Array
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

#?(:cljs
   (def exports
     #js {:encode #(encode %1 (or (keyword %2) :utf8))
          :toString #(str %1 (or (keyword %2) :utf8))
          :decode decode}))
