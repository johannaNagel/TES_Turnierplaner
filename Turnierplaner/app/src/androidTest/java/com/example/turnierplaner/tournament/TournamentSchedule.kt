/* (C)2022 */
package com.example.turnierplaner.tournament

/*@RunWith(AndroidJUnit4::class)
class TournamentSchedule {
   var competition: TournamentClass? = null
    var listPlayer: MutableList<Player>? = null
   var listWithAllGames : ListResult? = null


@Before
fun preparation() {
    listPlayer = mutableListOf<Player>(
        Player("1", 0, 0, 1, "1"),
        Player("2", 0, 0, 2, "2"),
        Player("3", 0, 0, 3, "3"),
        Player("4", 0, 0, 4, "4"),
        Player("5", 0, 0, 5, "5"),
        Player("6", 0, 0, 6, "6"),
        Player("7", 0, 0, 7, "7"),
        Player("8", 0, 0, 8, "8"),
    )
    //competition = TournamentClass("hello", "1", 10, listPlayer!!, 3, 1)
}

@After
   fun afterTest() {
   listPlayer = null
   competition = null
   listWithAllGames = null
    }

*/



// @Test
// fun testingCreateSchedule() {
// val playerNumber = getNumberOfActualPlayers(competition!!)
// val scheduleRound1 = createSchedule(competition!!, playerNumber)
// for (i in scheduleRound1) {
// assertTrue(i.player1.name == (scheduleRound1.indexOf(i) * 2 + 1).toString())
// assertTrue(i.player2.name == (scheduleRound1.indexOf(i) * 2 + 2).toString())
// }
// }
//
// @Test
// fun testingScheduleUnEvenNumberOfPlayer() {
// listPlayer!!.add(Player("9", 0, 0, 9, "9"))
// val playerNumber = getNumberOfActualPlayers(competition!!)
// listWithAllGames = ListResult(competition!!)
// val scheduleRound1 = listWithAllGames!!.allGames[0]
//
// for (i in scheduleRound1) {
// assertTrue(i.player1.name == (scheduleRound1.indexOf(i) * 2 + 1).toString())
// if (scheduleRound1.last() != i) {
// assertTrue(i.player2.name == (scheduleRound1.indexOf(i) * 2 + 2).toString())
// } else {
// assertTrue(i.player2.name == "")
// }
// }
// }
//
// @Test
// fun rotateSchedule() {
// val scheduleRound1 = createSchedule(competition!!, getNumberOfActualPlayers(competition!!))
// listWithAllGames = ListResult(competition!!)
// val bu = listWithAllGames!!.allGames[1]
// assertTrue(bu[0].player1.name == "1")
// assertTrue(bu[0].player2.name == "4")
// assertTrue(bu[1].player1.name == "2")
// assertTrue(bu[1].player2.name == "6")
// assertTrue(bu[2].player1.name == "3")
// assertTrue(bu[2].player2.name == "8")
// assertTrue(bu[3].player1.name == "5")
// assertTrue(bu[3].player2.name == "7")
// assertTrue(bu[4].player1.name == "9")
// }
//
// @Test
// fun rotateScheduleUnEvenNumberOfPlayerRound1() {
// listPlayer!!.add(Player("9", 0, 0, 9, "9"))
// val playerNumber = getNumberOfActualPlayers(competition!!)
// listWithAllGames = ListResult(competition!!)
// val bu = listWithAllGames!!.allGames[1]
// assertTrue(bu[0].player1.name == "1")
// assertTrue(bu[0].player2.name == "4")
// assertTrue(bu[1].player1.name == "2")
// assertTrue(bu[1].player2.name == "6")
// assertTrue(bu[2].player1.name == "3")
// assertTrue(bu[2].player2.name == "8")
// assertTrue(bu[3].player1.name == "5")
// assertTrue(bu[3].player2.name == "")
// assertTrue(bu[4].player1.name == "7")
// assertTrue(bu[4].player2.name == "9")
//
// }
//
// @Test
// fun rotateScheduleUnEvenNumberOFPlayerRound4(){
// listPlayer!!.add(Player("9", 0, 0, 9, "9"))
// val playerNumber = getNumberOfActualPlayers(competition!!)
// listWithAllGames = ListResult(competition!!)
// val bu = listWithAllGames!!.allGames[1]
// assertTrue(bu[0].player1.name == "1")
// assertTrue(bu[0].player2.name == "5")
// assertTrue(bu[1].player1.name == "7")
// assertTrue(bu[1].player2.name == "3")
// assertTrue(bu[2].player1.name == "9")
// assertTrue(bu[2].player2.name == "2")
// assertTrue(bu[3].player1.name == "")
// assertTrue(bu[3].player2.name == "4")
// assertTrue(bu[4].player1.name == "8")
// assertTrue(bu[4].player2.name == "6")
// }
//
// @Test
// fun correctGetRow(){
// val numberPlayers = 6
// val numberPlayers2 = 7
// assertTrue(getRow(numberPlayers) == 3)
// assertFalse(getRow(numberPlayers2) == 3)
// assertTrue(getRow(numberPlayers2) == 4)
//
// }
//
// @Test
// fun correctWin(){
// val  resultGame1 = "4"
// val resultGame2 = "0"
// var  resultGame3 = "0"
// var resultGame4 = "0"
// assertTrue(winOrTie(resultGame1, resultGame2) == "winner1")
// assertFalse(winOrTie(resultGame3, resultGame4)=="winner2")
// assertTrue(winOrTie(resultGame3, resultGame4) =="tie")
// resultGame3 = ""
// resultGame4 = ""
// assertTrue(winOrTie(resultGame3, resultGame4) == "")
// }
//
// @Test
// fun checkGamePlayed(){
// listPlayer!!.add(Player("9", 0, 0, 9, "9"))
// val playerNumber = getNumberOfActualPlayers(competition!!)
// listWithAllGames = ListResult(competition!!)
// val bu = listWithAllGames!!.allGames[7]
// bu[3].resultPlayer1 = "4"
// bu[3].resultPlayer2 = "5"
// assertTrue(checkIfGamePlayed(bu[3].player1.name, bu[3].player1.name))
//
// }
//
// @Test
// fun checkNumberOfActualPlayers(){
// assertTrue(getNumberOfActualPlayers(competition!!).equals("8"))
// }
//
// @Test
// fun checkAddResultPointsWinPlayer1(){
// addResultPoints(competition!!, "winner1", "1", "5")
// for(i in competition!!.players){
// if(i.name == "1"){
// assertTrue(i.points == 3)
// assertTrue(i.games == 1)
// } else if(i.name == "5")
// assertTrue(i.points == 0)
// assertTrue(i.games == 1)
// }
//
// }
//
// @Test
// fun checkAddResultPointsWinPlayer2(){
// addResultPoints(competition!!, "winner2", "1", "5")
// for(i in competition!!.players){
// if(i.name == "5"){
// assertTrue(i.points == 3)
// assertTrue(i.games == 1)
// } else if(i.name == "1")
// assertTrue(i.points == 0)
// assertTrue(i.games == 1)
// }
//
// }
//
// @Test
// fun checkAddResultPointsTie(){
// addResultPoints(competition!!, "tie", "1", "5")
// for(i in competition!!.players){
// if(i.name == "5"){
// assertTrue(i.points == 1)
// assertTrue(i.games == 1)
// } else if(i.name == "1")
// assertTrue(i.points == 1)
// assertTrue(i.games == 1)
// }
//
// }
//
// @Test
// fun checkAddResultPoints(){
// addResultPoints(competition!!, "", "1", "5")
// for(i in competition!!.players){
// if(i.name == "5"){
// assertTrue(i.points == 0)
// assertTrue(i.games == 0)
// } else if(i.name == "1")
// assertTrue(i.points == 0)
// assertTrue(i.games == 0)
// }
//
// }
//
// @Test
// fun checkAddResultPointsChange(){
// addResultPoints(competition!!, "winner1", "1", "5")
// addResultPointsChange(competition!!, "winner2", player1name = "1", "5")
// for(i in competition!!.players){
// if(i.name == "5"){
// assertTrue(i.points == 3)
// assertTrue(i.games == 1)
// } else if(i.name == "1")
// assertTrue(i.points == 0)
// assertTrue(i.games == 1)
// }
// addResultPointsChange(competition!!, "tie", player1name = "1", "5")
// for(i in competition!!.players){
// if(i.name == "5"){
// assertTrue(i.points == 1)
// assertTrue(i.games == 1)
// } else if(i.name == "1")
// assertTrue(i.points == 1)
// assertTrue(i.games == 1)
// }
// addResultPointsChange(competition!!, "", player1name = "1", "5")
// for(i in competition!!.players){
// if(i.name == "5"){
// assertTrue(i.points == 0)
// assertTrue(i.games == 0)
// } else if(i.name == "1")
// assertTrue(i.points == 0)
// assertTrue(i.games == 0)
// }
//
//
//
// }





 /*        @Test
         fun checkAddResultPointsChangeTie(){
             addResultPoints(competition!!, "tie", "1", "5")
             addResultPointsChange(competition!!, "winner2", player1name = "1", "5")
             for(i in competition!!.players) {
                 if (i.name == "5") {
                     assertTrue(i.points == 3)
                     assertTrue(i.games == 1)
                 } else if (i.name == "1") {
                     assertTrue(i.points == 0)
                     assertTrue(i.games == 1)
                 }
             }
             addResultPointsChange(competition!!, "winner1", player1name = "1", "5")
             for(i in competition!!.players) {
                 if (i.name == "5") {
                     assertTrue(i.points == 0)
                     assertTrue(i.games == 1)
                 } else if (i.name == "1") {

                     assertTrue(i.points == 3)
                     assertTrue(i.games == 1)
                 }
             }
             addResultPointsChange(competition!!, "", player1name = "1", "5")
             for(i in competition!!.players){
                 if(i.name == "5"){
                     assertTrue(i.points == 0)
                     assertTrue(i.games == 0)
                 } else if(i.name == "1") {

                     assertTrue(i.points == 0)
                     assertTrue(i.games == 0)
                 }
             }
         }


         @Test
         fun checkAddResultToListWin(){
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("1", "2", "4", "0", 1)
             assertTrue(listWithAllGames!!.allGames[0][0].resultPlayer1 == "4")
             assertTrue(listWithAllGames!!.allGames[0][0].resultPlayer2 == "0")
             assertTrue(listWithAllGames!!.allGames[0][0].player1.name == "1")
             assertTrue(listWithAllGames!!.allGames[0][0].player1.name == "0")

         }

         @Test
         fun checkAddResultToListEmpty(){
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("1", "2", "", "", 1)
             assertTrue(listWithAllGames!!.allGames[0][0].resultPlayer1 == "")
             assertTrue(listWithAllGames!!.allGames[0][0].resultPlayer2 == "")
             assertTrue(listWithAllGames!!.allGames[0][0].player1.name == "1")
             assertTrue(listWithAllGames!!.allGames[0][0].player1.name == "0")
         }

         @Test
         fun checkWhichRound(){
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("1", "2", "0", "1", 1)
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("3", "4", "0", "1", 1)
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("5", "6", "0", "1", 1)
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("7", "8", "0", "1", 1)
             assertTrue(methodWhichRound()==2)

         }

         @Test
         fun checkWhichRoundRound1(){
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("1", "2", "0", "1", 1)
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("3", "4", "0", "1", 1)
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("5", "6", "0", "1", 1)
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("7", "8", "", "", 1)
             assertTrue(methodWhichRound()==1)

         }

         @Test
         fun checkGameResult(){
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("1", "2", "0", "1", 1)
             val result = getGameResult("1 vs. 2")
             assertTrue(result.resultPlayer1 == "0")
             assertTrue(result.resultPlayer2 == "1")

         }

         @Test
         fun checkGameResultEmptyResult(){
             listWithAllGames = ListResult(competition!!)
             addResultToResultList("1", "2", "", "", 1)
             val result = getGameResult("1 vs. 2")
             assertTrue(result.resultPlayer1 == "")
             assertTrue(result.resultPlayer2 == "")
         }
 }


 }

  */
