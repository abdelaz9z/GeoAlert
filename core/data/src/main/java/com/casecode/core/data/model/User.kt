package com.casecode.core.data.model

data class User(
    val alerts: List<Alert> = emptyList(),
    val email: String = "",
    val id: String = "",
    val lists: List<Lists> = emptyList(),
    val name: String = "",
    val photo: String = ""
) {
    // No-argument constructor needed by Firebase
    constructor() : this(
        alerts = emptyList(),
        email = "",
        id = "",
        lists = emptyList(),
        name = "",
        photo = ""
    )
}

data class Location(
    val address: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val type: String = ""
) {
    constructor() : this(address = "", latitude = "", longitude = "", type = "")
}

data class Lists(
    val color: String = "",
    val icon: String = "",
    val id: String = "",
    val name: String = ""
) {
    constructor() : this(color = "", icon = "", id = "", name = "")
}

data class AssignedTo(
    val email: String = "",
    val name: String = "",
    val photo: String = ""
) {
    constructor() : this(email = "", name = "", photo = "")
}

data class Alert(
    val actions: List<Action> = emptyList(),
    val assignedTo: AssignedTo = AssignedTo(),
    val id: String = "",
    val notes: String = "",
    val reminder: String = "",
    val status: Boolean = false,
    val title: String = ""
) {
    constructor() : this(
        actions = emptyList(),
        assignedTo = AssignedTo(),
        id = "",
        notes = "",
        reminder = "",
        status = false,
        title = ""
    )
}

data class Action(
    val icon: String = "",
    val location: Location = Location(),
    val type: String = ""
) {
    constructor() : this(icon = "", location = Location(), type = "")
}
