package exercise.controller.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api/users")
public class PostsController {
    @GetMapping("/{id}/posts")
    public List<Post> index(@PathVariable int id){
        List<Post> mbPosts = Data.getPosts().stream().filter(p -> p.getUserId() == id).toList();
        return mbPosts;
    }

    @PostMapping("/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> create(@PathVariable int id, @RequestBody Post post){
        post.setUserId(id);
        Data.getPosts().add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}
// END
