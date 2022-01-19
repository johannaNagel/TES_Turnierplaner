package com.example.turnierplaner.DB

import com.example.turnierplaner.tournament.leagueSystem.Player
import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.getTeamsFromDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID

class TestingAddingToDB {

    private val name = "TestDB"
    private val id = UUID.randomUUID().toString()
    private val numberOfTeams = 1
    private val players = mutableListOf<Player>()
    private val tourney = TournamentClass(name, id, numberOfTeams, players)
    private val database = Firebase.database("https://turnierplaner-86dfe-default-rtdb.europe-west1.firebasedatabase.app/")
    private val reference = "Tournaments"


    @Before
    fun initialize(){
        getTeamsFromDb()
        createAddToAllTournaments(tourney.name, numberOfTeams)
    }

    @Test
    fun testAddingTournamentToDB(){
        pushLocalToDb()
        assertEquals(id, database.getReference(reference).child(tourney.id).key)
    }

    @After
    fun tearDown(){
        removeTournament(tourney)
    }
}