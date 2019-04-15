# nuid.hex

Cross-platform hex {en,de}coding.

## Requirements

[`jvm`](https://www.java.com/en/download/), [`node + npm`](https://nodejs.org/en/download/), [`clj`](https://clojure.org/guides/getting_started), [`shadow-cljs`](https://shadow-cljs.github.io/docs/UsersGuide.html#_installation)

## From Clojure and ClojureScript

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

## Notes

Because this library exists as a common interface over exception facilities, it may only be useful as a functional API to the underlying facilities in the host platform. The below is included just in case.

## From JavaScript

### node:

```
$ shadow-cljs release node
$ node
> var Hex = require('./target/node/nuid_hex');
> var h = Hex.encode("🐴");
> var b = Hex.decode(h);
> Array.from(b);
> Hex.toString(h);
> Hex.toString(h, "utf16le")
```

### browser:

```
$ shadow-cljs release browser
## go use ./target/browser/nuid_hex.js in a browser script
```

## From Java

To call `nuid.hex` from Java or other JVM languages, use one of the recommended interop strategies ([var/IFn](https://clojure.org/reference/java_interop#_calling_clojure_from_java) or [uberjar/aot](https://push-language.hampshire.edu/t/calling-clojure-code-from-java/865)). Doing so may require modifications or additions to the API for convenience.

## From CLR

Coming soon.

## Notes

The purpose of `nuid.hex` and sibling `nuid` libraries is to abstract over platform-specific differences and provide a common interface to fundamental dependencies. This allows us to express dependent logic once in pure Clojure(Script), and use it from each of the host platforms (Java, JavaScript, CLR). Along with [`tools.deps`](https://clojure.org/guides/deps_and_cli), this approach yields the code-sharing, circular-dependency avoidance, and local development benefits of a monorepo, with the modularity and orthogonality of an isolated library.

## Licensing

Apache v2.0 or MIT

## Contributing

Install [`git-hooks`](https://github.com/icefox/git-hooks) and fire away. Make sure not to get bitten by [`externs`](https://clojurescript.org/guides/externs) if modifying `npm` dependencies.

### formatting:

```
$ clojure -A:cljfmt            # check
$ clojure -A:cljfmt:cljfmt/fix # fix
```

### dependencies:

```
## check
$ npm outdated
$ clojure -A:depot

## update
$ npm upgrade -s
$ clojure -A:depot:depot/update
```
