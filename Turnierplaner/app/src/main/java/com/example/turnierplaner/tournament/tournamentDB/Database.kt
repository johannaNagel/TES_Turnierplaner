package com.example.turnierplaner.tournament.tournamentDB

import android.content.Context
import androidx.navigation.NavHostController
import com.example.turnierplaner.homescreen.joinTournament
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.RefreshPopUp

import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.changeState
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

// Database54736
val database =
    Firebase.database("https://turnierplaner-86dfe-default-rtdb.europe-west1.firebasedatabase.app/")
const val reference: String = "Tournaments"
var refreshActivate = false
var loggedInUser: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
// countDataChange is set in method onDatachanged()
// when count is on 0, allTournaments are initially filled with Tournaments from DB
// when its 1 allTournaments was already initialized
var countDataChange = 0
var participantInTournament = 0

fun pushLocalToDb() {
    for (s in allTournament) {
        database.getReference(reference).child(s.id).setValue(s)
    }
    changeState++
}

fun removeTournament(tourney: Tournament) {
    database.getReference("Tournaments").child(tourney.id).removeValue()
    val tourney = findTournament(tourney.name)
    allTournament.remove(tourney)
    changeState++
}
/*
This method is triggered when a Change is made to any Tournament in Database
The method will check if there are changes made to a tourney in which the currently logged in user
participates, if so, a pop up will shown which notify about the change tht were made
 */
fun getParticipantsFromDb() {
    database.getReference(reference).addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items: Iterator<DataSnapshot> = snapshot.children.iterator()
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
                    val inviteCode: Int =
                        item.getValue(Tournament::class.java)!!.inviteCode
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
                                inviteCode)
                        //states if user is a participant
                        participantInTournament = 0
                        // We want to only load that tournaments, in which the currently logged in user
                        // participates
                        // so we check if the participants list contains the currently logged in user
                        for (participant in tourney.participants) {
                            if (participant.id == loggedInUser) {
                                participantInTournament = 1
                                // initialize allTournament from DB
                                if (countDataChange == 0) {
                                    allTournament.add(tourney)
                                    break
                                }
                                // check if this tourney was newly added
                                if (countDataChange == 1 && findTournament(tourney.name).name == "") {
                                    allTournament.add(tourney)
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                                // In the next if conditions we check if changes where made to a Tournament,
                                // in which the current user participates
                                val tourneyFromAll: Tournament = findTournament(tourney.name)
                                if (tourney.numberOfParticipants != tourneyFromAll.numberOfParticipants) {
                                    message = "Number of participants has changed in tournament:" + tourney.name + "."
                                    allTournament[findTournamentIndex(tourney.id)] = tourney
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                                if(tourney.name != tourneyFromAll.name) {
                                    message = "Name of tournament:" + tourney.name + "has changed."
                                    allTournament[findTournamentIndex(tourney.id)] = tourney
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                                if(tourney.pointsVictory != tourneyFromAll.pointsVictory){
                                    message = "Points for victory has changed in tournament:" + tourney.name + "."
                                    allTournament[findTournamentIndex(tourney.id)] = tourney
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                                if(tourney.pointsTie != tourneyFromAll.pointsTie){
                                    message = "Points for tie has changed in tournament:" + tourney.name + "."
                                    allTournament[findTournamentIndex(tourney.id)] = tourney
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                                if(participantAdded(tourney.participants, tourneyFromAll.participants, tourney.numberOfParticipants)){
                                    message = "A new participant was added in tournament:" + tourney.name + "."
                                    allTournament[findTournamentIndex(tourney.id)] = tourney
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                                if(pointsChanged(tourney.participants, tourneyFromAll.participants, tourney.numberOfParticipants)) {
                                    message = "An update in Schedule was made in tournament:" + tourney.name + "."
                                    allTournament[findTournamentIndex(tourney.id)] = tourney
                                    if (refreshActivate) {
                                        showRefreshPopUp.value = true
                                    }
                                    break
                                }
                            }
                        }
                        //If user is kicked out from tournament
                        if (participantInTournament == 0 &&
                            countDataChange == 1 &&
                            containsTournament(tourney)) {
                            allTournament.removeAt(findTournamentIndex(tourney.id))
                            if (refreshActivate) {
                                showRefreshPopUp.value = true
                            }
                        }
                        //TODO:If tournament is deleted
                    }
                }
                countDataChange = 1
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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

                        allTournament.add(tourney)
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

fun findTournamentIndex(id: String): Int {
    var count = 0
    for (tournament in allTournament) {
        if (tournament.id == id) {
            break
        } else {
            count++
        }
    }
    return count
}

fun containsTournament(tournament: Tournament): Boolean {
    for (tourney in allTournament) {
        if (tourney.id == tournament.id) {
            return true
        }
    }
    return false
}

//If the currently logged-in User is part of the participants list, return the index in the list
fun findParticipantIndex(uid: String, participants: MutableList<Participant>): Int {
    var count = 0
    for (participant in participants) {
        if (participant.id == uid) {
            break
        } else {
            count++
        }
    }
    return count
}

fun pointsChanged(participantsFromDB: MutableList<Participant>, participantsFromLocal: MutableList<Participant>, numberOfParticipants: Int): Boolean {
    var pointschanged = false
    for (i in 0 until numberOfParticipants){
        if (participantsFromDB[i].points != participantsFromLocal[i].points){
            pointschanged = true
            break
        }
    }
    return pointschanged
}

fun participantAdded(participantsFromDB: MutableList<Participant>, participantsFromLocal: MutableList<Participant>, numberOfParticipants: Int): Boolean {
    var participantadded = false
    for (i in 0 until numberOfParticipants){
        if (participantsFromDB[i].name != participantsFromLocal[i].name){
            participantadded = true
            break
        }
    }
    return participantadded
}