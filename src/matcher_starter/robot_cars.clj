(ns matcher-starter.robot-cars
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(def world '#{(corridor corridor1)
              (corridor corridor2)
              (corridor corridor3)
              (corridor corridor4)
              (corridor corridor5)
              (corridor corridor6)
              (bay bay1)
              (bay bay2)
              (bay bay3)
              (bay bay4)
              (bay bay5)
              (bay bay6)
              (bay bay7)
              (bay bay8)
              (bay bay9)
              (bay bay10)
              (bay bay11)
              (bay bay12)
              (bay bay13)
              (bay bay14)
              (bay bay15)
              (bay bay16)
              (bay bay17)
              (bay bay18)
              (in bay1 corridor1)
              (in bay2 corridor1)
              (in bay3 corridor1)
              (in bay4 corridor2)
              (in bay5 corridor2)
              (in bay6 corridor2)
              (in bay7 corridor3)
              (in bay8 corridor3)
              (in bay9 corridor3)
              (in bay10 corridor4)
              (in bay11 corridor4)
              (in bay12 corridor4)
              (in bay13 corridor5)
              (in bay14 corridor5)
              (in bay15 corridor5)
              (in bay16 corridor6)
              (in bay17 corridor6)
              (in bay18 corridor6)
              (agent robot1)
              (junction junction1)
              (junction junction2)
              (junction junction3)
              (stock planks)

              ; Connects
              (connects junction1 bay1)
              (connects bay1 junction1)

              (connects junction1 bay4)
              (connects bay4 junction1)

              (connects junction1 junction2)
              (connects junction2 junction1)

              (connects junction2 bay7)
              (connects bay7 junction2)

              (connects junction2 bay10)
              (connects bay10 junction2)

              (connects junction2 junction3)
              (connects junction3 junction2)

              (connects junction3 bay13)
              (connects bay13 junction3)

              (connects junction3 bay16)
              (connects bay16 junction3)

              (connects bay1 bay2)
              (connects bay2 bay1)

              (connects bay2 bay3)
              (connects bay3 bay2)

              (connects bay4 bay5)
              (connects bay5 bay4)

              (connects bay5 bay6)
              (connects bay6 bay5)

              (connects bay7 bay8)
              (connects bay8 bay7)

              (connects bay8 bay9)
              (connects bay9 bay8)

              (connects bay10 bay11)
              (connects bay11 bay10)

              (connects bay11 bay12)
              (connects bay12 bay11)

              (connects bay13 bay14)
              (connects bay14 bay13)

              (connects bay14 bay15)
              (connects bay15 bay14)

              (connects bay16 bay17)
              (connects bay17 bay16)

              (connects bay17 bay18)
              (connects bay18 bay17)

              (connects junction3 ex1)
              (exchange ex1)
              (junction ex1)
              })

(def start '#{(facing robot1 vertical)
              (at robot1 bay1)
              (holding robot1 nil)
              (stored planks bay2)})

(def goal '#{(stored planks ex1)})

(def ops '{move-to-bay          {:pre ((agent ?agent)
                                       (at ?agent ?src)
                                       (in ?src ?cor)
                                       (corridor ?cor)
                                       (connects ?src ?dst)
                                       (bay ?dst)
                                       (facing ?agent vertical))
                                 :add ((at ?agent ?dst))
                                 :del ((at ?agent ?src))
                                 :txt (bay-move ?agent ?src ?dst)
                                 :cmd [?agent move to ?dst]}

           junction-to-junction {:pre ((agent ?agent)
                                       (at ?agent ?src)
                                       (connects ?src ?dst)
                                       (junction ?src)
                                       (junction ?dst)
                                       (facing ?agent horizontal))
                                 :add ((at ?agent ?dst))
                                 :del ((at ?agent ?src))
                                 :txt (j2j ?agent ?dst)
                                 :cmd [?agent move to ?dst]}

           corridor-to-junction {:pre ((agent ?agent)
                                       (at ?agent ?src)
                                       (connects ?src ?dst)
                                       (in ?src ?cor)
                                       (corridor ?cor)
                                       (junction ?dst)
                                       (facing ?agent vertical))
                                 :add ((at ?agent ?dst))
                                 :del ((at ?agent ?src))
                                 :txt (c2j ?agent ?dst)
                                 :cmd [?agent move to ?dst]}

           junction-to-corridor {:pre ((agent ?agent)
                                       (at ?agent ?src)
                                       (connects ?src ?dst)
                                       (in ?dst ?cor)
                                       (corridor ?cor)
                                       (junction ?src)
                                       (facing ?agent vertical))
                                 :add ((at ?agent ?dst))
                                 :del ((at ?agent ?src))
                                 :txt (j2c ?agent ?dst)
                                 :cmd [?agent move to ?dst]}

           face-horizontal      {:pre ((agent ?agent)
                                       (at ?agent ?junction)
                                       (facing ?agent vertical))
                                 :add ((facing ?agent horizontal))
                                 :del ((facing ?agent vertical))
                                 :txt (face-h ?agent horizontal)
                                 :cmd [?agent rotate horizontal]}

           face-vertical        {:pre ((agent ?agent)
                                       (at ?agent ?junction)
                                       (facing ?agent horizontal))
                                 :add ((facing ?agent vertical))
                                 :del ((facing ?agent horizontal))
                                 :txt (face-v ?agent vertical)
                                 :cmd [?agent rotate vertical]}

           pickup               {:pre ((agent ?agent)
                                       (holding ?agent nil)
                                       (stored ?stock ?bay)
                                       (at ?agent ?bay)
                                       (bay ?bay))
                                 :add ((holding ?agent ?stock))
                                 :del ((holding ?agent nil)
                                       (stored ?stock ?bay))
                                 :txt (pickup ?agent ?stock)
                                 :cmd [?agent pickup ?stock]}

           drop                 {:pre ((agent ?agent)
                                       (holding ?agent ?stock)
                                       (at ?agent ?bay)
                                       (bay ?bay))
                                 :add ((stored ?stock ?bay)
                                       (holding ?agent nil))
                                 :del ((holding ?agent ?stock))
                                 :txt (dropped ?agent ?stock)
                                 :cmd [?agent drop ?stock]}

           drop-in-ex           {:pre ((agent ?agent)
                                       (holding ?agent ?stock)
                                       (at ?agent ?exchange)
                                       (exchange ?exchange))
                                 :add ((stored ?stock ?exchange)
                                       (holding ?agent nil))
                                 :del ((holding ?agent ?stock))
                                 :txt (dropped ?agent ?stock)}})
