package com.example.turnierplaner.tournament.tournamentDB

import android.content.Context
import androidx.navigation.NavHostController
import com.example.turnierplaner.homescreen.joinTournament
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.RefreshPopUp

import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.allTournamentContainsTournamentID
import com.example.turnierplaner.tournament.leagueSystem.changeState
import com.example.turnierplaner.tournament.leagueSystem.dummyAllTournament
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.leagueSystem.message
import com.example.turnierplaner.tournament.leagueSystem.showRefreshPopUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.turnierplaner.tournament.leagueSystem.schedule.Result

val database =
    Firebase.database("https://turnierplaner-86dfe-default-rtdb.europe-west1.firebasedatabase.app/")
const val reference: String = "Tournaments"
var refreshActivate = false
var loggedInUser: String = FirebaseAuth.getInstance().currentUser?.uid.toString()


fun pushLocalToDb() {
    for (tournament in allTournament) {
        database.getReference(reference).child(tournament.id).setValue(tournament)
    }
    changeState++
}

fun removeTournament(tourney: Tournament) {
    val tournament = allTournament[findTournamentIndex(tourney.id)]
    for (participant in tourney.participants){
        participant.id = ""
    }
    allTournament[findTournamentIndex(tourney.id)] = tournament
    pushLocalToDb()
    database.getReference("Tournaments").child(tourney.id).removeValue()
    val tourney = findTournament(tourney.name)
    allTournament.remove(tourney)
    changeState++
}

fun getTournamentFromDB(inviteTournamentName: String, navController: NavHostController, context: Context, inviteCodeLocal: Int){

    database.getReference(reference).addValueEventListener(object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val items: Iterator<DataSnapshot> = dataSnapshot.children.iterator()
            while (items.hasNext()) {
                val item: DataSnapshot = items.next()
                val name: String = item.getValue(Tournament::class.java)!!.name
                val id: String? = item.key
                val numberOfParticipants: Int =
                    item.getValue(Tournament::class.java)!!.numberOfParticipants
                val participants: MutableList<Participant> =
                    item.getValue(Tournament::class.java)!!.participants
                val pointsVic: Int = item.getValue(Tournament::class.java)!!.pointsVictory
                val pointsTie: Int = item.getValue(Tournament::class.java)!!.pointsTie
                val schedule: MutableList<MutableList<Result>>? =
                    item.getValue(Tournament::class.java)!!.schedule
                val inviteCode: Int =  item.getValue(Tournament::class.java)!!.inviteCode
                if (id != null) {
                    val tourney =
                        Tournament(
                            name,
                            id,
                            numberOfParticipants,
                            pointsVic,
                            pointsTie,
                            participants,
                            schedule,
                            inviteCode
                        )
                    if (tourney.name == inviteTournamentName){

                        dummyAllTournament.clear()
                        dummyAllTournament.add(tourney)
                        database.getReference(reference).removeEventListener(this)
                        joinTournament(inviteTournamentName, navController, context, inviteCodeLocal)
                        return
                    }
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })

}

