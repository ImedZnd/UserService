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
            "savepersonqueue",
            "savepersonexchange",
            "savepersonroutingkey"
        )
        createCommunicationPipe(
            rabbitAdmin,
            "updatepersonqueue",
            "updatepersonexchange",
            "updatepersonroutingkey"
        )
        createCommunicationPipe(
            rabbitAdmin,
            "deletepersonqueue",
            "deletepersonexchange",
            "deletepersonroutingkey"
        )
        createCommunicationPipe(
            rabbitAdmin,
            "flagpersonqueue",
            "flagpersonexchange",
            "flagpersonroutingkey"
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