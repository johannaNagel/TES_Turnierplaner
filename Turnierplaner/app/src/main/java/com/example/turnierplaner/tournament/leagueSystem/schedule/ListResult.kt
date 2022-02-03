/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.leagueSystem.changeOpponent1
import com.example.turnierplaner.tournament.leagueSystem.createSchedule
import com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants
import com.example.turnierplaner.tournament.leagueSystem.getRow

/** data object ListResults which contains a list of all GamesRounds and their results */
data class ListResult(val participants: MutableList<Participant>) {
  var allGames = mutableListOf<MutableList<Result>>()
  var roundNumber = (getRow(getNumberOfActualParticipants(participants)) * 2) - 1
  init {
    allGames.add(createSchedule(participants, getNumberOfActualParticipants(participants)))
    for (i in 2..roundNumber) {
      allGames.add(changeOpponent1(allGames.get(0), getRow(roundNumber + 1), i))
    }
  }
}
