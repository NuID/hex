(ns nuid.hex.impl
  (:require
   [nuid.bytes :as bytes]
   [nuid.hex.lib :as lib]
   [nuid.hex.proto :as proto]))


(extend-protocol proto/Hexable
  (type (byte-array 0))
  (encode
    ([x]   (proto/encode x nil))
    ([x _] (apply str (map #(format "%02x" %) x))))

  java.lang.String
  (encode
    ([x]         (proto/encode (bytes/from x)))
    ([x charset] (proto/encode (bytes/from x charset)))))

(defn -decode
  [[x y]]
  (unchecked-byte (Integer/parseInt (str x y) 16)))

(extend-protocol proto/Hex
  java.lang.String
  (decode [h] (into-array Byte/TYPE (map -decode (partition 2 (lib/unprefixed h)))))
  (str
    ([h]         (bytes/str (proto/decode h)))
    ([h charset] (bytes/str (proto/decode h) charset))))
