(ns nuid.hex.proto
  (:refer-clojure :exclude [str]))

(defprotocol Hexable
  (encode
    [x]
    [x charset]))

(defprotocol Hex
  (decode [h])
  (str
    [h]
    [h charset]))
