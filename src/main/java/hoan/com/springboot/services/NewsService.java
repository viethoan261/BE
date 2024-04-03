package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.NewsEntity;
import hoan.com.springboot.payload.request.NewsCreateRequest;

import java.util.List;
import java.util.UUID;

public interface NewsService {
    NewsEntity create(NewsCreateRequest request);

    List<NewsEntity> getAll();

    List<NewsEntity> myNews();

    NewsEntity detail(String id);

    boolean delete(String id);

    NewsEntity update(String id, NewsCreateRequest request);
}
