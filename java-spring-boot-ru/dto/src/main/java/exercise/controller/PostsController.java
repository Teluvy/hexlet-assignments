package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import exercise.model.Post;
import exercise.model.Comment;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/{id}")
    public PostDTO showPost(@PathVariable Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        var dtoPost = toPostDTO(post);
        return dtoPost;
    }

    @GetMapping("")
    public List<PostDTO> show(){
        List<Post> posts = postRepository.findAll();

        var dtoPosts = posts.stream().map(this::toPostDTO).toList();

        return dtoPosts;
    }

    private PostDTO toPostDTO(Post post){
        var postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setBody(post.getBody());
        var comments = commentRepository.findByPostId(post.getId());
        var commentsInDTO = comments.stream().map(this::toCommentDTO).toList();
        postDTO.setComments(commentsInDTO);

        return postDTO;
    }

    private CommentDTO toCommentDTO(Comment comment){
        var dtoComment = new CommentDTO();

        dtoComment.setId(comment.getId());
        dtoComment.setBody(comment.getBody());
        return dtoComment;
    }
}
// END
