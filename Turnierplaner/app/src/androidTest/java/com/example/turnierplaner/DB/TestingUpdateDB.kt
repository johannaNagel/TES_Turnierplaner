/* (C)2022 */
package com.example.turnierplaner.DB

import com.example.turnierplaner.tournament.leagueSystem.Participant
import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.addPlayerToTournament
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import java.util.UUID
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestingUpdateDB {

  private val name = "TestDB"
  private val id = UUID.randomUUID().toString()
  private val numberOfTeams = 5
  private val players = mutableListOf<Participant>()
  private val tourney = TournamentClass(name, id, numberOfTeams, players, 0, 0)

  @Before
  fun initialize() {
    getParticipantsFromDb()
    createAddToAllTournaments(tourney.name, numberOfTeams, 0, 0)
  }

  @Test
  fun testAddPlayerToTournament() {
    addPlayerToTournament(tourney.name, "TestPlayer1")
    pushLocalToDb()
    Thread.sleep(5005)
    assertEquals("TestPlayer1", findTournament(tourney.name).participants[0].name)
  }

  @After
  fun tearDown() {
    removeTournament(tourney)
  }
}
