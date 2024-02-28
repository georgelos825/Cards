package com.example.cardsnew.services;

import com.example.cardsnew.entities.Card;
import com.example.cardsnew.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class  SecurityService {

    @Autowired
    private CardRepository cardRepository;

    public boolean isCardOwner(Authentication authentication, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        String email = authentication.getName();
        return card.getUser().getEmail().equals(email);
    }
}