### Macros are awesome!

[http://scalamacros.org/paperstalks/2014-02-04-WhatAreMacrosGoodFor.pdf](http://scalamacros.org/paperstalks/2014-02-04-WhatAreMacrosGoodFor.pdf)

### So let's go write some :)

  * This workshop is a down-to-earth live coding session
  * No philosophical discussions: for that check out ["Macros vs Types"](http://scalamacros.org/paperstalks/2014-03-01-MacrosVsTypes.pdf) and ["Philosophy of Scala Macros"](http://scalamacros.org/paperstalks/2013-09-19-PhilosophyOfScalaMacros.pdf)
  * No plans for the future: for that check out ["Rethinking Scala Macros"](http://scalamacros.org/paperstalks/2014-03-02-RethinkingScalaMacros.pdf)  * No plans for the future: for that check out ["Rethinking Scala Macros"](http://scalamacros.org/paperstalks/2014-03-02-RethinkingScalaMacros.pdf) and the upcoming ["Easy Metaprogramming For Everyone"](http://www.scaladays.org/#schedule/Easy-Metaprogramming-For-Everyone-)

### Our plan for today

  * Macro-based optimizers
  * Implicit materializers

We'll be using Scala 2.11.x with all the new goodies including quasiquotes and macro bundles, but we'll also spend some time discussing 2.10.x compatibility.

### Things that we'll leave out

  * Macro-based languge virtualization that was elaborated on in ["Yin-Yang: Transparent Deep Embedding of DSLs"](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.308.880) and ["An Embedded Query Language in Scala"](https://github.com/amirsh/master-thesis)
  * Type providers that were outlined in ["Macro-Based Type Providers in Scala"](https://github.com/travisbrown/type-provider-examples/blob/master/docs/scalar-2014-slides.pdf?raw=true) and [the accompanying code repository for that talk](https://github.com/travisbrown/type-provider-examples)
  * Macro annotations that were also presented in ["Macro-Based Type Providers in Scala"](https://github.com/travisbrown/type-provider-examples/blob/master/docs/scalar-2014-slides.pdf?raw=true)
  * String interpolation macros that will be covered at ScalaDays: ["Quote Or Be Quoted"](http://www.scaladays.org/#schedule/Quote-or-be-quoted)
