/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.leagueSystem.schedule.createSchedule
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SingleTournamentScreenKtUnitTest {

  @Before fun setUp() {}

  @Test
  fun sortTournamentByPoints() {

    createAddToAllTournaments("Test", 6, 0, 0)
    createAddToAllTournaments("ExcpectedTest", 6, 0, 0)

    val tourney = findTournament("Test")

    tourney.participants.add(Participant("TestPart1", 0, 1, 1, "testid1"))
    tourney.participants.add(Participant("TestPart2", 0, 2, 2, "testid2"))
    tourney.participants.add(Participant("TestPart3", 0, 3, 3, "testid3"))
    tourney.participants.add(Participant("TestPart4", 0, 4, 4, "testid4"))
    tourney.participants.add(Participant("TestPart5", 0, 5, 5, "testid5"))
    tourney.participants.add(Participant("TestPart6", 0, 6, 6, "testid6"))

    val actual = IntArray(6)
    val expected: IntArray = intArrayOf(1, 2, 3, 4, 5, 6)

    for (idx in 0 until tourney.numberOfParticipants) {

      actual[idx] = tourney.participants[idx].rank
    }

    assertTrue(actual.contentEquals(expected))
  }

  @Test fun listCopy() {}

  @Test
  fun testRemoveParticipant() {

    addParticipantToTournament("Test", "TestParticipant")

    assertTrue(tournamentContainsParticipant("Test", "TestParticipant"))
  }


  @Test
  fun entryInScheduleTest(){
    createAddToAllTournaments("Test", 6, 0, 0)
    createAddToAllTournaments("ExcpectedTest", 6, 0, 0)

    val tourney = findTournament("Test")
    tourney.participants.add(Participant("TestPart1", 0, 1, 1, "testid1"))
    tourney.participants.add(Participant("TestPart2", 0, 2, 2, "testid2"))
    tourney.participants.add(Participant("TestPart3", 0, 3, 3, "testid3"))
    tourney.participants.add(Participant("TestPart4", 0, 4, 4, "testid4"))
    tourney.participants.add(Participant("TestPart5", 0, 5, 5, "testid5"))
    tourney.participants.add(Participant("TestPart6", 0, 6, 6, "testid6"))

    createSchedule(tourney.participants, 6)



  }
}
