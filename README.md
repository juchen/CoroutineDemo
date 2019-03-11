A simple Android APP to demo a coroutine as an alternative to a switch-case state running function

A coroutine can be viewed as a state machine. When it is suspended, it enters a state.
When it is resumed, it run the state transition. Side effects are its output and the state
is maintained by variables in the "stack" of the coroutine, or in other words, the local variables in the
suspend functions along the call stack trace.

This project starts from a plain implementation of Java code, and is refactored to form a switch-case state running function.
And then, the code is converted into Kotlin. At last, a coroutine is used instead of the switch-case function.
Hopefully, it may help to give some intuition to understand Kotlin coroutine.

The scenario to implement
=========================

We choose a very simple scenario for this example. 3 pages are shown one by one.
In the first page, the user enter her name, second page, her age, and the last page
shows the result: the user's name and age.

![Screen cast][https://github.com/juchen/CoroutineDemo/blob/document/screencast.gif]

There are tons of better methods to similar jobs then what's in this example.
However, our purpose is a demo to the similarity betwenn a switch-case function and a coroutine.
So, we choose to bear with our not-so-good implementation.

