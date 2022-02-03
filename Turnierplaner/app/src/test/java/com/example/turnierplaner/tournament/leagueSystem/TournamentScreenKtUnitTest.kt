/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem

import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import junit.framework.TestCase
import org.junit.After
import org.junit.Test

class TournamentScreenKtUnitTest : TestCase() {

  public override fun setUp() {
    super.setUp()
    createAddToAllTournaments("Test", 6, 0, 0)
    createAddToAllTournaments("ExcpectedTest", 6, 0, 0)
  }

  @After
  override fun tearDown() {
    removeTournament(findTournament("Test"))
    removeTournament(findTournament("ExcpectedTest"))
  }

  @Test
  fun testAddToTournaments() {

    createAddToAllTournaments("TestAdd", 10, 0, 0)

    var tourney: List<Tournament> = getAllTournaments()

    assertFalse(tourney.isEmpty())
  }

  /* TODO
   *   create add methods*/
  @Test
  fun createAddToAllTournaments() {

    createAddToAllTournaments("Test", 10, 0, 0)

    assertEquals("Test", findTournament("Test").name)
  }


  @Test fun findTournament() {}

  @Test fun addParticipantToTournament() {}

  @Test fun tournamentContainsParticipant() {}

  @Test fun allTournamentContainsTournament() {}
}
