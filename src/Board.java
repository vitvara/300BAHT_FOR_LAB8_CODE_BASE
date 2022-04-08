import java.util.Random;

public class Board {
    private Cell [][] cells;
    private int size;
    private int mineCount;
    private Random random = new Random();

    public Board(int size, int mineCount) {
        this.size = size;
        this.mineCount = mineCount;
        initCells();
        seedMines();
        generateNumber();
    }
    private void initCells() {
        cells = new Cell[size][size];

        for(int row=0; row<size; row++) {
            for(int column=0; column<size; column++) {
                cells[row][column] = new Cell();
            }
        }
    }

    private void generateNumber() {
        for(int row=0; row < size; row++){
            for(int col=0; col<size; col++){
                Cell cell = getCell(row,col);
                if(cell.isMine()){
                    continue;
                }
                int [][] pairs = {
                        {-1, -1}, {-1, 0}, {-1, 1},
                        { 0, -1},/* CELL */{ 0, 1},
                        { 1, -1}, { 1, 0}, { 1, 1}
                };
                int count = 0;
                for (int [] pair: pairs){
                    Cell adj = getCell(row + pair[0], col + pair[1]);
                    if(adj != null && adj.isMine()) {
                        count++;
                    }
                }
                cell.setAdjacentMines(count);
            }
        }
    }

    private void seedMines() {
        int seeded = 0;
        while(seeded < mineCount) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            Cell cell = getCell(row,col);
            if(cell.isMine()){
                continue;
            }
            cell.setMine(true);
            seeded++;
        }
    }

    public void uncover(int row, int col){
        Cell cell = getCell(row,col);
        if(cell == null){
            return;
        }
        cell.setCovered(false);

        if(cell.getAdjacentMines() == 0 && !cell.isMine()){
            int [][] pairs = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    { 0, -1},/* CELL */{ 0, 1},
                    { 1, -1}, { 1, 0}, { 1, 1}
            };
            for(int [] pair: pairs){
                Cell adjCell = getCell(row+pair[0],col+pair[1]);
                if (adjCell != null && adjCell.isCovered()) {
                    adjCell.setCovered(false);
                    if(!adjCell.isMine()){
                        uncover(row+pair[0],col+pair[1]);
                    }
                }



            }
        }
    }

    public boolean mineUncover() {
        for(int row=0; row < size; row++){
            for(int col=0; col<size; col++){
                Cell cell = getCell(row, col);
                if(cell.isMine() && !cell.isCovered()){
                    return true;
                }
            }
        }
        return false;
    }

    public Cell getCell(int row, int col) {
        if(row<0 || col <0 || row >= size|| col >= size){
            return null;
        }
        return cells[row][col];
    }
}
