/* (C)2022 */
package com.example.turnierplaner.tournament.tournamentDB

import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.Result
import com.example.turnierplaner.tournament.leagueSystem.TournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.changeState
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.leagueSystem.showRefreshPopUp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// Database
val database =
    Firebase.database("https://turnierplaner-86dfe-default-rtdb.europe-west1.firebasedatabase.app/")
const val reference: String = "Tournaments"
var refreshActivate = false

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

fun getParticipantsFromDb() {
  database
      .getReference(reference)
      .addValueEventListener(
          object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
              allTournament.clear()
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
                if (id != null) {
                  val tourney =
                      Tournament(
                          name,
                          id,
                          numberOfParticipants,
                          pointsVic,
                          pointsTie,
                          participants,
                          schedule)
                  allTournament.add(tourney)
                }
              }

              if (refreshActivate) {
                showRefreshPopUp.value = true
              }
            }
            override fun onCancelled(error: DatabaseError) {
              TODO("Not yet implemented")
            }
          })
}

fun addEventListenerDb() {
  database
      .getReference(reference)
      .addChildEventListener(
          object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
              val tourney = snapshot.getValue(Tournament::class.java)
              if (tourney != null) {
                tourney.id = snapshot.key.toString()
                createAddToAllTournaments(
                    tourney.name,
                    tourney.numberOfParticipants,
                    tourney.id,
                    tourney.pointsVictory,
                    tourney.pointsTie)
                // Change State
                changeState++
                println("changed1")
              }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
              val id: String = snapshot.key.toString()
              for (s in allTournament) {
                if (s.id == id) {
                  val tourney = snapshot.getValue(Tournament::class.java)
                  if (tourney != null) {
                    s.name = tourney.name
                    s.numberOfParticipants = tourney.numberOfParticipants
                    s.participants = tourney.participants

                    // Change State
                    changeState++
                    println("changed2")
                  }
                }
              }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
              // Change State
              changeState++
              println("changed3")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
              TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
              TODO("Not yet implemented")
            }
          })
}

fun updateDb() {
  database.getReference("Tournaments").addChildEventListener(QuotesChildEventListener())
}

class QuotesChildEventListener : ChildEventListener {
  /**
   * This method is triggered when a new child is added to the location to which this listener was
   * added.
   *
   * @param snapshot An immutable snapshot of the data at the new child location
   * @param previousChildName The key name of sibling location ordered before the new child. This
   * will be null for the first child node of a location.
   */
  override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
    val tourney = snapshot.getValue(Tournament::class.java)
    if (tourney != null) {
      tourney.id = snapshot.key.toString()
      createAddToAllTournaments(
          tourney.name,
          tourney.numberOfParticipants,
          tourney.id,
          tourney.pointsVictory,
          tourney.pointsTie)
      // Change State
      changeState++
      println("changed1")
    }
  }

  /**
   * This method is triggered when the data at a child location has changed.
   *
   * @param snapshot An immutable snapshot of the data at the new data at the child location
   * @param previousChildName The key name of sibling location ordered before the child. This will
   * be null for the first child node of a location.
   */
  override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
    val id: String = snapshot.key.toString()
    for (s in allTournament) {
      if (s.id == id) {
        val tourney = snapshot.getValue(Tournament::class.java)
        if (tourney != null) {
          s.name = tourney.name
          s.numberOfParticipants = tourney.numberOfParticipants
          s.participants = tourney.participants

          // Change State
          changeState++
          println("changed2")
        }
      }
    }
  }

  /**
   * This method is triggered when a child is removed from the location to which this listener was
   * added.
   *
   * @param snapshot An immutable snapshot of the data at the child that was removed.
   */
  override fun onChildRemoved(snapshot: DataSnapshot) {
    // Change State
    changeState++
    println("changed3")
  }

  /**
   * This method is triggered when a child location's priority changes.
   *
   * @param snapshot An immutable snapshot of the data at the location that moved.
   * @param previousChildName The key name of the sibling location ordered before the child
   * location. This will be null if this location is ordered first.
   */
  override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
    TODO("Not yet implemented")
  }

  /**
   * This method will be triggered in the event that this listener either failed at the server, or
   * is removed as a result of the security and Firebase rules. For more information on securing
   * your data, see:
   * [ Security Quickstart](https://firebase.google.com/docs/database/security/quickstart)
   *
   * @param error A description of the error that occurred
   */
  override fun onCancelled(error: DatabaseError) {
    TODO("Not yet implemented")
  }
}
