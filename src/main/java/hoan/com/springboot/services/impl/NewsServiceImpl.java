package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.enums.NewsStatus;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.models.entities.NewsEntity;
import hoan.com.springboot.models.entities.NewsUserEntity;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.NewsCreateRequest;
import hoan.com.springboot.repository.NewsRepository;
import hoan.com.springboot.repository.NewsUserRepository;
import hoan.com.springboot.repository.UserRepository;
import hoan.com.springboot.security.utils.SecurityUtils;
import hoan.com.springboot.services.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;

    private final NewsUserRepository newsUserRepository;

    private final UserRepository userRepository;

    public NewsServiceImpl(NewsRepository newsRepository, NewsUserRepository newsUserRepository, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.newsUserRepository = newsUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public NewsEntity create(NewsCreateRequest request) {
        NewsEntity newsEntity = new NewsEntity();

        newsEntity.setStatus(request.getStatus());
        newsEntity.setContent(request.getContent());
        newsEntity.setIsImportant(request.getIsImportant());
        newsEntity.setTitle(request.getTitle());
        newsEntity.setAuthorName(getFullName());

        if (Boolean.TRUE.equals(request.getIsPublic())) {
            newsEntity.setIsPublic(Boolean.TRUE);

            newsRepository.save(newsEntity);
        } else {
            newsEntity.setIsPublic(Boolean.FALSE);

            List<UUID> employeeIds = request.getEmployeeIds()
                    .stream().map(UUID::fromString)
                    .collect(Collectors.toList());

            newsRepository.save(newsEntity);

            saveNewsUser(newsEntity.getId(), employeeIds);
        }

        return newsEntity;
    }

    @Override
    public List<NewsEntity> getAll() {
        return newsRepository.findAll();
    }

    @Override
    public List<NewsEntity> myNews() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        List<NewsUserEntity> newsUserEntities = newsUserRepository
                .findByEmployeeId(UUID.fromString(loginId.get()));

        List<UUID> newsIds = newsUserEntities.stream()
                .map(NewsUserEntity::getNewsId).collect(Collectors.toList());

        return newsRepository.findMyNews(newsIds);
    }

    @Override
    public NewsEntity detail(String id) {
        return newsRepository.findById(UUID.fromString(id)).get();
    }

    @Override
    public boolean delete(String id) {
        Optional<NewsEntity> newsOtp = newsRepository.findById(UUID.fromString(id));

        if (newsOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.NEWS_NOT_EXISTED);
        }

        NewsEntity newsEntity = newsOtp.get();

        newsEntity.setDeleted(Boolean.TRUE);

        newsRepository.save(newsEntity);

        return true;
    }

    @Override
    public NewsEntity update(String id, NewsCreateRequest request) {
        UUID newsUuid = UUID.fromString(id);

        Optional<NewsEntity> newsOtp = newsRepository.findById(newsUuid);

        if (newsOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.NEWS_NOT_EXISTED);
        }

        NewsEntity newsEntity = newsOtp.get();

        if (!NewsStatus.DRAFT.equals(newsEntity.getStatus())) {
            throw new ResponseException(BadRequestError.NEWS_NOT_EXISTED);
        }

        newsEntity.setStatus(request.getStatus());
        newsEntity.setContent(request.getContent());
        newsEntity.setIsImportant(request.getIsImportant());
        newsEntity.setTitle(request.getTitle());
        newsEntity.setAuthorName(getFullName());

        if (Boolean.TRUE.equals(request.getIsPublic())) {
            newsEntity.setIsPublic(Boolean.TRUE);

            newsRepository.save(newsEntity);
        } else {
            newsEntity.setIsPublic(Boolean.FALSE);

            // xóa các bản ghi cũ để lưu mới
            List<NewsUserEntity> newsUserEntities = newsUserRepository.findByNewsId(newsUuid);

            if (Objects.nonNull(newsUserEntities)) {
                newsUserRepository.deleteAll(newsUserEntities);
            }

            // cập nhật mới
            List<UUID> employeeIds = request.getEmployeeIds()
                    .stream().map(UUID::fromString)
                    .collect(Collectors.toList());

            newsRepository.save(newsEntity);

            saveNewsUser(newsUuid, employeeIds);
        }

        return newsEntity;
    }

    private void saveNewsUser(UUID newsId, List<UUID> employeeIds) {
        List<NewsUserEntity> newsUserEntities = new ArrayList<>();

        for (UUID employeeId : employeeIds) {
            NewsUserEntity newsUserEntity = new NewsUserEntity();

            newsUserEntity.setNewsId(newsId);
            newsUserEntity.setEmployeeId(employeeId);

            newsUserEntities.add(newsUserEntity);
        }

        newsUserRepository.saveAll(newsUserEntities);
    }

    private String getFullName() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        UserEntity user = userRepository.findById(UUID.fromString(loginId.get())).get();

        return user.getFullName();
    }
}
