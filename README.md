<p align="right"><a href="https://nuid.io"><img src="https://nuid.io/svg/logo.svg" width="20%"></a></p>

# nuid.hex

Cross-platform hex {en,de}coding.

## Requirements

[`jvm`](https://www.java.com/en/download/), [`node + npm`](https://nodejs.org/en/download/), [`clj`](https://clojure.org/guides/getting_started), [`shadow-cljs`](https://shadow-cljs.github.io/docs/UsersGuide.html#_installation)

## Clojure and ClojureScript

### tools.deps:

`{nuid/hex {:git/url "https://github.com/nuid/hex" :sha "..."}}`

### usage:

```
$ clj # or shadow-cljs node-repl
=> (require '[nuid.hex :as hex])
=> (def h (hex/encode "🐴")) ;; defaults to reading input as utf8
=> h                         ;; => "f09f90b4"
=> (hex/decode h)            ;; => array-like: [240 159 144 180] (endianness may vary)
=> (hex/str h)               ;; => "🐴"
=> (hex/str h :utf16le)      ;; => "鿰뒐"
```

## Licensing

Apache v2.0 or MIT
