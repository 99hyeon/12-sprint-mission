package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.File;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        clearFileData();

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class,
            args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

		test(userService, channelService, messageService);
    }

    private static void test(UserService userService, ChannelService channelService,
        MessageService messageService) {
        System.out.println("=== User 테스트 ===");
        UserResponse user = userService.create(
            new UserCreateRequest(
                "user1@gmail.com",
                "유저1",
                "유저일",
                "password1",
                null
            )
        );
        System.out.println("유저 생성: " + user.id());
        System.out.println("유저 조회: " + userService.find(user.id()).userName());

        UserResponse updatedUser = userService.update(
            new UserUpdateRequest(
                user.id(),
                "user1update@gmail.com",
                "유저1수정",
                "유저일수정",
                null
            )
        );
        System.out.println("유저 수정: " + updatedUser.userName());

        System.out.println("\n=== Channel 테스트 ===");
        ChannelResponse channel = channelService.createPublic(
            new ChannelPublicCreateRequest(
                "채널1",
                "공지 제목",
                "공지 내용"
            )
        );
        System.out.println("채널 생성: " + channel.id());
        System.out.println("채널 조회: " + channelService.find(channel.id()).name());

        ChannelResponse updatedChannel = channelService.update(
            new ChannelUpdateRequest(
                channel.id(),
                "채널1수정",
                "공지 제목 수정",
                "공지 내용 수정"
            )
        );
        System.out.println("채널 수정: " + updatedChannel.name());

        System.out.println("\n=== Message 테스트 ===");
        MessageResponse message = messageService.create(
            new MessageCreateRequest(
                "안녕하세요",
                channel.id(),
                user.id(),
                List.of()
            )
        );
        System.out.println("메시지 생성: " + message.id());
        System.out.println("메시지 조회: " + messageService.find(message.id()).content());

        MessageResponse updatedMessage = messageService.update(
            new MessageUpdateRequest(
                message.id(),
                "안녕하세요 수정"
            )
        );
        System.out.println("메시지 수정: " + updatedMessage.content());

        messageService.delete(message.id());
        System.out.println(
            "메시지 삭제 후 개수: " + messageService.findAllByChannelId(channel.id()).size());
    }

    private static void clearFileData() {
        deleteFile("data/users.ser");
        deleteFile("data/channels.ser");
        deleteFile("data/messages.ser");
        deleteFile("data/binaryContents.ser");
        deleteFile("data/readStatuses.ser");
        deleteFile("data/userStatuses.ser");
    }

    private static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

}
