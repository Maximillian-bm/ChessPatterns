import java.util.List;

public class Piece {

    private int id;

    private final List<int[]> moves;

    private String color;

    private int completed_straight_moves = 0;

    private int[] moves_before_turn;

    private int[] location;

    public Piece(List<int[]> moves, String color, int id) {
        this.id = id;
        this.moves = moves;
        this.color = color;
        this.moves_before_turn = new int[2];
        this.moves_before_turn[0] = 0;
        this.moves_before_turn[1] = 1;
        this.location = new int[2];
    }

    public int get_id(){
        return this.id;
    }

    public List<int[]> get_moves() {
        return this.moves;
    }

    public String get_color(){
        return this.color;
    }

    public int[] get_location(){
        return this.location;
    }

    private void update_moves() {
        if (this.moves_before_turn[0] == 0) {
            if(this.moves_before_turn[1] < 0 && Math.abs(this.moves_before_turn[1]) == this.completed_straight_moves) {
                this.moves_before_turn[0] = -this.completed_straight_moves;
                this.moves_before_turn[1] = 0;
                this.completed_straight_moves = 0;
            }else if (this.moves_before_turn[1] > 0 && Math.abs(this.moves_before_turn[1]) == this.completed_straight_moves) {
                this.moves_before_turn[0] = this.completed_straight_moves;
                this.moves_before_turn[1] = 0;
                this.completed_straight_moves = 0;
            }
        }else if (this.moves_before_turn[1] == 0) {
            if(this.moves_before_turn[0] < 0 && Math.abs(this.moves_before_turn[0]) == this.completed_straight_moves) {
                this.moves_before_turn[1] = this.completed_straight_moves + 1;
                this.moves_before_turn[0] = 0;
                this.completed_straight_moves = 0;
            }else if (this.moves_before_turn[0] > 0 && Math.abs(this.moves_before_turn[0]) == this.completed_straight_moves) {
                this.moves_before_turn[1] = - (this.completed_straight_moves + 1);
                this.moves_before_turn[0] = 0;
                this.completed_straight_moves = 0;
            }
        }
    }

    public boolean out_of_board(int dimention) {
        return (this.location[0] >= dimention || this.location[0] < 0 ||this.location[1] >= dimention || this.location[1] < 0);
    }

    public boolean valid_location(int[][] board, int nr_of_pieces) {
        if (board[this.location[0]][this.location[1]] != -1) return false;
        for (int i = 0; i < nr_of_pieces; i++) {
            if(i == id && nr_of_pieces > 1) {
                continue;
            }
            for (int j = 0; j < this.moves.size(); j++) {
                int x = this.location[0] - moves.get(j)[0];
                int y = this.location[1] - moves.get(j)[1];
                try {
                    if(board[x][y] == i) {
                    return false;
                }
                } catch (Exception e) {

                }
            }
        }
        return true;
    }

    public void init_location(int loc) {
        this.completed_straight_moves = 0;
        this.moves_before_turn[0] = 0;
        this.moves_before_turn[1] = 1;
        this.location[0] = loc;
        this.location[1] = loc;
    }

    public void move_to_next_location() {
        update_moves();
        if (this.moves_before_turn[0] == 0) {
            if(this.moves_before_turn[1] < 0) {
                this.location[1]--;
                this.completed_straight_moves++;
            }else if (this.moves_before_turn[1] > 0) {
                this.location[1]++;
                this.completed_straight_moves++;
            }else {
                throw new Error("Somthing is wrong");
            }
        }else if (this.moves_before_turn[1] == 0) {
            if(this.moves_before_turn[0] < 0) {
                this.location[0]--;
                this.completed_straight_moves++;
            }else if (this.moves_before_turn[0] > 0) {
                this.location[0]++;
                this.completed_straight_moves++;
            }else {
                throw new Error("Somthing is wrong");
            }
        }else {
            throw new Error("Somthing is wrong");
        }
    }

}
