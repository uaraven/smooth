# Changes in version 0.2.0

##General
  - Java 6 support is dropped. Java 7 is required to compile and use.
  - Minor Javadoc fixes and code refactoring. 
  
###Functions:
  Added `Provider` interface. It is essentially the same as `Promise`, but with different semantics.
 
###Utilities
  Added `Option.orGet(Provider)` method for lazy Option alternative evaluation.
     
###Other
  Added `Trie` interface as well as basic mutable implementation. This is in **beta** and not tested properly.