package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.publisher

import org.springframework.amqp.rabbit.core.RabbitTemplate
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting.PersonQueueSetting

class PersonQueuePublisher(
    private val rabbitTemplate: RabbitTemplate,
    private val personQueueSetting: PersonQueueSetting
) {

    fun publishSavePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.save?.exchange ?: "",
            personQueueSetting.save?.routingkey ?: ""
        )
    }

    fun publishUpdatePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.update?.exchange ?: "",
            personQueueSetting.update?.routingkey ?: ""
        )
    }

    fun publishDeletePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.delete?.exchange ?: "",
            personQueueSetting.delete?.routingkey ?: ""
        )
    }

    fun publishFlagPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.flag?.exchange ?: "",
            personQueueSetting.flag?.routingkey ?: ""
        )
    }

    fun publishFraudPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.fraud?.exchange ?: "",
            personQueueSetting.fraud?.routingkey ?: ""
        )
    }

    fun publishUnFraudPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.unfraud?.exchange ?: "",
            personQueueSetting.unfraud?.routingkey ?: ""
        )
    }

    private fun publishEvent(
        personId: String,
        exchange: String,
        routingKey: String
    ) {
        rabbitTemplate.convertAndSend(
            exchange,
            routingKey,
            personId
        )
    }
}