package com.github.mathieus.syncer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mathieus.syncer.dto.MysqlCreditApplicationDTO;
import com.github.mathieus.syncer.dto.PostgresCreditApplicationDTO;
import com.github.mathieus.syncer.service.MysqlService;
import com.github.mathieus.syncer.service.PostgresService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChangeMessageListener {

    private final ObjectMapper objectMapper;
    private final MysqlService mySqlService;
    private final PostgresService postgresService;
    private final RabbitTemplate rabbitTemplate;

    public ChangeMessageListener(
            ObjectMapper objectMapper,
            MysqlService mySqlService,
            PostgresService postgresService,
            RabbitTemplate rabbitTemplate) {

        this.objectMapper = objectMapper;
        this.mySqlService = mySqlService;
        this.postgresService = postgresService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "credit_changes")
    public void onMessage(String messageJson) throws JsonProcessingException {
        try {
            MysqlCreditApplicationDTO dto = objectMapper.readValue(messageJson, MysqlCreditApplicationDTO.class);

            String nationalId = mySqlService.getNationalId(dto.getAgencyId());

            postgresService.upsert(PostgresCreditApplicationDTO.of(dto, nationalId));

        } catch (Exception e) {
            rabbitTemplate.convertAndSend("credit_changes_error", messageJson);
        }
    }
}