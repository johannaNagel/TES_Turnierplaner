/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class FillScheduleKtUnitTest {
  var competition: Tournament? = null
  var listParticipant: MutableList<Participant>? = null

  @Before fun setUp() {
    listParticipant = mutableListOf<Participant>(
      Participant("1", 0, 0, 1, "1"),
      Participant("2", 0, 0, 2, "2"),
      Participant("3", 0, 0, 3, "3"),
      Participant("4", 0, 0, 4, "4"),
      Participant("5", 0, 0, 5, "5"),
      Participant("6", 0, 0, 6, "6"),
      Participant("7", 0, 0, 7, "7"),
      Participant("8", 0, 0, 8, "8"),
    )
    competition = Tournament("hello", "1", 10, 3, 1, listParticipant!!,  null, 0)
  }

  @After fun tearDown() {
    listParticipant = null
    competition = null
  }

  @Test fun addResultPoints() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    addResultPoints(competition!!, "winner1", "1", "5")
    for (i in competition!!.participants) {
      if (i.name == "1") {
        TestCase.assertTrue(i.points == 3)
        TestCase.assertTrue(i.games == 1)
      }
      if (i.name == "5") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 1)
      }
    }
  }

  @Test fun addResultPointsChange() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    addResultPoints(competition!!, "winner1", "3", "9")
    addResultToResultList("3", "9", "1", "0", 3, competition!!)
    addResultPointsChange(competition!!, "winner2", "3", "9")
    addResultToResultList("3", "9", "1", "3", 3, competition!!)
    for (i in competition!!.participants) {
      if (i.name == "9") {
        TestCase.assertTrue(i.points == 3)
        TestCase.assertTrue(i.games == 1)
      }
      if (i.name == "3") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 1)
      }
    }
    addResultPointsChange(competition!!, "tie", "3", "9")
    addResultToResultList("3", "9", "0", "0", 3, competition!!)
    for (i in competition!!.participants) {
      if (i.name == "3") {
        TestCase.assertTrue(i.points == 1)
        TestCase.assertTrue(i.games == 1)
      }
      if (i.name == "9") {
        TestCase.assertTrue(i.points == 1)
        TestCase.assertTrue(i.games == 1)
      }
    }
    addResultPointsChange(competition!!, "", "3", "9")
    addResultToResultList("3", "9", "", "", 3, competition!!)
    for (partic in competition!!.participants) {
      if (partic.name == "3") {
        TestCase.assertTrue(partic.points == 0)
        TestCase.assertTrue(partic.games == 0)
      }
      if (partic.name == "9") {
        TestCase.assertTrue(partic.points == 0)
        TestCase.assertTrue(partic.games == 0)
      }
    }
  }

  @Test
  fun checkAddResultPointsChangeTie() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }


    addResultPoints(competition!!, "tie", "1", "2")
    addResultToResultList("1", "2", "0", "0", 1, competition!!)
    addResultPointsChange(competition!!, "winner2", "1", "2")
    addResultToResultList("1", "2", "0", "2", 1, competition!!)
    for (i in competition!!.participants) {
      if (i.name == "2") {
        TestCase.assertTrue(i.points == 3)
        TestCase.assertTrue(i.games == 1)
      }
      if (i.name == "1") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 1)
      }
    }

    addResultPointsChange(competition!!, "winner1", "1", "2")
    addResultToResultList("1", "2", "3", "2", 1, competition!!)
    for (i in competition!!.participants) {
      if (i.name == "2") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 1)
      }
      if (i.name == "1") {
        TestCase.assertTrue(i.points == 3)
        TestCase.assertTrue(i.games == 1)
      }
    }

    addResultPointsChange(competition!!, "", "1", "2")
    addResultToResultList("1", "2", "", "", 1, competition!!)
    for (i in competition!!.participants) {
      if (i.name == "2") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 0)
      }
      if (i.name == "1") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 0)
      }
    }
  }

  @Test fun winOrTie() {
    val resultGame1 = "4"
    val resultGame2 = "0"
    var resultGame3 = "0"
    var resultGame4 = "0"
    TestCase.assertTrue(winOrTie(resultGame1, resultGame2) == "winner1")
    TestCase.assertFalse(winOrTie(resultGame3, resultGame4) == "winner2")
    TestCase.assertTrue(winOrTie(resultGame3, resultGame4) == "tie")
    resultGame3 = ""
    resultGame4 = ""
    TestCase.assertTrue(winOrTie(resultGame3, resultGame4) == "")
  }

  @Test fun addResultToResultList() {
    val scheduleRound1 =
      createSchedule(listParticipant!!, getNumberOfActualParticipants(listParticipant!!))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(listParticipant!!)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(listParticipant!!))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    addResultToResultList("1", "2", "4", "0", 1, competition!!)
    TestCase.assertTrue(competition!!.schedule!![0][0].resultParticipant1 == "4")
    TestCase.assertTrue(competition!!.schedule!![0][0].resultParticipant2 == "0")
    TestCase.assertTrue(competition!!.schedule!![0][0].participant1.name == "1")
    TestCase.assertTrue(competition!!.schedule!![0][0].participant2.name == "2")
    TestCase.assertFalse(competition!!.schedule!![0][0].participant2.name == "3")
  }

  @Test
  fun checkAddResultToListEmpty() {
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    addResultToResultList("1", "2", "", "", 1, competition!!)
    TestCase.assertTrue(competition!!.schedule!![0][0].resultParticipant1 == "")
    TestCase.assertTrue(competition!!.schedule!![0][0].resultParticipant2 == "")
    TestCase.assertTrue(competition!!.schedule!![0][0].participant1.name == "1")
    TestCase.assertTrue(competition!!.schedule!![0][0].participant2.name == "2")
  }

  @Test
  fun checkAddResultPointsWinPlayer2() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    addResultPoints(competition!!, "winner2", "1", "5")
    for (i in competition!!.participants) {
      if (i.name == "5") {
        TestCase.assertTrue(i.points == 3)
        TestCase.assertTrue(i.games == 1)
      } else if (i.name == "1") {
        TestCase.assertTrue(i.points == 0)
        TestCase.assertTrue(i.games == 1)
      }
    }
  }

  @Test
  fun checkAddResultPointsTie() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    addResultPoints(competition!!, "tie", "1", "5")
    for (i in competition!!.participants) {
      if (i.name == "5") {
        TestCase.assertTrue(i.points == 1)
        TestCase.assertTrue(i.games == 1)
      } else if (i.name == "1") {
        TestCase.assertTrue(i.points == 1)
        TestCase.assertTrue(i.games == 1)
      }
    }
  }

  @Test fun checkIfGamePlayed() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      createSchedule(competition!!.participants, getNumberOfActualParticipants(competition!!.participants))
    val numberOfRounds = (getRow(getNumberOfActualParticipants(competition!!.participants)) *2)-1
    competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = getRow(getNumberOfActualParticipants(competition!!.participants))
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(changeOpponent1(scheduleRound1, row, i))
    }
    val bu = competition!!.schedule!![7]
    bu[2].resultParticipant1 = "4"
    bu[2].resultParticipant2 = "5"
    TestCase.assertTrue(
      checkIfGamePlayed(
        bu[2].participant1.name,
        bu[2].participant2.name,
        competition!!
      )
    )
  }
}
