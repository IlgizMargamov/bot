package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.deck.Deck;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;

import java.util.ArrayList;
import java.util.Scanner;

public class FoolLogic extends BaseGameLogic {

    ArrayList<Tuple> table;
    CardImpl trump;
    int uncoveredCard;
    boolean deckEmpty;
    boolean trumpGiven;

    public FoolLogic(BasePlayer[] players, Deck deck) {
        super(players, deck);
        giveCardToPlayers(6);
        this.table = new ArrayList<>();
        this.uncoveredCard = 0;
        this.deckEmpty = false;
    }

    public void startGame() {
        int currentTurn = defineFirstPlayer();
        trumpGiven = false;
        while (!defineWinner()) {
            boolean lose = makeSet(currentTurn);
            if (lose) currentTurn += 2;
            else currentTurn++;
            table.clear();
        }
    }


    protected boolean defineWinner() {
        int count = checkEnd();
        if(count == 0){
            System.out.println("Tie!");
            return true;
        }
        else if(count == 1){
            for (BasePlayer player:players) {
                if(player.hand.size() != 0){
                    System.out.println(player.name + "you lose!");
                    return true;
                }
            }
        }
        return false;
    }

    private int checkEnd(){
        int count = 0;
        for (BasePlayer player:players) {
            if(player.hand.size() > 0) count++;
        }
        return count;
    }

    @Override
    protected int defineFirstPlayer() {
        trump = deck.giveNext();
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


    protected boolean makeSet(int firstPlayer) {
        int attackPlayer2 = (firstPlayer + 2) % players.length;
        int defendPlayer = (firstPlayer + 1) % players.length;
        boolean end;
        attackTurn(false, firstPlayer);
        while (true) {
            end = defendTurn(defendPlayer);
            attackTurn(true, firstPlayer);
            if (firstPlayer != attackPlayer2) attackTurn(true, attackPlayer2);
            if (end) break;
            if (uncoveredCard == 0) break;
        }
        if (!deckEmpty || !trumpGiven) giveAllToSix();
        return end;
    }

    private void giveAllToSix() {
        for (BasePlayer player : players) {
            while (player.hand.size() < 6) {
                if (deck.isEmpty()) {
                    deckEmpty = true;
                    player.TakeCard(trump);
                    trumpGiven = true;
                    break;
                }
                player.TakeCard(deck.giveNext());
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
                    if (checkMoveCorrectness(playerCard)) {
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

    @Override
    protected boolean checkMoveCorrectness(CardImpl card) {
        if (table.size() == 0) return false;
        for (Tuple tuple : table) {
            if (tuple.first.CardRank == card.CardRank ||
                    (tuple.second != null && tuple.second.CardRank == card.CardRank)) {
                return false;
            }
        }
        return true;
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
