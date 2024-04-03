package hoan.com.springboot.controllers;

import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.NewsEntity;
import hoan.com.springboot.payload.request.NewsCreateRequest;
import hoan.com.springboot.payload.response.EventResponse;
import hoan.com.springboot.services.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@Tag(name = "News resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Get all news")
    @GetMapping("")
    @PreAuthorize("hasPermission(null, 'news:view')")
    public Response<List<NewsEntity>> getAll() {
        return Response.of(newsService.getAll());
    }

    @Operation(summary = "Get my news")
    @GetMapping("my-news")
    public Response<List<NewsEntity>> getMyNews() {
        return Response.of(newsService.myNews());
    }

    @Operation(summary = "Create news")
    @PostMapping("")
    @PreAuthorize("hasPermission(null, 'news:create')")
    public Response<NewsEntity> create(@RequestBody @Valid NewsCreateRequest request) {
        return Response.of(newsService.create(request));
    }

    @Operation(summary = "Detail news")
    @GetMapping("{id}")
    @PreAuthorize("hasPermission(null, 'news:view')")
    public Response<NewsEntity> detail(@PathVariable String id) {
        return Response.of(newsService.detail(id));
    }

    @Operation(summary = "Update news")
    @PostMapping("{id}")
    @PreAuthorize("hasPermission(null, 'news:update')")
    public Response<NewsEntity> update(@PathVariable String id, @RequestBody @Valid NewsCreateRequest request) {
        return Response.of(newsService.update(id, request));
    }

    @Operation(summary = "Delete news")
    @PostMapping("{id}/delete")
    @PreAuthorize("hasPermission(null, 'news:delete')")
    public Response<Boolean> delete(@PathVariable String id) {
        return Response.of(newsService.delete(id));
    }
}
