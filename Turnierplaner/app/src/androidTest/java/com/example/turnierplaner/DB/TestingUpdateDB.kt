package com.example.turnierplaner.DB

import com.example.turnierplaner.tournament.leagueSystem.Player
import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.addPlayerToTournament
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.addTournamentToDb
import com.example.turnierplaner.tournament.tournamentDB.getTeamsFromDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID

class TestingUpdateDB {

    private val name = "TestDB"
    private val id = UUID.randomUUID().toString()
    private val numberOfTeams = 5
    private val players = mutableListOf<Player>()
    private val tourney = TournamentClass(name, id, numberOfTeams, players)


    @Before
    fun initialize(){
        getTeamsFromDb()
        createAddToAllTournaments(tourney.name, numberOfTeams)
    }

    @Test
    fun testAddPlayerToTournament(){
        addPlayerToTournament(tourney.name, "TestPlayer1")
        addTournamentToDb()
        Thread.sleep(5005)
        assertEquals("TestPlayer1", findTournament(tourney.name).players[0].name)
    }

    @After
    fun tearDown() {
        removeTournament(tourney)
    }
}