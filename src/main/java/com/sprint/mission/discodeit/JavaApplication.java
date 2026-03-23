package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
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

        //JCFUserService 테스트
        UserService userService = new JCFUserService();
        User userNull = userService.read(user1.getId()).orElse(null);
        System.out.println("유저1 등록 전(null) : " + userNull);

        User savedUser = userService.create(user1);
        System.out.println("유저1 등록 후(유저1) : " + savedUser.getUserName());

        List<User> users = userService.readAll();
        System.out.println("유저 리스트 사이즈(1) : " + users.size());

        user1.updateUserName("1유저");
        savedUser = userService.update(user1);
        System.out.println("유저 변경 이름(1유저) : " + savedUser.getUserName());

        userService.delete(user1.getId());
        System.out.println("유저 삭제됨(null) : " + userService.read(user1.getId()).orElse(null) + "\n");

        //JCFChannelService 테스트
        ChannelService channelService = new JCFChannelService();
        List<Channel> channels = channelService.readAll();
        System.out.println("채널들 등록 전(true) : " + channels.isEmpty());

        Channel savedChannel1 = channelService.create(channel1);
        System.out.println("채널1 등록 후(채널1) : " + savedChannel1.getName());
        Channel savedChannel2 = channelService.create(channel2);
        System.out.println("채널2 등록 후(채널2) : " + savedChannel2.getName());

        channels = channelService.readAll();
        System.out.println("채널들 등록 후(2) : " + channels.size());

        channel1.updateName("1채널");
        channelService.update(channel1);
        savedChannel1 = channelService.read(channel1.getId()).orElse(null);
        System.out.println("채널1 -> 1채널로 이름 수정 : " + savedChannel1.getName());

        channelService.delete(channel1.getId());
        savedChannel1 = channelService.read(channel1.getId()).orElse(null);
        System.out.println("채널1 삭제 후(null) : " + savedChannel1 + "\n");

        //JCFMessageService 테스트
        MessageService messageService = new JCFMessageService();
        List<Message> messages = messageService.readAll();
        System.out.println("메세지들 등록 전(true) : " + messages.isEmpty());

        Message savedMessage1 = messageService.create(message1);
        System.out.println("메세지1 등록 후 : " + savedMessage1.getContent());
        Message savedMessage2 = messageService.create(message2);
        System.out.println("메세지2 등록 후 : " + savedMessage2.getContent());

        messages = messageService.readAll();
        System.out.println("메세지들 등록 후(2) : " + messages.size());

        message1.updateContent("메세지1 수정함");
        channelService.update(channel1);
        savedMessage1 = messageService.read(message1.getId()).orElse(null);
        System.out.println("메세지 내용 수정 : " + savedMessage1.getContent());

        messageService.delete(message1.getId());
        savedMessage1 = messageService.read(message1.getId()).orElse(null);
        System.out.println("메세지1 삭제 후(null) : " + savedMessage1 + "\n");

    }

}
