/* (C)2021 */
package com.example.turnierplaner.tournament

import com.example.turnierplaner.tournament.leagueSystem.Participant
import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.addParticipantToTournament
import com.example.turnierplaner.tournament.leagueSystem.containsParticipant
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.leagueSystem.getAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.sortTournamentByPoints
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

  @Test
  fun testSortParticipants(){

    createAddToAllTournaments("Test", 6, 0, 0)
    createAddToAllTournaments("ExcpectedTest", 6, 0, 0)

    val tourney = findTournament("Test")

    tourney.participants.add(Participant("TestPart1", 0, 1, 1,"testid1" ))
    tourney.participants.add(Participant("TestPart2", 0, 2, 2,"testid2" ))
    tourney.participants.add(Participant("TestPart3", 0, 3, 3,"testid3" ))
    tourney.participants.add(Participant("TestPart4", 0, 4, 4,"testid4" ))
    tourney.participants.add(Participant("TestPart5", 0, 5, 5,"testid5" ))
    tourney.participants.add(Participant("TestPart6", 0, 6, 6,"testid6" ))

    val actual = IntArray(6)
    val expected: IntArray = intArrayOf(1,2,3,4,5,6)

    for(idx in 0 until tourney.numberOfParticipants){

      actual[idx] = tourney.participants[idx].rank

    }

    assertTrue(actual.contentEquals(expected))

  }

  @After
  override fun tearDown() {
    removeTournament(findTournament("Test"))
    removeTournament(findTournament("ExcpectedTest"))
  }
}
