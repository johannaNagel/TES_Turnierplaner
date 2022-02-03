/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import com.example.turnierplaner.tournament.Participant

/** data class object Result with the following parameters: Participant, Strings */
data class Result(
    var participant1: Participant,
    var participant2: Participant,
    var resultParticipant1: String,
    var resultParticipant2: String
) {
  constructor() : this(Participant("", 0, 0, 0, ""), Participant("", 0, 0, 0, ""), "", "")
}
