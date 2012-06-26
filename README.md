dyscord
=======

My favourite Metal band from Perth, Western Australia and a clojurescript library to bring emacs-like key sequences to web-apps.

usage
=====

A key sequence, is a sequence of keyboard key presses. Any element in such a sequence can be a [chord](http://www.emacswiki.org/emacs/Chord) composed of a non-modifier key and one or more modifier keys pressed at the same time.

```
;; define a command for 'a'
(key-sequence "a" (fn [] (js/alert "You pressed 'a' indeed!")))
;; let's do our first chord: 'C-x' by pressing 'ctrl' and 'x' at the same time.
(key-sequence "C-x" (fn [] (js/alert "Congrats; Your first chord. Now let's get musical!")))
;; The chords 'C-x' and 'C-M-e' are typed in sequence
(key-sequence "C-x C-M-e" (fn [] (js/alert "Sounds like music to me")))
```





