package com.casecode.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val alerts: List<Alert>,
    val email: String,
    val id: String,
    val lists: List<Lists>,
    val name: String,
    val photo: String
)

@Serializable
data class UserResponse(
    val users: List<User>
)

@Serializable
data class Location(
    val address: String,
    val latitude: String,
    val longitude: String,
    val type: String
)

@Serializable
data class Lists(
    val color: String,
    val icon: String,
    val id: String,
    val name: String
)

@Serializable
data class AssignedTo(
    val email: String,
    val name: String,
    val photo: String
)

@Serializable
data class Alert(
    val actions: List<Action>,
    val assignedTo: AssignedTo,
    val id: String,
    val notes: String,
    val reminder: String,
    val status: String,
    val title: String
)

@Serializable
data class Action(
    val icon: String,
    val location: Location,
    val type: String
)
