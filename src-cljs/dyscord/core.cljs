(ns dyscord.core
  (:require [goog.object :as gobj]
            [goog.events :as events]))

(def mods (atom { 16 false 18 false 17 false 91 false}))

(def modifiers
  (reduce (fn [m [k v]]
            (assoc m k v))
          { "S" 16
            "shift" 16
            "M" 18
            "alt" 18
            "option" 18
            "C" 17
            "ctrl" 17
            "control" 17
            "cmd" 91
            "command" 91}
          (for [k (range 1 20)] [(str "f" k) (+ 111 k)])))

(def special-ks
  { "backspace" 8
    "tab" 9
    "clear" 12
    "enter" 13
    "return" 13
    "esc" 27
    "escape" 27
    "space" 32
    "left" 37
    "up" 38
    "right" 39
    "down" 40
    "del" 46
    "delete" 46
    "home" 36
    "end" 35
    "pageup" 33
    "pagedown" 34
    "," 188
    "." 190
    "/" 191
    "`" 192
    "-" 189
    "=" 187,
    ";" 186
    "'" 222
    "[" 219
    "]" 221
    "\\" 220})

(def builtin-events (set (map keyword (gobj/getValues events/EventType))))

(def root-element (.. js/window -document))

(defn- find-builtin-type
  [evt-type]
  (if (contains? builtin-events evt-type)
    (name evt-type)
    evt-type))

(defn index-of [e coll]
  (when-let [[i _] (first (filter (fn [[_ v]] (= e v)) (map-indexed vector coll)))]
    i))

(defn group-chords [c]
  (let [keys (clojure.string/split c "-")]
    keys))

(defn key-sequence [kseq fn]
  (map group-chords kseq))


(defn dispatch [e]
  (js/alert "Dispatch stub"))

(defn clear-modifier [e]
  (js/alert "clear-modifier stub"))

(defn reset-modifiers [e]
  (js/alert "reset-modifiers stub"))

;; global handlers
(events/listen root-element
               (find-builtin-type :keydown)
               dispatch
               true)

(events/listen root-element
               (find-builtin-type :keyup)
               clear-modifier
               true)

(events/listen root-element
               (find-builtin-type :keyup)
               reset-modifiers
               true)


