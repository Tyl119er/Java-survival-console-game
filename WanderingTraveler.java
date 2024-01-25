import java.util.Scanner;
import java.util.Random;
public class WanderingTraveler {
    private static final int GRID_WIDTH = 100;
    private static final int GRID_HEIGHT = 45;
    private static final int ENERGY_PER_MOVE = 10;
    private static final int MAX_ENERGY = 100; // Максимальное значение энергии.
    private int x;
    private int y;
    private int energy;
    private boolean[][] mountains;  // Добавим поля для хранения информации о горах.
    public WanderingTraveler(int initialEnergy) {
        x = GRID_WIDTH / 2;
        y = GRID_HEIGHT / 2;
        energy = initialEnergy;
        mountains = new boolean[GRID_HEIGHT][GRID_WIDTH];
        generateMountains(); // Вызовем метод для генерации гор.
    }
     // Метод для генерации овальных гор.
    private void generateMountains() {
        Random rand = new Random();
        int mountainsCount = 5; // Например, генерировать 5 гор.
        for (int k = 0; k < mountainsCount; k++) {
            int centerX = rand.nextInt(GRID_WIDTH);
            int centerY = rand.nextInt(GRID_HEIGHT);
            int width = rand.nextInt(14) + 15; // От 15 до 29.
            int height = rand.nextInt(8) + 5; // От 5 до 13.
            // Овальная форма горы.
            for (int i = 0; i < GRID_HEIGHT; i++) {
                for (int j = 0; j < GRID_WIDTH; j++) {
                    // Проверяем, находится ли точка внутри эллипса.
                    if ((Math.pow(j - centerX, 2) / Math.pow(width / 2, 2)) +
                        (Math.pow(i - centerY, 2) / Math.pow(height / 2, 2)) <= 1) {
                        mountains[i][j] = true;
                    }
                }
            }
        }
    }
    public void move(char direction) {
        if (energy < ENERGY_PER_MOVE) {
            System.out.println("You are too tired to move. Consider resting.");
            return;
        }
        int newX = x, newY = y;
        switch (direction) {
            case 'w': newY = y - 1; break;
            case 's': newY = y + 1; break;
            case 'a': newX = x - 1; break;
            case 'd': newX = x + 1; break;
            default: System.out.println("Invalid direction."); return;
        }
        // Проверяем, не является ли новая позиция частью горы.
        if (newX >= 0 && newX < GRID_WIDTH && newY >= 0 && newY < GRID_HEIGHT && !mountains[newY][newX]) {
            x = newX;
            y = newY;
            energy -= ENERGY_PER_MOVE;
        } else {
            System.out.println("You can't climb mountains! Choose another path.");
            return; // Возвращаемся, чтобы не печатать карту и не тратить энергию.
        }
        printMap();
        System.out.println("Player coordinates: (" + x + ", " + y + ")");
    }
    public void rest() {
        energy += MAX_ENERGY / 2; // Восполняем энергию наполовину.
        if (energy > MAX_ENERGY) {
            energy = MAX_ENERGY; // Убедимся, что энергия не превышает максимум.
        }
        System.out.println("You've rested and regained some of your energy.");
        printMap();
    }
    public void printMap() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (i == y && j == x) {
                    System.out.print('@');
                } else if (mountains[i][j]) { // Проверяем, является ли точка частью горы.
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        printEnergyBar();
    }
    private void printEnergyBar() {
        System.out.print("Energy: [");
        int energyBarLength = 10; // Длина полоски энергии.
        int filledLength = (energy * energyBarLength) / MAX_ENERGY;
        for (int i = 0; i < energyBarLength; i++) {
            if (i < filledLength) {
                System.out.print("#");
            } else {
                System.out.print("-");
            }
        }
        System.out.println("] " + energy + "/" + MAX_ENERGY);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WanderingTraveler traveler = new WanderingTraveler(MAX_ENERGY);
        traveler.printMap();
        System.out.println("Use WASD to move. Press 'r' to rest. Press 'q' to quit.");
        char command;
        do {
            System.out.print("Enter your move: ");
            command = scanner.next().charAt(0);
            switch (command) {
                case 'w':
                case 'a':
                case 's':
                case 'd':
                    traveler.move(command);
                    break;
                case 'r':
                    traveler.rest();
                    break;
                case 'q':
                    System.out.println("Quitting the game.");
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        } while (command != 'q');
        scanner.close();
    }
}