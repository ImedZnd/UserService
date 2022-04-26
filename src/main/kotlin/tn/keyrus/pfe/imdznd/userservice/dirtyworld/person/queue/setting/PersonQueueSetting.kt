package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "person.event")
object PersonQueueSetting{

     var save: Event?=null
     var update: Event?=null
     var delete: Event?=null
     var flag: Event?=null

    data class Event(
         val queue:String,
         val exchange:String,
         val routingkey:String
    )
}