package com.example.cardsnew.services;


import com.example.cardsnew.entities.Card;
import com.example.cardsnew.enums.Status;
import com.example.cardsnew.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;


    public Card createCard(Card card) {
        card.setStatus(Status.TO_DO);
        card.setCreatedAt(LocalDateTime.now());
        return cardRepository.save(card);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardById(Long cardId) {
        return cardRepository.findById(cardId);
    }

    public Card updateCard(Long cardId, Card updatedCard) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isPresent()) {
            Card existingCard = optionalCard.get();
            existingCard.setName(updatedCard.getName());
            existingCard.setDescription(updatedCard.getDescription());
            existingCard.setColor(updatedCard.getColor());
            existingCard.setStatus(updatedCard.getStatus());
            return cardRepository.save(existingCard);
        } else {
            throw new RuntimeException("Card not found with id: " + cardId);
        }
    }

    public void deleteCard(Long cardId) {
        if (cardRepository.existsById(cardId)) {
            cardRepository.deleteById(cardId);
        } else {
            throw new RuntimeException("Card not found with id: " + cardId);
        }
    }

    public List<Card> searchCards(String name, String color, String status, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Specification<Card> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (color != null && !color.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("color"), color));
        }
        if (status != null && !status.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }
        if (date != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("creationDate"), date));
        }

        Page<Card> cardPage = cardRepository.findAll(specification, pageable);
        return cardPage.getContent();
    }
}