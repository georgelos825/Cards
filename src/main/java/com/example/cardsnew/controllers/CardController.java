package com.example.cardsnew.controllers;


import com.example.cardsnew.entities.Card;
import com.example.cardsnew.services.CardService;
import com.example.cardsnew.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Validated
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private SecurityService securityService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<Card> getCards() {
        return cardService.getAllCards();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER') and @securityService.isCardOwner(authentication, #cardId)")
    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getCardById(@PathVariable  Long cardId) {
        Card card = cardService.getCardById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException(("Card not found with id: " + cardId), null));
        return ResponseEntity.ok().body(card);
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card createdCard = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER') and @securityService.isCardOwner(authentication, #cardId)")
    @PutMapping("/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable  Long cardId, @RequestBody Card card) {
        Card updatedCard = cardService.updateCard(cardId, card);
        return ResponseEntity.ok().body(updatedCard);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MEMBER') and @securityService.isCardOwner(authentication, #cardId)")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable  Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<Card>> searchCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Card> cards = cardService.searchCards(name, color, status, date, page, size);
        return ResponseEntity.ok(cards);
    }
}