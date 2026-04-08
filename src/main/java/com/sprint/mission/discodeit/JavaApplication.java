package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        User user1 = new User("user1@gmail.com", "유저1", "유저일");
        User user2 = new User("user2@gmail.com", "유저2", "유저이");

        Channel channel1 = new Channel("채널1", true, "채널1입니다.", "자유롭게 사용 가능");
        Channel channel2 = new Channel("채널2", false, "채널2입니다.", "비밀 채널");

        Message message1 = new Message(channel1, user1, "채널1 여러분 반갑습니다.");
        Message message2 = new Message(channel1, user1, "채널1 여러분 반갑습니다2.");

        System.out.println("/----------------Service JCF 구현체들 테스트----------------/");
        testJCFService(user1, user2, channel1, channel2, message1, message2);

        clearFileData();
        System.out.println("\n/----------------Service File 구현체들 테스트----------------/");
        testFileService(user1, user2, channel1, channel2, message1, message2);

        System.out.println("\n/----------------Service Basic JCF 구현체들 테스트----------------/");
        testBasicJCFService(user1, user2, channel1, channel2, message1, message2);

        clearFileData();
        System.out.println("\n/----------------Service Basic File 구현체들 테스트----------------/");
        testBasicFileService(user1, user2, channel1, channel2, message1, message2);

    }

    private static void testBasicFileService(User user1, User user2, Channel channel1, Channel channel2,
        Message message1, Message message2) {
        UserRepository userRepository = new FileUserRepository("data/users.ser");
        ChannelRepository channelRepository = new FileChannelRepository("data/channels.ser");
        MessageRepository messageRepository = new FileMessageRepository("data/messages.ser");

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, userService, channelService);

        System.out.println("BasicUserService 테스트");
        userServiceTest(user1, user2, userService);

        System.out.println("\nBasicChannelService 테스트");
        channelServiceTest(channel1, channel2, channelService);

        System.out.println("\nBasicMessageService 테스트");
        messageServiceTest(message1, message2, messageService);
    }

    private static void testBasicJCFService(User user1, User user2, Channel channel1, Channel channel2,
        Message message1, Message message2) {
        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, userService, channelService);

        System.out.println("BasicUserService 테스트");
        userServiceTest(user1, user2, userService);

        System.out.println("\nBasicChannelService 테스트");
        channelServiceTest(channel1, channel2, channelService);

        System.out.println("\nBasicMessageService 테스트");
        messageServiceTest(message1, message2, messageService);
    }

    private static void testFileService(User user1, User user2, Channel channel1, Channel channel2,
        Message message1, Message message2) {
        System.out.println("FileUserService 테스트");
        UserService userService = new FileUserService("data/users.ser");
        userServiceTest(user1, user2, userService);

        System.out.println("\nFileChannelService 테스트");
        ChannelService channelService = new FileChannelService("data/channels.ser");
        channelServiceTest(channel1, channel2, channelService);

        System.out.println("\nFileMessageService 테스트");
        MessageService messageService = new FileMessageService("data/messages.ser", userService, channelService);
        messageServiceTest(message1, message2, messageService);
    }

    private static void testJCFService(User user1, User user2, Channel channel1, Channel channel2,
        Message message1, Message message2) {
        System.out.println("JCFUserService 테스트");
        UserService userService = new JCFUserService();
        userServiceTest(user1, user2, userService);

        System.out.println("\nJCFChannelService 테스트");
        ChannelService channelService = new JCFChannelService();
        channelServiceTest(channel1, channel2, channelService);

        System.out.println("\nJCFMessageService 테스트");
        MessageService messageService = new JCFMessageService(userService, channelService);
        messageServiceTest(message1, message2, messageService);
    }

    private static void messageServiceTest(Message message1, Message message2,
        MessageService messageFileService) {
        Message savedMessage1 = messageFileService.create(message1);
        Message savedMessage2 = messageFileService.create(message2);
        System.out.println("메세지1 생성 : " + savedMessage1.getId());
        System.out.println("메세지2 생성 : " + savedMessage2.getId());
        System.out.println("메세지 단건 조회 : " + messageFileService.read(savedMessage1.getId()).orElse(null).getContent());
        System.out.println("메세지 전체 조회 수 : " + messageFileService.readAll().size());

        savedMessage1.updateContent(savedMessage1.getContent() + "수정/");
        messageFileService.update(savedMessage1);
        System.out.println("메세지1 내용 수정 : " + messageFileService.read(savedMessage1.getId()).orElse(null).getContent());

        messageFileService.delete(savedMessage1.getId());
        System.out.println("메세지2 삭제 : " + messageFileService.read(savedMessage1.getId()).orElse(null));
        System.out.println("메세지 전체 조회 수 : " + messageFileService.readAll().size());
    }

    private static void channelServiceTest(Channel channel1, Channel channel2,
        ChannelService channelFileService) {
        Channel savedChannel1 = channelFileService.create(channel1);
        Channel savedChannel2 = channelFileService.create(channel2);

        System.out.println("채널1 생성 : " + savedChannel1.getId());
        System.out.println("채널2 생성 : " + savedChannel2.getId());
        System.out.println("채널 단건 조회 : " + channelFileService.read(savedChannel1.getId()).orElse(null).getName());
        System.out.println("채널 전체 조회 수 : " + channelFileService.readAll().size());

        savedChannel1.updateName(savedChannel1.getName() + "수정/");
        channelFileService.update(savedChannel1);
        System.out.println("채널1 이름 수정 : " + channelFileService.read(savedChannel1.getId()).orElse(null).getName());

        channelFileService.delete(savedChannel2.getId());
        System.out.println("채널2 삭제 : " + channelFileService.read(savedChannel2.getId()).orElse(null));
        System.out.println("채널 전체 조회 수 : " + channelFileService.readAll().size());
    }

    private static void userServiceTest(User user1, User user2, UserService userFileService) {
        User savedUser = userFileService.create(user1);
        User savedUser2 = userFileService.create(user2);
        System.out.println("유저 생성 : " + savedUser.getId());
        System.out.println("유저 단건 조회 : " + userFileService.read(savedUser.getId()).orElse(null).getUserName());
        System.out.println("유저 전체 조회 수 : " + userFileService.readAll().size());

        savedUser.updateUserName(savedUser.getUserName() + "수정/");
        userFileService.update(savedUser);
        System.out.println("유저 이름 수정 : " + userFileService.read(savedUser.getId()).orElse(null).getUserName());

        userFileService.delete(savedUser2.getId());
        System.out.println("유저 삭제 : " + userFileService.read(savedUser2.getId()).orElse(null));
    }

    private static void clearFileData() {
        deleteFile("data/users.ser");
        deleteFile("data/channels.ser");
        deleteFile("data/messages.ser");
    }

    private static void deleteFile(String path) {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            file.delete();
        }
    }

}
