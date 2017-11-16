/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author huynguyen
 */
public class DeckOfCards implements Comparable<DeckOfCards> {
    private final List<Card> cards;

    public DeckOfCards() {
        this.cards = new ArrayList<>();
        
        for(int cardVal = 0; cardVal < 9; ++cardVal) {
            for(int cardTypeIdx = 0; cardTypeIdx < 4; ++cardTypeIdx) {
                this.cards.add(new Card(cardVal+1, Common.CardType.values()[cardTypeIdx]));
            }
        }
        
        this.shuffle();
    }
    
    private DeckOfCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }
    
    
    public final void shuffle() {
        Collections.shuffle(this.cards);
//        return this;
    }
    
    public DeckOfCards take(int amount) {
        int _amount = amount < this.cards.size() ? amount : this.cards.size();
        List<Card> tempCards = new ArrayList<>();
        for(int i = 0; i < _amount; ++i) {
            tempCards.add(this.cards.remove(0));
        }
        return new DeckOfCards(tempCards);
    }
    
    public Card getMax() {
        return this.cards
                .stream()
                .max(Card::compareTo)
                .get();
    }
    
    public int sum() {
        int sum = this.cards
                .stream()
                .mapToInt(Card::getValue)
                .sum();
        if(sum <= 10) {
            return sum;
        } else if (sum == 20) {
            return 10;
        } else {
            return sum % 10;
        }
    }

    @Override
    public int compareTo(DeckOfCards o) {
        int sumComparedResult = new Integer(this.sum()).compareTo(o.sum());
        if(sumComparedResult == 0) {
            return this.getMax().compareTo(o.getMax());
        } else {
            return sumComparedResult;
        }
        
    }
    
    
}
