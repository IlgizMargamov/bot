package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.deck.Deck;
import com.common.gamelogic.BaseGameLogic;

import java.util.ArrayList;
import java.util.Scanner;

public class FoolLogic extends BaseGameLogic {

    Deck deck;
    FoolPlayer[] players;
    ArrayList<Tuple> table;
    CardImpl trump;
    int uncoveredCard;
    boolean deckEmpty;

    public FoolLogic(FoolPlayer[] players, Deck deck) {
        this.players = players;
        this.deck = deck;
        giveCardToPlayers();
        table = new ArrayList<>();
        uncoveredCard = 0;
        deckEmpty = false;
    }

    public void startGame() {
        int currentTurn = chooseFirst();
        while (true) {
            boolean lose = makeSet(currentTurn);
            if (lose) {
                currentTurn += 2;
                continue;
            }
            currentTurn++;
            int count = checkEnd();
            if(count == 0){
                System.out.println("Tie!");
                break;
            }
            else if(count == 1){
                for (FoolPlayer player:players) {
                    if(player.hand.size() != 0){
                        System.out.println(player.name + "you lose!");
                    }
                }
            }
        }
    }

    private int checkEnd(){
        int count = 0;
        for (FoolPlayer player:players) {
            if(player.hand.size() > 0) count++;
        }
        return count;
    }

    private int chooseFirst() {
        trump = deck.GiveNext();
        CardImpl minCard = new CardImpl(trump.CardSuit, Rank.ACE);
        int firstPlayer = 0;
        for (int i = 0; i < players.length; i++) {
            for (CardImpl card : players[i].hand) {
                if (card.CardSuit == trump.CardSuit && card.CardRank.ordinal() < minCard.CardRank.ordinal()) {
                    minCard = card;
                    firstPlayer = i;
                }
            }
        }
        return firstPlayer;
    }

    private void giveCardToPlayers() {
        for (FoolPlayer player : players) {
            player.TakeHand(createHand(6));
        }
    }

