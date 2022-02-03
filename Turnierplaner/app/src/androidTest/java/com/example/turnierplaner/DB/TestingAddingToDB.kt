/* (C)2022 */
package com.example.turnierplaner.DB

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.TournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestingAddingToDB {

  private val name = "TestDB"
  private val id = UUID.randomUUID().toString()
  private val numberOfTeams = 1
  private val players = mutableListOf<Participant>()
  private val tourney = Tournament(name, id, numberOfTeams, 0, 0, players, null)
  private val database =
      Firebase.database(
          "https://turnierplaner-86dfe-default-rtdb.europe-west1.firebasedatabase.app/")
  private val reference = "Tournaments"

  @Before
  fun initialize() {
    getParticipantsFromDb()
    createAddToAllTournaments(tourney.name, numberOfTeams, 0, 0)
  }

  @Test
  fun testAddingTournamentToDB() {
    pushLocalToDb()
    assertEquals(id, database.getReference(reference).child(tourney.id).key)
  }

  @After
  fun tearDown() {
    removeTournament(tourney)
  }
}
