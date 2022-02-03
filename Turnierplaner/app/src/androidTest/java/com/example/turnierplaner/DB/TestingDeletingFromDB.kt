/* (C)2022 */
package com.example.turnierplaner.DB

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.TournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import java.util.UUID
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestingDeletingFromDB {

  private val name = "TestDB"
  private val id = UUID.randomUUID().toString()
  private val numberOfTeams = 1
  private val players = mutableListOf<Participant>()
  private val tourney = Tournament(name, id, numberOfTeams, 0, 0, players, null)

  @Before
  fun initialize() {
    getParticipantsFromDb()
    createAddToAllTournaments(tourney.name, numberOfTeams, 0, 0)
  }

  @Test
  fun testDeletingTournamentToDB() {
    removeTournament(tourney)
    getParticipantsFromDb()
    assertEquals("", findTournament(tourney.name).name)
  }
}