    private ArrayList<CardImpl> createHand(int count) {
        ArrayList<CardImpl> hand = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            hand.add(deck.GiveNext());
        }
        return hand;
    }

    private boolean makeSet(int firstPlayer) {
        int attackPlayer1 = firstPlayer;
        int attackPlayer2 = (firstPlayer + 2) % players.length;
        int defendPlayer = firstPlayer + 1;
        boolean end;
        attackTurn(false, attackPlayer1);
        while (true) {
            firstPlayer++;
            end = defendTurn(defendPlayer);
            if (end) break;
            attackTurn(true, attackPlayer1);
            if (attackPlayer1 != attackPlayer2) attackTurn(true, attackPlayer2);
            if (uncoveredCard == 0) break;
        }
        if (!deckEmpty) giveAllToSix();
        return end;
    }

    private void giveAllToSix() {
        for (FoolPlayer player : players) {
            while (player.hand.size() < 6) {
                if (deck.isEmpty()) {
                    deckEmpty = true;
                    player.TakeCard(trump);
                    break;
                }
                player.TakeCard(deck.GiveNext());
            }
        }
    }

    private void attackTurn(boolean possiblePass, int currentPlayer) {
        System.out.printf("Player %s make your turn(type number of command)\n", players[currentPlayer].name);
        while (true) {
            System.out.println("""
                    Type of turn:
                    1. What on my hand?
                    2. What on the table?
                    3. What is trump?
                    4. Throw this card.
                    5. Pass.""");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> players[currentPlayer].ShowHand();
                case "2" -> {
                    if (table.size() == 0) {
                        System.out.println("Table is empty");
                        continue;
                    }
                    for (Tuple card : table) {
                        System.out.println(card.toString());
                    }
                }
                case "3" -> System.out.println(trump.cardSuitAndRank());
                case "4" -> {
                    System.out.println("What card you want to throw?");
                    players[currentPlayer].ShowHand();
                    System.out.println("0. Back.");
                    int numberOfCardOnHand = Integer.parseInt(scanner.nextLine()) - 1;
                    if (numberOfCardOnHand == -1) continue;
                    CardImpl playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
                    if (!isPossibleTurn(playerCard)) {
                        System.out.println("Try another card");
                        continue;
                    }
                    table.add(new Tuple(playerCard));
                    players[currentPlayer].RemoveCard(numberOfCardOnHand);
                    possiblePass = true;
                    uncoveredCard++;
                    if (table.size() == 6) {
                        System.out.println("Table is full!");
                    }
                    System.out.println("Is it all? y/n");
                    String answer = scanner.nextLine();
                    if (answer.equals("y")) {
                        return;
                    }
                }
                case "5" -> {
                    if (possiblePass) return;
                    System.out.println("It's only start of set!");
                }
            }
        }
    }

    private boolean defendTurn(int currentPlayer) {
        System.out.printf("Player %s make your turn(type number of command)\n", players[currentPlayer].name);
        while (true) {
            System.out.println("""
                    Type of turn:
                    1. What on my hand?
                    2. What on the table?
                    3. What is trump?
                    4. Throw this card.
                    5. Pass""");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> players[currentPlayer].ShowHand();
                case "2" -> {
                    for (Tuple card : table) {
                        System.out.println(card.toString());
                    }
                }
                case "3" -> System.out.println(trump.cardSuitAndRank());
                case "4" -> {
                    System.out.println("What card you want to throw?");
                    players[currentPlayer].ShowHand();
                    System.out.println("0. Back.");
                    int numberOfCardOnHand = Integer.parseInt(scanner.nextLine()) - 1;
                    if (numberOfCardOnHand == -1) continue;
                    CardImpl playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
                    System.out.println("Where you want to throw it?");
                    for (int i = 0; i < table.size(); i++) {
                        if (table.get(i).second != null) continue;
                        System.out.println(i + 1 + ". " + table.get(i).toString());
                    }
                    int numberOfCardOnTable = Integer.parseInt(scanner.nextLine()) - 1;
                    table.get(numberOfCardOnTable).Cover(playerCard);
                    if (table.get(numberOfCardOnTable).second == null) continue;
                    players[currentPlayer].RemoveCard(numberOfCardOnHand);
                    uncoveredCard--;
                    if (uncoveredCard == 0) return false;
                }
                case "5" -> {
                    for (Tuple card : table) {
                        players[currentPlayer].TakeCard(card.first);
                        if (card.second != null)
                            players[currentPlayer].TakeCard(card.second);
                    }
                    return true;
                }
            }
        }
    }

    private boolean isPossibleTurn(CardImpl card) {
        if (table.size() == 0) return true;
        for (Tuple tuple : table) {
            if (tuple.first.CardRank == card.CardRank ||
                    (tuple.second != null && tuple.second.CardRank == card.CardRank)) {
                return true;
            }
        }
        return false;
    }

    private class Tuple {
        public CardImpl first;
        public CardImpl second;

        public Tuple(CardImpl first) {
            this.first = first;
        }

        public void Cover(CardImpl second) {
            if (first.CardSuit == trump.CardSuit) {
                if (second.CardSuit == trump.CardSuit && first.CardRank.ordinal() < second.CardRank.ordinal()) {
                    this.second = second;
                }
            } else if (second.CardSuit == trump.CardSuit) {
                this.second = second;
            } else if (first.CardRank.ordinal() < second.CardRank.ordinal() && first.CardSuit == second.CardSuit) {
                this.second = second;
            } else {
                System.out.println("Not possible turn!");
            }
        }


        public String toString() {
            if (second == null) return first.cardSuitAndRank() + " \\ " + "Nothing";
            return first.cardSuitAndRank() + " \\ " + second.cardSuitAndRank();
        }
    }
}
