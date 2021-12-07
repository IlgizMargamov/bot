package com.games.OneHundred;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;
import com.common.deck.Deck;
import com.common.gamelogic.BaseGameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class OneHundredLogic extends BaseGameLogic {

    private Deck deck;
    private OneHundredPlayer[] players;
    private int currentPlayer = 0;
    private ArrayList<CardImpl> table;
    private boolean gameCondition;
    private CardImpl lastCard;
    HashMap<OneHundredPlayer, Integer> score;
    Scanner scanner;

    public OneHundredLogic(OneHundredPlayer[] players, Deck deck) {
        this.players = players;
        this.deck = deck;
        this.table = new ArrayList<>();
        this.gameCondition = true;
        this.score = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public void StartGame() {
        for (OneHundredPlayer player :
                players) {
            score.put(player, 0);
        }
        while (checkGameCondition()) {
            startSet();
            countPlayersScore();
            if (lastCard.CardRank == Rank.HIDDEN) {
                score.replace(players[(currentPlayer - 1) % players.length],
                        score.get(players[(currentPlayer - 1) % players.length]) - 20);
            }
        }
    }

    private void startSet() {
        for (OneHundredPlayer player : players) {
            player.TakeHand(CreateHand(4));
        }
        table.add(players[currentPlayer].GiveLastCard());
        lastCard = table.get(table.size() - 1);
        MovePlayerOn(1);
        do {
            boolean madeTurn = MakeTurn();
            if (madeTurn) {
                if (lastCard.CardRank == Rank.LADY) {
                    System.out.println("""
                            Choose a next card suit
                            1. Club
                            2. Diamond
                            3. Hearts
                            4. Spades""");
                    String command = scanner.nextLine();
                    switch (command) {
                        case "1" -> lastCard = new CardImpl(Suit.CLUBS, Rank.HIDDEN);
                        case "2" -> lastCard = new CardImpl(Suit.DIAMOND, Rank.HIDDEN);
                        case "3" -> lastCard = new CardImpl(Suit.HEARTS, Rank.HIDDEN);
                        case "4" -> lastCard = new CardImpl(Suit.SPADES, Rank.HIDDEN);
                    }
                    MovePlayerOn(1);
                    continue;
                } else if (lastCard.CardRank == Rank.ACE) {
                    MovePlayerOn(2);
                    continue;
                } else if (lastCard.CardRank == Rank.SEVEN) {
                    NextTakeCard(2);
                    MovePlayerOn(2);
                    continue;
                } else if (lastCard.CardRank == Rank.KING && lastCard.CardSuit == Suit.CLUBS) {
                    NextTakeCard(5);
                    MovePlayerOn(2);
                    continue;
                } else if (lastCard.CardRank == Rank.SIX) {
                    continue;
                }
            }
            MovePlayerOn(1);
        } while (!checkSetCondition());
    }

    private void MovePlayerOn(int count) {
        currentPlayer += count;
        currentPlayer %= players.length;
    }

    private void NextTakeCard(int count) {
        for (int i = 0; i < count; i++) {
            players[(currentPlayer + 1) % players.length].TakeCard(deck.GiveNext());
            if (deck.isEmpty()) deck = new Deck(table);
        }
    }

    private boolean MakeTurn() {
        System.out.printf("Player %s make your turn(type number of command)\n", players[currentPlayer].name);
        boolean take = true;
        while (true) {
            System.out.println("""
                    Type of turn:
                    1. What on my hand?
                    2. What on the table?
                    3. Throw this card.""");
            if (take) System.out.println("4. Take");
            else System.out.println("4. Pass");
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> players[currentPlayer].ShowHand();
                case "2" -> {
                    Rank rank = lastCard.CardRank;
                    if (rank == Rank.HIDDEN) System.out.println(lastCard.CardSuit);
                    else System.out.println(lastCard.CardSuit + " " + rank);
                }
                case "3" -> {
                    boolean endTurn = throwCard();
                    if (endTurn) return true;
                }
                case "4" -> {
                    if (take) {
                        players[currentPlayer].TakeCard(deck.GiveNext());
                        take = false;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private boolean throwCard() {
        System.out.println("What card you want to throw?");
        players[currentPlayer].ShowHand();
        System.out.println("0. Back.");
        CardImpl playerCard;
        int numberOfCardOnHand;
        while (true) {
            numberOfCardOnHand = Integer.parseInt(scanner.nextLine()) - 1;
            if (numberOfCardOnHand == -1) return false;
            playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
            if (!possibleTurn(playerCard)) {
                System.out.println("Try another card");
                continue;
            }
            break;
        }
        table.add(playerCard);
        lastCard = playerCard;
        players[currentPlayer].RemoveCard(numberOfCardOnHand);
        return true;
    }

    private void countPlayersScore() {
        for (OneHundredPlayer player : players) {
            CardImpl card = player.GiveLastCard();
            switch (card.CardRank) {
                case JACK -> score.replace(player, score.get(player) + 2);
                case LADY -> score.replace(player, score.get(player) + 3);
                case KING -> score.replace(player, score.get(player) + 4);
                case ACE -> score.replace(player, score.get(player) + 11);
                default -> score.replace(player, score.get(player) + card.CardRank.ordinal());
            }
        }
    }

    private boolean possibleTurn(CardImpl card) {
        CardImpl cardOnTable = table.get(table.size() - 1);
        return (card.CardRank == cardOnTable.CardRank ||
                card.CardSuit == cardOnTable.CardSuit);
    }

    private boolean checkSetCondition() {
        return players[currentPlayer].hand.size() == 0;
    }

    private boolean checkGameCondition() {
        int count = 0;
        for (OneHundredPlayer player : players) {
            if (score.get(player) < 101) {
                count += 1;
            }
        }
        return count >= 1;
    }

    private ArrayList<CardImpl> CreateHand(int count) {
        ArrayList<CardImpl> hand = new ArrayList<CardImpl>();
        for (int i = 0; i < count; i++) {
            hand.add(deck.GiveNext());
        }
        return hand;
    }
}
