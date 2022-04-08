import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game extends JFrame {
    private Board board;
    private int boardSize = 10;
    private GridUI gridUI;
    private int mineCount = 10;

    public Game() {
        board  = new Board(boardSize, mineCount);
        gridUI = new GridUI();
        add(gridUI);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setVisible(true);
    }

    class  GridUI extends JPanel {
        public static final int CELL_PIXEL_SIZE = 30;
        private Image imageCell, imageFlag, imageMine;

        public GridUI() {
            int cellSize = boardSize * CELL_PIXEL_SIZE;
            setPreferredSize(new Dimension(cellSize,cellSize));
            imageCell = new ImageIcon("imgs/Cell.png").getImage();
            imageFlag = new ImageIcon("imgs/Flag.png").getImage();
            imageMine = new ImageIcon("imgs/Mine.png").getImage();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    int row = e.getY() / CELL_PIXEL_SIZE;
                    int col = e.getX() / CELL_PIXEL_SIZE;
                    Cell cell = board.getCell(row,col);
                    if (cell.isCovered()) {
                        if(SwingUtilities.isRightMouseButton(e)){
                            cell.setFlagged(!cell.isFlagged());
                        } else if(SwingUtilities.isLeftMouseButton(e) && !cell.isFlagged()) {
                            board.uncover(row,col);
                            if(board.mineUncover()) {
                                JOptionPane.showMessageDialog(Game.this,"You Lose!", "You hit the mine!",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }


                    repaint();
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for(int row=0; row < boardSize; row++) {
                for(int col=0; col < boardSize; col++) {
                    paintCell(g, row, col);
                }
            }
        }

        private void paintCell(Graphics g, int row, int col) {
            int x = col * CELL_PIXEL_SIZE;
            int y = row * CELL_PIXEL_SIZE;
            Cell cell = board.getCell(row, col);

            if(cell.isCovered()){
                g.drawImage(imageCell, x,y,CELL_PIXEL_SIZE,CELL_PIXEL_SIZE,Color.black,null);

            } else {
                g.setColor(Color.darkGray);
                g.fillRect(x,y,CELL_PIXEL_SIZE,CELL_PIXEL_SIZE);
                g.setColor(Color.gray);
                g.fillRect(x+1,y+1,CELL_PIXEL_SIZE+4,CELL_PIXEL_SIZE+4);
                if(cell.isMine()) {
                    g.drawImage(imageMine,x,y,CELL_PIXEL_SIZE,CELL_PIXEL_SIZE,null,null);
                } else if(cell.getAdjacentMines() > 0) {
                    g.setColor(Color.black);
                    g.drawString(cell.getAdjacentMines() + "", x + (int)(CELL_PIXEL_SIZE*0.35),y + (int)(CELL_PIXEL_SIZE*0.55));
                }
            }



            if(cell.isFlagged()) {
                g.drawImage(imageFlag,x,y,CELL_PIXEL_SIZE,CELL_PIXEL_SIZE,null,null);
            }

        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
