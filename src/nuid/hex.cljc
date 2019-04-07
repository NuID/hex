(ns nuid.hex
  (:require
   [clojure.string :as str]
   [nuid.bytes :as bytes]
   #?@(:cljs [["buffer" :as b]])))

(defn encode
  ([s] (encode s :utf8))
  ([s charset]
   (let [b (if (bytes/bytes? s) s (bytes/from s charset))]
     #?(:clj (apply str (map #(format "%02x" %) b))
        :cljs (.toString b "hex")))))

(defn decode [h]
  (let [h (if (str/starts-with? h "0x") (subs h 2) h)]
    #?(:clj (let [f (fn [[x y]] (unchecked-byte (Integer/parseInt (str x y) 16)))]
              (into-array Byte/TYPE (map f (partition 2 h))))
       :cljs (b/Buffer.from h "hex"))))

(defn to
  ([h] (bytes/to (decode h)))
  ([h charset] (bytes/to (decode h) charset)))

#?(:cljs (def exports #js {:encode #(encode %1 (or (keyword %2) :utf8))
                           :toString #(to %1 (or (keyword %2) :utf8))
                           :decode decode}))
