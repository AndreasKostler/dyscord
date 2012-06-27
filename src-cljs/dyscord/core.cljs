(ns dyscord.core
  (:require [goog.object :as gobj]
            [goog.events :as events]
            [clojure.string :as string]))

(def mods (atom { 16 false 18 false 17 false 91 false}))

(def keyseq-handlers (atom {}))

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

;; Stolen from domina
(def builtin-events (set (map keyword (gobj/getValues events/EventType))))

(def root-element (.. js/window -document))

(defprotocol Event
  (prevent-default [evt] "Prevents the default action, for example a link redirecting to a URL")
  (stop-propagation [evt] "Stops event propagation")
  (target [evt] "Returns the target of the event")
  (current-target [evt] "Returns the object that had the listener attached")
  (event-type [evt] "Returns the type of the the event")
  (raw-event [evt] "Returns the original GClosure event"))

(defn- create-listener-function
  [f]
  (fn [evt]
    (f (reify
         Event
         (prevent-default [_] (.preventDefault evt))
         (stop-propagation [_] (.stopPropagation evt))
         (target [_] (.-target evt))
         (current-target [_] (.-currentTarget evt))
         (event-type [_] (.-type evt))
         (raw-event [_] evt)
         ILookup
         (-lookup [o k]
           (if-let [val (aget evt k)]
             val
             (aget evt (name k))))
         (-lookup [o k not-found] (or (-lookup o k)
                                      not-found))))
    true))

(defn- find-builtin-type
  [evt-type]
  (if (contains? builtin-events evt-type)
    (name evt-type)
    evt-type))

;; end

(defn index-of [e coll]
  (when-let [[i _] (first (filter (fn [[_ v]] (= e v)) (map-indexed vector coll)))]
    i))

(defn get-keycode [key]
  (or (get special-ks key)
      (get modifiers key)
      (.charCodeAt (.toUpperCase key) 0)))

(defn canonicalize-keyseq [kseq]
  (vec
   (for [chord (remove string/blank? (string/split kseq #"[\t ]"))]
     (let [keys (string/split chord #"-")
           keycodes (map get-keycode keys)]
       (when (every? identity keycodes)
         (set keycodes))))))

(defn key-sequence! [kseq fn]
  (let [kseq (string/split kseq #",")]
    (doseq [k kseq]
      (when-let [k (canonicalize-keyseq k)]
        (swap! keyseq-handlers assoc k fn)))))


(defn- canonicalize-command-key [key]
  (if (or (== key 93)
          (== key 224))
    91
    key))

(defn get-chord [key]
  (set (conj (map first (filter (fn [[_ v]] (true? v)) @mods)) key)))

(defn- modifier? [key]
  (find @mods key))

(def keyseq (atom []))

(defn- reset-mods! []
  (reset! mods { 16 false 18 false 17 false 91 false}))

(defn reset-keyseq! []
  (reset! keyseq []))

(defn- modifier-pressed? []
  (some (fn [[_ v]] (true? v)) @mods))

(def dispatch!
  (create-listener-function
   (fn [event]
     (let [key (canonicalize-command-key (:keyCode event))]
       (if (modifier? key)
         (swap! mods assoc key true)
         (let [chord (get-chord key)
               handler (get @keyseq-handlers (conj @keyseq chord))]
           ;; see if key-seq is complete
           (if-not (nil? handler)
             (do
               (reset-keyseq!)
               (handler))
             (when modifier-pressed?
               (swap! keyseq conj chord)))))))))
          
(def clear-modifier!
  (create-listener-function
   (fn [event]
     (let [key (canonicalize-command-key (:keyCode event))]
       (when (modifier? key)
         (swap! mods assoc key false))))))

(def reset-all!
  (create-listener-function
   (fn [event]
     (reset-keyseq!)
     (reset-mods!))))

;; global handlers
(events/listen root-element
               (find-builtin-type :keydown)
               dispatch!
               true)

(events/listen root-element
               (find-builtin-type :keyup)
               clear-modifier!
               true)

(events/listen root-element
               (find-builtin-type :focus)
               reset-all!
               true)

(key-sequence! "C-x" (fn [] (.log js/console "C-x")))
(key-sequence! "M-k C-x, M-l C-y" (fn [] (.log js/console "M-k C-x")))
(key-sequence! "C-g" (fn [] (reset-keyseq!)))

