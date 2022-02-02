/* (C)2021 */
package com.example.turnierplaner.tournament

import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.addParticipantToTournament
import com.example.turnierplaner.tournament.leagueSystem.containsParticipant
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.leagueSystem.getAllTournaments
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import junit.framework.TestCase
import org.junit.After
import org.junit.Test

class TournamentLogicKtTest : TestCase() {

  public override fun setUp() {
    super.setUp()
  }

  @Test
  fun testAddToTournaments() {

    createAddToAllTournaments("Test", 10, 0, 0)

    var tourney: List<TournamentClass> = getAllTournaments()

    assertFalse(tourney.isEmpty())
  }

  @Test
  fun testCreateTournament() {

    createAddToAllTournaments("Test", 10, 0, 0)

    assertEquals("Test", findTournament("Test").name)
  }

  @Test
  fun testRemoveParticipant(){

    createAddToAllTournaments("Test", 10, 0, 0)

    addParticipantToTournament("Test", "TestParticipant")

    assertTrue(containsParticipant("Test", "TestParticipant"))

  }

  @After
  override fun tearDown() {
    removeTournament(findTournament("Test"))
  }
}
