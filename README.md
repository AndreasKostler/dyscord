dyscord
=======
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/AndreasKostler/dyscord?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

My favourite Metal band from Perth, Western Australia and a clojurescript library to bring emacs-like key sequences to web-apps.

usage
=====

A key sequence, is a sequence of keyboard key presses. Any element in such a sequence can be a [chord](http://www.emacswiki.org/emacs/Chord) composed of a non-modifier key and one or more modifier keys pressed at the same time.

```clj
;; define a command for 'a'
(key-sequence! "a" (fn [] (js/alert "You pressed 'a' indeed!")))
;; let's do our first chord: 'C-x' by pressing 'ctrl' and 'x' at the same time.
(key-sequence! "C-x" (fn [] (js/alert "Congrats; Your first chord. Now let's get musical!")))
;; The chords 'C-x' and 'C-M-e' are typed in sequence
(key-sequence! "C-x C-M-e" (fn [] (js/alert "Sounds like music to me")))
```
As in emacs, 'C-g' cancels a partially typed or accidental command. Dyscord does not suppress browser shortcuts, yet. Maybe it never will. 
 





