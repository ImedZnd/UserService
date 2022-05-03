package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.configuration

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting.PersonQueueSetting

@Service
data class PersonQueueInitializer(
    @Autowired val rabbitAdmin: RabbitAdmin,
    private val personQueueSetting: PersonQueueSetting
) {

    @EventListener(ApplicationReadyEvent::class)
    fun onStart() {
        createCommunicationPipe(
            rabbitAdmin,
            personQueueSetting.save?.queue?:"",
            personQueueSetting.save?.exchange?:"",
            personQueueSetting.save?.routingkey?:""
        )
        createCommunicationPipe(
            rabbitAdmin,
            personQueueSetting.update?.queue?:"",
            personQueueSetting.update?.exchange?:"",
            personQueueSetting.update?.routingkey?:""
        )
        createCommunicationPipe(
            rabbitAdmin,
            personQueueSetting.delete?.queue?:"",
            personQueueSetting.delete?.exchange?:"",
            personQueueSetting.delete?.routingkey?:""
        )
        createCommunicationPipe(
            rabbitAdmin,
            personQueueSetting.flag?.queue?:"",
            personQueueSetting.flag?.exchange?:"",
            personQueueSetting.flag?.routingkey?:""
        )
        createCommunicationPipe(
            rabbitAdmin,
            personQueueSetting.fraud?.queue?:"",
            personQueueSetting.fraud?.exchange?:"",
            personQueueSetting.fraud?.routingkey?:""
        )
        createCommunicationPipe(
            rabbitAdmin,
            personQueueSetting.unfraud?.queue?:"",
            personQueueSetting.unfraud?.exchange?:"",
            personQueueSetting.unfraud?.routingkey?:""
        )
    }

    private fun createCommunicationPipe(
        rabbitAdmin: RabbitAdmin,
        queueName: String,
        exchangeName: String,
        routingkey: String
    ) {
        val queue = Queue(queueName)
        rabbitAdmin.declareQueue(queue)
        val exchange = ExchangeBuilder.directExchange(exchangeName).build<Exchange>()
        rabbitAdmin.declareExchange(exchange)
        val binding: Binding = BindingBuilder.bind(queue).to(exchange).with(routingkey).noargs()
        rabbitAdmin.declareBinding(binding)
    }
}