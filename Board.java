import java.util.ArrayList;
import java.util.List;

public class Board {


    private int turn = 0;
    private int[][] board;
    private List<Piece> pieces;
    
    public Board() {
        this.pieces = new ArrayList<>();
    }

    public void add_piece(List<int[]> moves, String color) {
        this.pieces.add(new Piece(moves, color, this.pieces.size()));
    }

    public String[] get_colors() {
        String[] colors = new String[this.pieces.size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = this.pieces.get(i).get_color();
        }
        return colors;
    }

    public int get_piece_count() {
        return this.pieces.size();
    }

    public String[] get_piece_summaries() {
        String[] summaries = new String[this.pieces.size()];
        for (int i = 0; i < this.pieces.size(); i++) {
            Piece piece = this.pieces.get(i);
            StringBuilder moves = new StringBuilder();
            for (int[] move : piece.get_moves()) {
                if (moves.length() > 0) {
                    moves.append(" ");
                }
                moves.append(move[0]).append(",").append(move[1]);
            }
            summaries[i] = String.format("moves=%s color=%s", moves.toString(), piece.get_color());
        }
        return summaries;
    }

    public void clear() {
        this.pieces.clear();
        this.turn = 0;
    }

    private void place_piece() {
        int[] loc = this.pieces.get(turn).get_location();
        this.board[loc[0]][loc[1]] = this.turn;
        this.turn++;
        if(this.turn >= this.pieces.size()) {
            this.turn = 0;
        }
    }

    private void init_location() {
        int len = this.board[0].length;
        if(len % 2 != 0) len++;
        int loc = (len / 2) - 1;
        for (int i = 0; i < this.pieces.size(); i++){
            this.pieces.get(i).init_location(loc);
        }
    }

    public int[][] render(int dimention) {
        this.board = new int[dimention][dimention];
        for (int i = 0; i < dimention; i++) {
            for (int j = 0; j < dimention; j++) {
                this.board[i][j] = -1;
            }
        }
        init_location();
        while (true) {
            if (this.pieces.get(this.turn).out_of_board(dimention)) {
                break;
            }
            if(this.pieces.get(this.turn).valid_location(this.board, this.pieces.size())) {
                place_piece();
            }
            this.pieces.get(this.turn).move_to_next_location();
        }
        return this.board.clone();
    }
}
