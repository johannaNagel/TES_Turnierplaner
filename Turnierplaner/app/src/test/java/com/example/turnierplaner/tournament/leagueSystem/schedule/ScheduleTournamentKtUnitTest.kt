/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.Result as Result

class ScheduleTournamentKtUnitTest {
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
    competition = Tournament("hello", "1", 10, 3, 1, listParticipant!!,  null)
  }

  @After fun tearDown() {
    listParticipant = null
    competition = null
  }

  @Test fun createSchedule() {
    val playerNumber =
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        listParticipant!!
      )
    val scheduleRound1 = com.example.turnierplaner.tournament.leagueSystem.createSchedule(
      listParticipant!!,
      playerNumber
    )
    for (i in scheduleRound1) {
      TestCase.assertTrue(i.participant1.name == (scheduleRound1.indexOf(i) * 2 + 1).toString())
      TestCase.assertTrue(i.participant2.name == (scheduleRound1.indexOf(i) * 2 + 2).toString())
    }
  }
  @Test
  fun testingScheduleUnEvenNumberOfPlayer() {

    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val playerNumber =
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
    val scheduleRound1 = com.example.turnierplaner.tournament.leagueSystem.createSchedule(
      competition!!.participants,
      playerNumber
    )
    for (i in scheduleRound1) {
      TestCase.assertTrue(i.participant1.name == (scheduleRound1.indexOf(i) * 2 + 1).toString())
      if (scheduleRound1.last() != i) {
        TestCase.assertTrue(i.participant2.name == (scheduleRound1.indexOf(i) * 2 + 2).toString())
      } else {
        TestCase.assertTrue(i.participant2.name == "")
      }
    }
  }
  @Test fun actualizeTournamentSchedule() {}

  @Test fun changeOpponent1() {
    val scheduleRound1 =
      com.example.turnierplaner.tournament.leagueSystem.createSchedule(
        listParticipant!!,
        com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
          listParticipant!!
        )
      )
    val numberOfRounds = (com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        listParticipant!!
      )
    ) *2)-1
    val row = com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        listParticipant!!
      )
    )
    //competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(
        com.example.turnierplaner.tournament.leagueSystem.changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }

    TestCase.assertTrue(competition!!.schedule!![1][0].participant1.name == "1")
    TestCase.assertTrue(competition!!.schedule!![1][0].participant2.name == "4")
    TestCase.assertTrue(competition!!.schedule!![1][1].participant1.name == "2")
    TestCase.assertTrue(competition!!.schedule!![1][1].participant2.name == "6")
    TestCase.assertTrue(competition!!.schedule!![1][2].participant1.name == "3")
    TestCase.assertTrue(competition!!.schedule!![1][2].participant2.name == "8")
    TestCase.assertTrue(competition!!.schedule!![1][3].participant1.name == "5")
    TestCase.assertTrue(competition!!.schedule!![1][3].participant2.name == "7")

  }
  @Test
  fun rotateScheduleUnEvenNumberOfParticipantRound1() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      com.example.turnierplaner.tournament.leagueSystem.createSchedule(
        competition!!.participants,
        com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
      )
    val numberOfRounds = (com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        competition!!.participants
      )
    ) *2)-1
    //competition!!.schedule = mutableListOf<MutableList<Result>>()
    val row = com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
    )
    competition!!.schedule!!.add(scheduleRound1)
    for (i in 2..numberOfRounds - 1) {
      competition!!.schedule!!.add(
        com.example.turnierplaner.tournament.leagueSystem.changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }
    val bu = competition!!.schedule!![1]
    TestCase.assertTrue(bu[0].participant1.name == "1")
    TestCase.assertTrue(bu[0].participant2.name == "4")
    TestCase.assertTrue(bu[1].participant1.name == "2")
    TestCase.assertTrue(bu[1].participant2.name == "6")
    TestCase.assertTrue(bu[2].participant1.name == "3")
    TestCase.assertTrue(bu[2].participant2.name == "8")
    TestCase.assertTrue(bu[3].participant1.name == "5")
    TestCase.assertTrue(bu[3].participant2.name == "")
    TestCase.assertTrue(bu[4].participant1.name == "7")
    TestCase.assertTrue(bu[4].participant2.name == "9")
  }

  @Test
  fun rotateScheduleUnEvenNumberOFPlayerRound4() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      com.example.turnierplaner.tournament.leagueSystem.createSchedule(
        competition!!.participants,
        com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
      )
    val numberOfRounds = (com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        competition!!.participants
      )
    ) *2)-1
    //competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
    )
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(
        com.example.turnierplaner.tournament.leagueSystem.changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }
    val bu = competition!!.schedule!![7]
    TestCase.assertTrue(bu[0].participant1.name == "1")
    TestCase.assertTrue(bu[0].participant2.name == "5")
    TestCase.assertTrue(bu[1].participant1.name == "7")
    TestCase.assertTrue(bu[1].participant2.name == "3")
    TestCase.assertTrue(bu[2].participant1.name == "9")
    TestCase.assertTrue(bu[2].participant2.name == "2")
    TestCase.assertTrue(bu[3].participant1.name == "")
    TestCase.assertTrue(bu[3].participant2.name == "4")
    TestCase.assertTrue(bu[4].participant1.name == "8")
    TestCase.assertTrue(bu[4].participant2.name == "6")
  }
  @Test fun getRow() {
    val numberPlayers = 6
    val numberPlayers2 = 7
    TestCase.assertTrue(com.example.turnierplaner.tournament.leagueSystem.getRow(numberPlayers) == 3)
    TestCase.assertFalse(com.example.turnierplaner.tournament.leagueSystem.getRow(numberPlayers2) == 3)
    TestCase.assertTrue(com.example.turnierplaner.tournament.leagueSystem.getRow(numberPlayers2) == 4)
  }

  @Test fun splitString() {}

  @Test fun getNumberOfActualParticipants() {
    TestCase.assertTrue(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        competition!!.participants
      ) == 8
    )
  }

  @Test fun createScheduleTournament() {}

  @Test fun fillGameString() {}

  @Test fun methodWhichRound() {
    val scheduleRound1 =
      com.example.turnierplaner.tournament.leagueSystem.createSchedule(
        competition!!.participants,
        com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
      )
    val numberOfRounds = (com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        competition!!.participants
      )
    ) *2)-1
   // competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
    )
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(
        com.example.turnierplaner.tournament.leagueSystem.changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }
    addResultToResultList("1", "2", "0", "1", 1, competition!!)
    addResultToResultList("3", "4", "0", "1", 1, competition!!)
    addResultToResultList("5", "6", "0", "1", 1, competition!!)
    addResultToResultList("7", "8", "0", "1", 1, competition!!)
    TestCase.assertTrue(
      com.example.turnierplaner.tournament.leagueSystem.methodWhichRound(
        competition!!
      ) == 2
    )
  }
  @Test
  fun checkWhichRoundRound1() {
    val scheduleRound1 =
      com.example.turnierplaner.tournament.leagueSystem.createSchedule(
        competition!!.participants,
        com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
      )
    val numberOfRounds = (com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        competition!!.participants
      )
    ) *2)-1
    //competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
    )
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(
        com.example.turnierplaner.tournament.leagueSystem.changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }
    addResultToResultList("1", "2", "0", "1", 1, competition!!)
    addResultToResultList("3", "4", "0", "1", 1, competition!!)
    addResultToResultList("5", "6", "0", "1", 1, competition!!)
    addResultToResultList("7", "8", "", "", 1, competition!!)
    TestCase.assertTrue(
      com.example.turnierplaner.tournament.leagueSystem.methodWhichRound(
        competition!!
      ) == 1
    )

  }

  @Test fun getGameResult() {}

  @Test fun getTournament() {}

  @Test fun setTourn() {}

  @Test
  fun correctWin() {
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
  @Test
  fun checkGamePlayed() {
    competition!!.participants.add(Participant("9", 0, 0, 9, "9"))
    val scheduleRound1 =
      com.example.turnierplaner.tournament.leagueSystem.createSchedule(
        competition!!.participants,
        com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
      )
    val numberOfRounds = (com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(
        competition!!.participants
      )
    ) *2)-1
    //competition!!.schedule = mutableListOf<MutableList<Result>>()
    competition!!.schedule!!.add(scheduleRound1)
    val row = com.example.turnierplaner.tournament.leagueSystem.getRow(
      com.example.turnierplaner.tournament.leagueSystem.getNumberOfActualParticipants(competition!!.participants)
    )
    for (i in 2..numberOfRounds) {
      competition!!.schedule!!.add(
        com.example.turnierplaner.tournament.leagueSystem.changeOpponent1(
          scheduleRound1,
          row,
          i
        )
      )
    }
    val bu = competition!!.schedule!![7]
    bu[2].resultParticipant1 = "4"
    bu[2].resultParticipant2 = "5"
    assertTrue(
      checkIfGamePlayed(
        bu[2].participant1.name,
        bu[2].participant2.name,
        competition!!
      )
    )
  }

}
