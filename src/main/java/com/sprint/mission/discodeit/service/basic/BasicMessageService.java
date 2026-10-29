package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(CreateMessageRequest request) {
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("해당 id의 채널이 없음");
        }

        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("해당 id의 유저가 없음");
        }

        List<UUID> attachmentIds = new ArrayList<>();
        if (request.files() != null) {
            attachmentIds = request.files()
                    .stream()
                    .map(this::createBinaryContent)
                    .map(binaryContentRepository::save)
                    .map(BinaryContent::getId)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        Message message = new Message(request.content(), request.channelId(),
                request.authorId(), attachmentIds);
        messageRepository.save(message);

        return MessageResponse.from(message);
    }

    @Override
    public MessageResponse find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        return MessageResponse.from(message);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("해당 id의 채널이 없음");
        }

        return messageRepository.findAll().stream()
                .filter(message -> channelId.equals(message.getChannelId()))
                .map(MessageResponse::from)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public MessageResponse update(UUID messageId, UpdateMessageRequest request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 id를 가진 메시지가 없음"));

        message.update(request.content());
        messageRepository.save(message);
        return MessageResponse.from(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 id를 가진 메시지가 없음"));

        message.getAttachmentIds().forEach(binaryContentRepository::deleteById);
        messageRepository.deleteById(messageId);
    }

    private BinaryContent createBinaryContent(BinaryContentRequest request) {
        String filename = request.filename();
        Long size = request.size();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();

        BinaryContent binaryContent = new BinaryContent(filename, size, contentType, bytes);
        return binaryContent;
    }
}
