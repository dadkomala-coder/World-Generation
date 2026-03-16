import greenfoot.*;
import java.util.*;

public class MyWorld extends World
{
    private static final int NUM_COLORS = 10;
    private static final int GRID_SIZE = 70;
    private static final int CELL_SIZE = 5;
    
    private Tiles[][] tiles = new Tiles[GRID_SIZE][GRID_SIZE];
    private ArrayList<Integer>[][] possibilities;
    private boolean[][] collapsed;
    private boolean[][] adjacency = new boolean[NUM_COLORS][NUM_COLORS];
    private long lastTime;
    private final long DELAY_MS = 0;
    private Stack<int[]> propagationStack;

    public MyWorld()
    {
        super(GRID_SIZE, GRID_SIZE, CELL_SIZE);
        setPaintOrder(Tiles.class);

        // Adjacency: each color can touch itself and adjacent colors
        for (int i = 0; i < NUM_COLORS; i++) {
            adjacency[i][i] = true;
            if (i > 0) adjacency[i][i - 1] = true;
            if (i < NUM_COLORS - 1) adjacency[i][i + 1] = true;
        }

        possibilities = new ArrayList[GRID_SIZE][GRID_SIZE];
        collapsed = new boolean[GRID_SIZE][GRID_SIZE];

        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                possibilities[y][x] = new ArrayList<>();
                for (int c = 0; c < NUM_COLORS; c++) {
                    possibilities[y][x].add(c);
                }

                tiles[y][x] = new Tiles();
                addObject(tiles[y][x], x, y);
                tiles[y][x].setColor(-1);
            }
        }

        // Center is always yellow (color 0)
        int centerX = GRID_SIZE / 2;
        int centerY = GRID_SIZE / 2;
        collapseCell(centerX, centerY, 5);

        propagationStack = new Stack<>();
        addNeighborsToStack(centerX, centerY);

        lastTime = System.currentTimeMillis();
    }

    public void act()
    {
        long now = System.currentTimeMillis();
        if (now - lastTime >= DELAY_MS) {
            if (!allCollapsed()) {
                if (!propagationStack.isEmpty()) {
                    propagate();
                } else {
                    collapseNext();
                }
                lastTime = now;
            }
        }
    }

    private boolean allCollapsed()
    {
        for (boolean[] row : collapsed) {
            for (boolean b : row) {
                if (!b) return false;
            }
        }
        return true;
    }

    private void collapseNext()
    {
        // Find lowest entropy cells
        double minEntropy = Double.MAX_VALUE;
        ArrayList<int[]> candidates = new ArrayList<>();

        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (!collapsed[y][x]) {
                    int size = possibilities[y][x].size();
                    if (size == 0) {
                        System.out.println("CONTRADICTION at " + x + "," + y);
                        return;
                    }
                    double entropy = size + Greenfoot.getRandomNumber(100) / 10000.0;
                    if (entropy < minEntropy) {
                        minEntropy = entropy;
                        candidates.clear();
                        candidates.add(new int[]{x, y});
                    } else if (Math.abs(entropy - minEntropy) < 0.01) {
                        candidates.add(new int[]{x, y});
                    }
                }
            }
        }

        if (candidates.isEmpty()) return;

        int[] pos = candidates.get(Greenfoot.getRandomNumber(candidates.size()));
        int x = pos[0], y = pos[1];
        List<Integer> poss = new ArrayList<>(possibilities[y][x]); // copy to sort safely
        Collections.sort(poss); // ensure ordered: low to high

        int chosenColor;

        // Determine the "next" color (one step toward purple)
        int currentMax = Collections.max(poss);
        int preferredNext = -1;
        if (currentMax < NUM_COLORS - 1) {
            preferredNext = currentMax + 1;
        }

        // 50% chance to pick the preferred next color (if available)
        if (preferredNext != -1 && poss.contains(preferredNext) && Greenfoot.getRandomNumber(100) < 50) {
            chosenColor = preferredNext;
        } else {
            // Otherwise: equal chance among ALL remaining possibilities
            // (this includes same color, lower, or higher jumps)
            chosenColor = poss.get(Greenfoot.getRandomNumber(poss.size()));
        }

        collapseCell(x, y, chosenColor);
        addNeighborsToStack(x, y);
    }

    private void collapseCell(int x, int y, int color)
    {
        possibilities[y][x].clear();
        possibilities[y][x].add(color);
        collapsed[y][x] = true;
        tiles[y][x].setColor(color);
    }

    private void addNeighborsToStack(int x, int y)
    {
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE && !collapsed[ny][nx]) {
                propagationStack.push(new int[]{nx, ny});
            }
        }
    }

    private void propagate()
    {
        Set<String> processed = new HashSet<>();
        while (!propagationStack.isEmpty()) {
            int[] pos = propagationStack.pop();
            String key = pos[0] + "," + pos[1];
            if (processed.contains(key)) continue;
            processed.add(key);

            if (collapsed[pos[1]][pos[0]]) continue;

            if (updatePossibilities(pos[0], pos[1])) {
                addNeighborsToStack(pos[0], pos[1]);
            }
        }
    }

    private boolean updatePossibilities(int x, int y)
    {
        ArrayList<Integer> newPoss = new ArrayList<>(possibilities[y][x]);
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE && collapsed[ny][nx]) {
                int neighborColor = possibilities[ny][nx].get(0);
                newPoss.removeIf(c -> !adjacency[c][neighborColor]);
            }
        }

        if (newPoss.size() < possibilities[y][x].size()) {
            possibilities[y][x] = newPoss;
            return true;
        }
        return false;
    }
}