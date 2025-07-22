package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShow() throws Exception{
        var task = createFakeTask();
        taskRepository.save(task);

        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreate() throws Exception {
        var task = createFakeTask();

        var result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Task createdTask = om.readValue(responseContent, Task.class);

        Optional<Task> taskBD = taskRepository.findById(createdTask.getId());
        assertThat(taskBD).isPresent();
        assertThat(taskBD.get().getTitle()).isEqualTo(task.getTitle());
        assertThat(taskBD.get().getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void testUpdate() throws Exception {
        var task = taskRepository.save(createFakeTask());

        var updates = new HashMap<String, String>();
        updates.put("title", "updatedTitle");
        updates.put("description", "updatedDescription");

        var result = mockMvc.perform(put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Optional<Task> updatedTask = taskRepository.findById(task.getId());
        assertThat(updatedTask).isPresent();
        assertThat(updatedTask.get().getTitle()).isEqualTo("updatedTitle");
        assertThat(updatedTask.get().getDescription()).isEqualTo("updatedDescription");
    }


    @Test
    public void testDelete() throws Exception {
        var task = taskRepository.save(createFakeTask());

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());

        Optional<Task> deletedTask = taskRepository.findById(task.getId());
        assertThat(deletedTask).isEmpty();
    }


    public Task createFakeTask(){
        var task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().word())
                .create();
        return task;
    }
    // END
}
