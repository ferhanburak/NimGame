java    
package project;

import java.util.*;

class Move {
    int heapIndex, coinsRemoved;

    Move() {
        heapIndex = 0;
        coinsRemoved = 0;
    }

    Move(int index, int coins) {
        heapIndex = index;
        coinsRemoved = coins;
    }
}

class Nim {
    ArrayList<Integer> heaps;
    Random rand = new Random(); 
    Stack<Move> moveHistory = new Stack<>(); 

    Nim() {
        heaps = new ArrayList<>();
    }

    Nim(ArrayList<Integer> heaps) {
        this.heaps = new ArrayList<>(heaps);
    }

    int nimSum() {
        int nimsum = heaps.get(0);
        for (int i = 1; i < heaps.size(); ++i)
            nimsum ^= heaps.get(i);
        return nimsum;
    }

    boolean isFinished() {
        return heaps.stream().allMatch(heap -> heap == 0);
    }

    void moveAI() {
        int ns = nimSum();

        if (ns != 0) {
            ArrayList<Integer> nonEmptyHeaps = new ArrayList<>();
            for (int i = 0; i < heaps.size(); ++i) {
                if (heaps.get(i) > (ns ^ heaps.get(i)))
                    nonEmptyHeaps.add(i);
            }

            if (!nonEmptyHeaps.isEmpty()) {
                Move move = new Move();
                move.heapIndex = nonEmptyHeaps.get(rand.nextInt(nonEmptyHeaps.size()));
                move.coinsRemoved = heaps.get(move.heapIndex) - (ns ^ heaps.get(move.heapIndex));
                heaps.set(move.heapIndex, heaps.get(move.heapIndex) - move.coinsRemoved);
                moveHistory.push(move); 
            }
        } else {
            ArrayList<Integer> nonEmptyHeaps = new ArrayList<>();
            for (int i = 0; i < heaps.size(); ++i) {
                if (heaps.get(i) > 0)
                    nonEmptyHeaps.add(i);
            }

            if (!nonEmptyHeaps.isEmpty()) {
                Move move = new Move();
                move.heapIndex = nonEmptyHeaps.get(rand.nextInt(nonEmptyHeaps.size()));
                move.coinsRemoved = rand.nextInt(heaps.get(move.heapIndex)) + 1;
                heaps.set(move.heapIndex, heaps.get(move.heapIndex) - move.coinsRemoved);
                moveHistory.push(move); 
            }
        }
    }

    void moveUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your turn:");

        int index;
        do {
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter the pile index again: ");
                scanner.next();
            }
            index = scanner.nextInt();
            if (index < 0 || index >= heaps.size() || heaps.get(index) == 0) {
                System.out.print("Invalid input. Enter a valid pile index: ");
            }
        } while (index < 0 || index >= heaps.size() || heaps.get(index) == 0);

        int coins;
        do {
            System.out.print("Enter the number of coins to remove: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter the number of coins again: ");
                scanner.next();
            }
            coins = scanner.nextInt();
        } while (coins > heaps.get(index) || coins <= 0);

        Move move = new Move();
        move.heapIndex = index;
        move.coinsRemoved = coins;
        heaps.set(index, heaps.get(index) - coins);
        moveHistory.push(move); 
    }

    void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            heaps.set(lastMove.heapIndex, heaps.get(lastMove.heapIndex) + lastMove.coinsRemoved);
            System.out.println("Last move undone. " + lastMove.coinsRemoved + " coins added back to heap at index " + lastMove.heapIndex);
        } else {
            System.out.println("No moves to undo.");
        }
    }

    void printWinner(boolean turn) {
        System.out.println("\n" + (turn ? "Congratulations, You won!" : "You lost, AI won!"));
    }

    void print() {
        System.out.print("Remaining Coins: ");
        heaps.forEach(heap -> System.out.print(heap + " "));
        System.out.println("\n");
    }

    void nim(boolean turn) {
        Scanner scanner = new Scanner(System.in);

        while (!isFinished()) {
            print();

            if (!turn) { // AI's turn
                System.out.println("AI's turn:");
                moveAI();
            } else { // User's turn
                System.out.println("Your turn:");
                moveUser();
            }

            turn = !turn;
        }

        print();
        printWinner(turn);
    }

    public static void main(String[] args) {
        ArrayList<Integer> heaps = new ArrayList<>(Arrays.asList(3, 4, 5, 3));

        Nim game = new Nim(heaps);
        System.out.println("Welcome to the Nim game where you will play against an AI!");
        System.out.println("Each player must select a heap by its index and select the number of coins to remove.");
        System.out.println("Players must remove at least 1 coin until there are no coins left to remove.");
        System.out.println("Game begins:\n");

        game.nim(true); // If the parameter is true, it's the user's turn; if false, it's the AI's turn

        // Example of using undoLastMove:
        game.undoLastMove();
    }
}
