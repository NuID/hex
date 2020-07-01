(ns nuid.hex.impl
  (:require
   [nuid.bytes :as bytes]
   [nuid.hex.lib :as lib]
   [nuid.hex.proto :as proto]
   ["buffer" :as b]))

(extend-protocol proto/Hexable
  b/Buffer
  (encode
    ([x]   (proto/encode x nil))
    ([x _] (.toString x "hex")))

  array
  (encode
    ([x]   (proto/encode (b/Buffer.from x)))
    ([x _] (proto/encode (b/Buffer.from x))))

  string
  (encode
    ([x]         (proto/encode (bytes/from x)))
    ([x charset] (proto/encode (bytes/from x charset)))))

(extend-protocol proto/Hex
  string
  (decode [h] (b/Buffer.from (lib/unprefixed h) "hex"))
  (str
    ([h]         (bytes/str (proto/decode h)))
    ([h charset] (bytes/str (proto/decode h) charset))))
