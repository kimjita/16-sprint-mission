package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// 실제 file-data-map 폴더에 저장하므로, 테스트가 만든 파일은 끝날 때 지운다
class FileBinaryContentRepositoryTest {
    private FileBinaryContentRepository repository;
    private final List<UUID> savedIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        repository = new FileBinaryContentRepository();
    }

    @AfterEach
    void tearDown() {
        savedIds.forEach(repository::deleteById);
    }

    private BinaryContent saveBinaryContent() {
        byte[] bytes = {1, 2, 3};
        BinaryContent binaryContent = repository.save(new BinaryContent("profile.png", (long) bytes.length, "image/png", bytes));
        savedIds.add(binaryContent.getId());
        return binaryContent;
    }

    @Test
    @DisplayName("저장한 BinaryContent를 id로 조회하면 내용까지 그대로 읽힌다")
    void saveAndFindById() {
        BinaryContent saved = saveBinaryContent();

        Optional<BinaryContent> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getFileName()).isEqualTo(saved.getFileName());
        assertThat(found.get().getContentType()).isEqualTo(saved.getContentType());
        assertThat(found.get().getBytes()).isEqualTo(saved.getBytes());
    }

    @Test
    @DisplayName("존재하지 않는 id로 조회하면 빈 Optional을 반환한다")
    void findByIdNotFound() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    @DisplayName("findAll은 저장한 BinaryContent를 모두 포함한다")
    void findAll() {
        BinaryContent first = saveBinaryContent();
        BinaryContent second = saveBinaryContent();

        assertThat(repository.findAll()).extracting(BinaryContent::getId)
                .contains(first.getId(), second.getId());
    }

    @Test
    @DisplayName("existsById는 저장 여부를 반환한다")
    void existsById() {
        BinaryContent saved = saveBinaryContent();

        assertThat(repository.existsById(saved.getId())).isTrue();
        assertThat(repository.existsById(UUID.randomUUID())).isFalse();
    }

    @Test
    @DisplayName("deleteById로 삭제하면 더 이상 조회되지 않는다")
    void deleteById() {
        BinaryContent saved = saveBinaryContent();

        repository.deleteById(saved.getId());

        assertThat(repository.existsById(saved.getId())).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 id를 삭제해도 예외가 발생하지 않는다")
    void deleteByIdNotFound() {
        assertThatCode(() -> repository.deleteById(UUID.randomUUID()))
                .doesNotThrowAnyException();
    }
}
