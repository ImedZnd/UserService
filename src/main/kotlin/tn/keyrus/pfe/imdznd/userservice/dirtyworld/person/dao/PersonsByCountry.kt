package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao

data class PersonsByCountry(
    val country: String = "",
    val numberOfUsers: Long = 0,
){
    override fun toString(): String {
        return "PersonsByCountry(country='$country', numberOfUsers=$numberOfUsers)"
    }
}
