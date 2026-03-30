package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        User user1 = new User("user1@gmail.com", "유저1", "유저일");

        Channel channel1 = new Channel("채널1", true, "채널1입니다.", "자유롭게 사용 가능");
        Channel channel2 = new Channel("채널2", false, "채널2입니다.", "비밀 채널");

        Message message1 = new Message(channel1, user1, "채널1 여러분 반갑습니다.");
        Message message2 = new Message(channel1, user1, "채널1 여러분 반갑습니다2.");

        System.out.println("/----------------UserService 테스트----------------/");
        UserService userFileService = new FileUserService("data/users.ser");

        User savedUser = userFileService.create(user1);
        System.out.println("유저 생성 : " + savedUser.getId());
        System.out.println("유저 단건 조회 : " + userFileService.read(savedUser.getId()).orElse(null).getUserName());
        System.out.println("유저 전체 조회 수 : " + userFileService.readAll().size());

        savedUser.updateUserName("이름수정");
        userFileService.update(savedUser);
        System.out.println("유저 이름 수정 : " + userFileService.read(savedUser.getId()).orElse(null).getUserName());

        userFileService.delete(savedUser.getId());
        System.out.println("유저 삭제 : " + userFileService.read(savedUser.getId()).orElse(null));


        System.out.println("\n/----------------ChannelService 테스트----------------/");
        ChannelService channelFileService = new FileChannelService("data/channels.ser");

        Channel savedChannel1 = channelFileService.create(channel1);
        Channel savedChannel2 = channelFileService.create(channel2);
        System.out.println("채널1 생성 : " + savedChannel1.getId());
        System.out.println("채널2 생성 : " + savedChannel2.getId());
        System.out.println("채널 단건 조회 : " + channelFileService.read(savedChannel1.getId()).orElse(null).getName());
        System.out.println("채널 전체 조회 수 : " + channelFileService.readAll().size());

        savedChannel1.updateName("채널1수정");
        channelFileService.update(savedChannel1);
        System.out.println("채널1 이름 수정 : " + channelFileService.read(savedChannel1.getId()).orElse(null).getName());

        channelFileService.delete(savedChannel1.getId());
        System.out.println("채널1 삭제 : " + channelFileService.read(savedChannel1.getId()).orElse(null));
        System.out.println("채널 전체 조회 수 : " + channelFileService.readAll().size());


        //JCFMessageService 테스트

        System.out.println("\n/----------------MessageService 테스트----------------/");
        MessageService messageFileService = new FileMessageService("data/messages.ser");

        Message savedMessage1 = messageFileService.create(message1);
        Message savedMessage2 = messageFileService.create(message2);
        System.out.println("메세지1 생성 : " + savedMessage1.getId());
        System.out.println("메세지2 생성 : " + savedMessage2.getId());
        System.out.println("메세지 단건 조회 : " + messageFileService.read(savedMessage1.getId()).orElse(null).getContent());
        System.out.println("메세지 전체 조회 수 : " + messageFileService.readAll().size());

        savedMessage1.updateContent("메세지1 내용수정");
        messageFileService.update(savedMessage1);
        System.out.println("메세지1 이름 수정 : " + messageFileService.read(savedMessage1.getId()).orElse(null).getContent());

        messageFileService.delete(savedMessage1.getId());
        System.out.println("메세지1 삭제 : " + messageFileService.read(savedMessage1.getId()).orElse(null));
        System.out.println("메세지 전체 조회 수 : " + messageFileService.readAll().size());

    }

}
