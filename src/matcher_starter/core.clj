(ns matcher-starter.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))
;---------------------------------------------------------------------------
(def house
  '{ kitchen (living-room hall)
    living-room (kitchen hall study)
    hall (kitchen living-room study stairs)
    study (living-room hall)
    stairs (hall landing)
    landing (stairs bed-1 bed-2 bathroom)
    bathroom (landing)
    bed-1 (landing)
    bed-2 (landing)})

(breadth-search 'study 'bathroom house)

; repl -> run file ->
; (org.clojars.cognesence.breadth-search.core/breadth-search 'study 'bathroom matcher-starter.core/house)

;---------------------------------------------------------------------------

(def room-ops
  '{pickup {:pre ( (agent ?agent)
                   (manipulable ?obj)
                   (holds ?agent nil)
                   (on ?obj ?place)
                   (at ?agent ?place)
                   )
            :del ( (on ?obj ?place)
                   (holds ?agent nil)
                   )
            :add ( (holds ?agent ?obj) )
            :txt (pickup ?obj from ?place)
            :cmd [grasp ?obj]
            }
    drop    {:pre ( (holds ?agent ?obj)
                    (at ?agent ?place)
                    (:guard (? obj))
                    )
             :del ( (holds ?agent ?obj) )
             :add ( (holds ?agent nil)
                    (on ?obj ?place)
                    )
             :txt ( (drop ?obj on ?place) )
             :cmd [drop ?obj]
             }
    move    {:pre ( (agent ?agent)
                    (at ?agent ?p1)
                    (connects ?p1 ?p2)
                    )
             :del ( (at ?agent ?p1) )
             :add ( (at ?agent ?p2) )
             :txt ( (move ?p1 to ?p2) )
             :cmd [move ?p2]
             }
    })

(def room
  '#{(at M table)
     (on pen1 table)
     (on pen2 table)
     (holds M nil)
     })

(def room-world
  '#{(connects table bookcase)
     (manipulable pen1)
     (manipulable pen2)
     (agent M)
     }
  )

(def room-world2
  '#{(connects table bookcase)
     (connects bookcase table)
     (connects bookcase desk)
     (connects desk bookcase)
     (manipulable pen1)
     (manipulable pen2)
     (agent M)
     }
  )

(def room2
  '#{(on pen1 bookcase)
     })

(ops-search room room2 room-ops :world room-world)

; repl => (use 'org.clojars.cognesence.ops-search.core) etc.
; repl => (ops-search matcher-starter.core/room matcher-starter.core/room2 matcher-starter.core/ops :world matcher-starter.core/world)
; (ops-search matcher-starter.core/room '((on pen1 bookcase) (on pen2 desk)) matcher-starter.core/ops :world matcher-starter.core/world2)