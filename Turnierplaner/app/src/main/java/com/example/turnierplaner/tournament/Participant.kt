/* (C)2022 */
package com.example.turnierplaner.tournament

data class Participant(
    var name: String,
    var games: Int,
    var points: Int,
    var rank: Int,
    var id: String,
) {
  constructor() : this("", 0, 0, 0, "")
}
