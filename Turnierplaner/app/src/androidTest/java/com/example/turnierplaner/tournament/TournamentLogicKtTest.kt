/* (C)2021 */
package com.example.turnierplaner.tournament

import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
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

  // WIRD BEI TESTS DER DB BEREITS MITGETESTET

  /*@Test
  fun testDeleteTournament() {

    createAddToAllTournaments("Test", 10)

    var tourney: List<TournamentClass> = getAllTournaments()

    //Ich habe eine weitere Methode implementiert, die nun local und von der DB gleichzeitig löscht
    removeTournament(findTournament("Test"))

    assertFalse(tourney.isEmpty())
  }*/

  @After
  override fun tearDown() {
    removeTournament(findTournament("Test"))
  }
}
