/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.leagueSystem.schedule.addResultPointsChange
import com.example.turnierplaner.tournament.leagueSystem.schedule.addResultToResultList
import com.example.turnierplaner.tournament.leagueSystem.schedule.changeOpponent1
import com.example.turnierplaner.tournament.leagueSystem.schedule.createSchedule
import com.example.turnierplaner.tournament.leagueSystem.schedule.getNumberOfActualParticipants
import com.example.turnierplaner.tournament.leagueSystem.schedule.getRow
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import junit.framework.TestCase
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

    val numberOfRounds = (getRow(
      getNumberOfActualParticipants(
        tourney.participants
      )
    ) *2)-1
    val row = getRow(
      getNumberOfActualParticipants(
        tourney.participants
      )
    )

    val scheduleRound1 = createSchedule(tourney.participants, 6)
    tourney.schedule!!.add(scheduleRound1)
    for (i in 2..numberOfRounds) {
      tourney!!.schedule!!.add(
        changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }
  assertFalse(entryInSchedule(tourney))
  addResultPointsChange(tourney, "winner1", "TestPart1", "TestPart2")
  addResultToResultList("TestPart1", "TestPart2", "3", "4", 1, tourney)
  assertTrue(entryInSchedule(tourney))
  }

  @Test
  fun participantWithSameUID(){
    createAddToAllTournaments("Test", 6, 0, 0)
    createAddToAllTournaments("ExcpectedTest", 6, 0, 0)

    val tourney = findTournament("Test")
    tourney.participants.add(Participant("TestPart1", 0, 1, 1, "testid1"))
    tourney.participants.add(Participant("TestPart2", 0, 2, 2, "testid1"))
    tourney.participants.add(Participant("TestPart3", 0, 3, 3, "testid1"))
    tourney.participants.add(Participant("TestPart4", 0, 4, 4, "testid1"))
    tourney.participants.add(Participant("TestPart5", 0, 5, 5, "testid1"))
    tourney.participants.add(Participant("TestPart6", 0, 6, 6, "testid1"))

    var list = participantsWithSameUID("Test")
    var list1 = participantsWithSameUID("Test")
    assertTrue(list.size == 6)
    tourney.participants.add(Participant("TestPart7", 0, 1, 1, "testid1"))
    assertTrue(list1.size == 2)
  }

  @Test
  fun participantWithNOSameUID(){
    createAddToAllTournaments("Test", 6, 0, 0)
    createAddToAllTournaments("ExcpectedTest", 6, 0, 0)

    val tourney = findTournament("Test")
    tourney.participants.add(Participant("TestPart1", 0, 1, 1, "testid1"))
    tourney.participants.add(Participant("TestPart2", 0, 2, 2, "testid2"))
    tourney.participants.add(Participant("TestPart3", 0, 3, 3, "testid3"))
    tourney.participants.add(Participant("TestPart4", 0, 4, 4, "testid4"))
    tourney.participants.add(Participant("TestPart5", 0, 5, 5, "testid5"))
    tourney.participants.add(Participant("TestPart6", 0, 6, 6, "testid6"))

    var list = participantsWithSameUID("Test")
    assertTrue(list.size == 1)

  }


  @Test
  fun editPointsVictory(){
    createAddToAllTournaments("Test", 6, 0, 0)
    val tourney = findTournament("Test")

    editPointsVictoryTie(tourney, "", "9")
    assertTrue(tourney.pointsVictory == 0)
    assertTrue(tourney.pointsTie == 9)

    editPointsVictoryTie(tourney, "2", "")
    assertTrue(tourney.pointsVictory == 2)
    assertTrue(tourney.pointsTie == 9)

    editPointsVictoryTie(tourney, "", "")
    assertTrue(tourney.pointsVictory == 2)
    assertTrue(tourney.pointsTie == 9)

    editPointsVictoryTie(tourney, "5", "6")
    assertTrue(tourney.pointsVictory == 5)
    assertTrue(tourney.pointsTie == 6)
  }

  @Test
  fun changeTournamentName(){
    createAddToAllTournaments("Test", 6, 0, 0)
    val tourney = findTournament("Test")
    changeTournamentName(tourney.name, "hello")
    assertTrue(tourney.name == "hello")
  }

  @Test
  fun changeFalseTournamentName(){
    createAddToAllTournaments("Test", 6, 0, 0)
    val tourney = findTournament("Test")
    changeTournamentName(tourney.name, "hello")
    assertTrue(tourney.name == "hello44")
  }

  @Test
  fun changeNameTrue(){
    createAddToAllTournaments("Test", 6, 0, 0)
    val tourney = findTournament("Test")
    tourney.participants.add(Participant("TestPart1", 0, 1, 1, "testID1"))
    changeName("TestPart1", "TestPart", tourney.name)
    assertTrue(tourney.name == "TestPart")
  }

  @Test
  fun changeNameFalse(){
    createAddToAllTournaments("Test", 6, 0, 0)
    val tourney = findTournament("Test")
    tourney.participants.add(Participant("TestPart1", 0, 1, 1, "testID1"))
    changeName("TestPart1", "TestPart", tourney.name)
    assertTrue(tourney.name == "TestPart3")
  }
}
