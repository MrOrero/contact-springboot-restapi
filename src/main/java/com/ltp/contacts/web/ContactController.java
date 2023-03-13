package com.ltp.contacts.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ltp.contacts.ErrorResponse;
import com.ltp.contacts.pojo.Contact;
import com.ltp.contacts.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;

@Tag(name = "Contact Controller", description = "Create and retrieve contacts")
@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping(value = "/contact/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful retrieval of contacts", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Contact.class))))
    @Operation(summary = "Retrieves contacts", description = "Provides a list of all contacts")
    public ResponseEntity<List<Contact>> getContacts() {
        List<Contact> contacts = contactService.getContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Contact doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Successful retrieval of contact", content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @Operation(summary = "Get contact by Id", description = "Returns a contact based on an ID")
    public ResponseEntity<Contact> getContact(@PathVariable String id) {
        Contact contact = contactService.getContactById(id);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation of contact"),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/contact", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a contact", description = "Creates a contact from the provided payload")
    public ResponseEntity<HttpStatus> createContact(@Valid @RequestBody Contact contact) {
        contactService.saveContact(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Contact doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "Successful Updating of contact", content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @Operation(summary = "Update a contact", description = "Updates an existing contact")
    public ResponseEntity<Contact> updateContact(@PathVariable String id, @Valid @RequestBody Contact contact) {
        contactService.updateContact(id, contact);
        return new ResponseEntity<Contact>(contactService.getContactById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Contact doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "204", description = "Successful deletion of contact", content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @Operation(summary = "Delete a contact", description = "Deletes an existing contact")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}