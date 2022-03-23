/* (C)2022 */
package com.example.turnierplaner.tournament.tournamentDB

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.addParticipantToTournament
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class DatabaseKtUnitTest {

  private val name = "TestDB"
  private val id = UUID.randomUUID().toString()
  private val numberOfTeams = 1
  private val players = mutableListOf<Participant>()
  private val tourney = Tournament(name, id, numberOfTeams, 0, 0, players,null,  0)
  private val database =
      Firebase.database(
          "https://turnierplaner-86dfe-default-rtdb.europe-west1.firebasedatabase.app/")
  private val reference = "Tournaments"

  @Before
  fun setUp() {
    getParticipantsFromDb()
    createAddToAllTournaments(tourney.name, numberOfTeams, 0, 0)
  }

  @After
  fun tearDown() {
    removeTournament(tourney)
  }

  @Test fun getDatabase() {}

  @Test fun getRefreshActivate() {}

  @Test fun setRefreshActivate() {}

  @Test
  fun pushLocalToDbAddTournament() {
    pushLocalToDb()
    assertEquals(id, database.getReference(reference).child(tourney.id).key)
  }

  @Test
  fun pushLocalToDbAddPlayer() {
    addParticipantToTournament(tourney.name, "TestPlayer1")
    pushLocalToDb()
    Thread.sleep(5005)
    assertEquals("TestPlayer1", findTournament(tourney.name).participants[0].name)
  }

  @Test
  fun removeTournament() {
    removeTournament(tourney)
    getParticipantsFromDb()
    assertEquals("", findTournament(tourney.name).name)
  }

  @Test fun getParticipantsFromDb() {}

  @Test fun findTournamentIndex(id: String){}

  @Test fun containsTournament(){}

  @Test fun findParticipantIndex(){}




}
