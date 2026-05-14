import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class main {
    public static void main(String args[]) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        printWelcome();

        while (true) {
            System.out.print("command> ");
            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split(" ");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "help":
                    printHelp();
                    break;
                case "list":
                    printPieceList(board);
                    break;
                case "clear":
                    board.clear();
                    System.out.println("Cleared all pieces.");
                    break;
                case "render":
                    renderBoard(board, tokens);
                    break;
                case "exit":
                case "quit":
                    System.out.println("Goodbye.");
                    scanner.close();
                    return;
                case "add":
                    addPieceFromTokens(board, tokens, 1);
                    break;
                default:
                    addPieceFromTokens(board, tokens, 0);
                    break;
            }
        }

        scanner.close();
    }

    private static void printWelcome() {
        System.out.println("Chess Pattern Generator");
        System.out.println("Type 'help' to see available commands.");
        System.out.println();
    }

    private static void printHelp() {
        System.out.println("Commands:");
        System.out.println("  add <moves> [#RRGGBB]   Add a piece by move offsets and optional color.");
        System.out.println("  list                    Show all added pieces.");
        System.out.println("  clear                   Remove all pieces.");
        System.out.println("  render <size> <file>    Generate a PNG pattern from current pieces.");
        System.out.println("  help                    Show this help message.");
        System.out.println("  exit | quit             Exit the program.");
        System.out.println();
        System.out.println("Piece format examples:");
        System.out.println("  add 1,2 2,1 -1,-2 -2,-1 -1,2 -2,1 1,-2 2,-1 #000000");
        System.out.println("  add 0,1 1,0 0,-1 -1,0 #FF0000");
        System.out.println("  1,2 2,1 -1,-2 -2,-1 -1,2 -2,1 1,-2 2,-1 #000000");
    }

    private static void printPieceList(Board board) {
        String[] summaries = board.get_piece_summaries();
        if (summaries.length == 0) {
            System.out.println("No pieces added yet.");
            return;
        }

        System.out.println("Current pieces:");
        for (int i = 0; i < summaries.length; i++) {
            System.out.printf("  %d: %s%n", i + 1, summaries[i]);
        }
    }

    private static void renderBoard(Board board, String[] tokens) {
        if (tokens.length < 3) {
            System.out.println("Usage: render <size> <output.png>");
            return;
        }

        try {
            int size = Integer.parseInt(tokens[1]);
            if (size <= 0) {
                System.out.println("Size must be a positive number.");
                return;
            }

            String filename = tokens[2];
            if (!filename.toLowerCase().endsWith(".png")) {
                System.out.println("Output filename should end with .png");
                return;
            }

            if (board.get_piece_count() == 0) {
                System.out.println("Add at least one piece before rendering.");
                return;
            }

            int[][] pixels = board.render(size);
            String[] colors = board.get_colors();
            ImageExporter.exportIntArrayToPNG(pixels, colors, filename);
            System.out.println("Rendered pattern to " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid size. Use a number like 100.");
        } catch (IOException e) {
            System.out.println("Could not write the output file: " + e.getMessage());
        }
    }

    private static void addPieceFromTokens(Board board, String[] tokens, int startIndex) {
        if (tokens.length <= startIndex) {
            System.out.println("No piece moves found. Use 'help' for syntax.");
            return;
        }

        String color = getRandomColor();
        List<int[]> moves = new ArrayList<>();

        for (int i = startIndex; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.startsWith("#")) {
                color = token;
            } else {
                String[] moveParts = token.split(",");
                if (moveParts.length != 2) {
                    System.out.println("Invalid move format: " + token);
                    return;
                }

                try {
                    int dx = Integer.parseInt(moveParts[0]);
                    int dy = Integer.parseInt(moveParts[1]);
                    moves.add(new int[] {dx, dy});
                } catch (NumberFormatException e) {
                    System.out.println("Invalid move values: " + token);
                    return;
                }
            }
        }

        if (moves.isEmpty()) {
            System.out.println("A piece needs at least one move.");
            return;
        }

        board.add_piece(moves, color);
        System.out.println("Added piece " + board.get_piece_count() + " with color " + color + ".");
    }

    public static String getRandomColor() {
        final Random random = new Random();
        final String[] letters = "0123456789ABCDEF".split("");
        StringBuilder color = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            color.append(letters[Math.round(random.nextFloat() * 15)]);
        }
        return color.toString();
    }
}
