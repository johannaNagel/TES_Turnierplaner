/* (C)2022 */
package com.example.turnierplaner.tournament

import com.example.turnierplaner.tournament.leagueSystem.schedule.Result
import com.google.firebase.database.Exclude

data class Tournament(
    var name: String,
    // To avoid storing in firebase database
    @get:Exclude var id: String,
    var numberOfParticipants: Int,
    var pointsVictory: Int,
    var pointsTie: Int,
    var participants: MutableList<Participant>,
    var schedule: MutableList<MutableList<Result>>?,
    var inviteCode: Int?
) {
  constructor() : this("", "", 0, 0, 0, mutableListOf(), null, null)
}
