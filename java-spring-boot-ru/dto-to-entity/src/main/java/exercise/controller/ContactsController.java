package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO create(ContactCreateDTO ccDTO){
        var contract = new Contact();

        contract.setPhone(ccDTO.getPhone());
        contract.setFirstName(ccDTO.getFirstName());
        contract.setLastName(ccDTO.getLastName());

        contactRepository.save(contract);

        var contractDTO = new ContactDTO();

        contractDTO.setId(contract.getId());
        contractDTO.setPhone(contract.getPhone());
        contractDTO.setFirstName(contract.getFirstName());
        contractDTO.setLastName(contract.getLastName());
        contractDTO.setCreatedAt(contract.getCreatedAt());
        contractDTO.setUpdatedAt(contract.getUpdatedAt());

        return contractDTO;
    }
    // END
}
