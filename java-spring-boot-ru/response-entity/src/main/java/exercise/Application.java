package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    private static  List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public ResponseEntity<List<Post>>  index(){
        var data = posts.stream().toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(data);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post>  indexById(@PathVariable String id){
        Optional<Post> mbPost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();

        if (mbPost.isPresent()){
            return ResponseEntity.status(HttpStatus.valueOf(200))
                    .body(mbPost.get());
        }
        return ResponseEntity.status(HttpStatus.valueOf(404)).build();
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post){
        posts.add(post);
        return ResponseEntity.status(201).body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post body){
        Optional<Post> mbPost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();

        if (mbPost.isPresent()){
            mbPost.get().setId(body.getId());
            mbPost.get().setBody(body.getBody());
            mbPost.get().setTitle(body.getTitle());
            return ResponseEntity.ok().body(mbPost.get());
        }
        return ResponseEntity.status(404).build();
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
