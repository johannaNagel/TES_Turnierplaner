
sealed class InviteScreens(val title: String, val route: String) {

    object Invite :
        InviteScreens(
            title = "invite",
            route = "invite_route/{tournamentName}",
        )

    object SelectName :
        InviteScreens(
            title = "select",
            route = "select_name_route/{tournamentName}",
        )

    object QRReader :
        InviteScreens(
            title = "qreader",
            route = "qreader_route",
        )

}