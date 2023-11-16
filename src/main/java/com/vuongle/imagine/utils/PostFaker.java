package com.vuongle.imagine.utils;

import com.github.javafaker.Faker;
import com.vuongle.imagine.dto.blog.CategoryDto;
import com.vuongle.imagine.models.Category;
import com.vuongle.imagine.models.Post;
import com.vuongle.imagine.services.core.blog.command.CreatePostCommand;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PostFaker {

    static List<CategoryDto> categories = List.of(
            new CategoryDto(new ObjectId("654690401766123ac170252a"), "test1", null),
            new CategoryDto(new ObjectId("6547aa3d42face30eaa05b73"), "test3", null),
            new CategoryDto(new ObjectId("654ba40f9090ce6a9f1f5ded"), "test2", null)
    );


    public static List<CreatePostCommand> fakePosts(int num) {

        List<CreatePostCommand> posts = new ArrayList<>();

        IntStream.range(0, num).forEach(i -> {
            CreatePostCommand post = fakePost();
            posts.add(post);
        });

        return posts;
    }

    private static CreatePostCommand fakePost() {
        Faker faker = new Faker();

        String title = faker.lorem().paragraph(2);
        String description = faker.lorem().paragraph(5);
        String content = faker.lorem().paragraph(20);

        CreatePostCommand post = new CreatePostCommand();
        post.setTitle(title);
        post.setDescription(description);
        post.setContent(content);

        // set category
        post.setCategoryId(fakeCategory().getId());

        return post;
    }

    private static CategoryDto fakeCategory() {
        Random random = new Random();

        int index = random.nextInt(categories.size());

        return categories.get(index);
    }
}
