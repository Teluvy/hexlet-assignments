package exercise.controller;

import exercise.model.Post;
import exercise.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    CommentRepository commentRepository;

    @GetMapping(path = "")
    public List<Comment> getComments(){
        return commentRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Comment getComment(@PathVariable long id){
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " is not found"));
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment postComment(@RequestBody Comment comment){
        return commentRepository.save(comment);
    }

    @PutMapping(path = "/{id}")
    public Comment putComment(@PathVariable long id, @RequestBody Comment updateComment){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " is not found"));
        comment.setBody(updateComment.getBody());
        comment.setPostId(updateComment.getPostId());
        return commentRepository.save(comment);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteComment(@PathVariable long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " is not found"));
        commentRepository.deleteById(id);
    }
}
// END
