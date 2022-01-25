/* (C)2022 */
package com.example.turnierplaner.DB

import com.example.turnierplaner.tournament.leagueSystem.Player
import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.getTeamsFromDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import java.util.UUID
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestingDeletingFromDB {

  private val name = "TestDB"
  private val id = UUID.randomUUID().toString()
  private val numberOfTeams = 1
  private val players = mutableListOf<Player>()
  private val tourney = TournamentClass(name, id, numberOfTeams, players, 0, 0)

  @Before
  fun initialize() {
    getTeamsFromDb()
    createAddToAllTournaments(tourney.name, numberOfTeams, 0, 0)
  }

  @Test
  fun testDeletingTournamentToDB() {
    removeTournament(tourney)
    getTeamsFromDb()
    assertEquals("", findTournament(tourney.name).name)
  }
}