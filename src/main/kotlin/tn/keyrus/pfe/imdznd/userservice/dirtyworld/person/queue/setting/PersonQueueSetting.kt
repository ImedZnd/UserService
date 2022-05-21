package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "person.event")
@ConstructorBinding
object PersonQueueSetting {

    var save: Event? = null
    var update: Event? = null
    var delete: Event? = null
    var flag: Event? = null
    var fraud: Event? = null
    var unfraud: Event? = null

    @ConstructorBinding
    data class Event(
        val queue: String = "",
        val exchange: String = "",
        val routingkey: String = ""
    )
}