package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // TODO: context에서 Bean 조회하여 각 서비스 구현체에 할당
        ChannelService channelService = (ChannelService) context.getBean("basicChannelService");
        MessageService messageService = (MessageService) context.getBean("basicMessageService");
        UserService userService = (UserService) context.getBean("basicUserService");

        channelService.create(ChannelType.PUBLIC, "아무거나", "테스트용 채널");
    }

}
