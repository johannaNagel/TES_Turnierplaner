/* (C)2021 */
package com.example.turnierplaner.tournament

import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.deleteTournament
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.leagueSystem.getAllTournaments
import junit.framework.TestCase
import org.junit.Test

class TournamentLogicKtTest : TestCase() {

  public override fun setUp() {
    super.setUp()
  }

  @Test
  fun testAddToTournaments() {

    createAddToAllTournaments("Test", 10)

    var tourney: List<TournamentClass> = getAllTournaments()

    assertFalse(tourney.isEmpty())
  }

  @Test
  fun testCreateTournament() {

    createAddToAllTournaments("Test", 10)

    assertEquals("Test", findTournament("Test").name)
  }

  @Test
  fun testDeleteTournament() {

    createAddToAllTournaments("Test", 10)

    var tourney: List<TournamentClass> = getAllTournaments()

    deleteTournament("Test")

    assertFalse(tourney.isEmpty())
  }

  public override fun tearDown() {}
}
